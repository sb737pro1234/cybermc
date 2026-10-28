package me.sbpro.cyberquests.menus;

import me.sbpro.cyberquests.CyberQuests;
import me.sbpro.cyberquests.Messages;
import me.sbpro.cyberquests.data.PlayerData;
import me.sbpro.cyberquests.quests.ActiveQuest;
import me.sbpro.cyberquests.quests.QuestDefinition;
import me.sbpro.cyberquests.quests.QuestManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.kyori.adventure.text.Component;

import java.util.*;

public class MenuManager {
    public static final int[] QUEST_SLOTS = {10, 12, 14, 16};
    public static final int[] EASIFY_SLOTS = {19, 21, 23, 25};
    /** Slot of the Quest List button in the main menu. */
    public static final int QUEST_LIST_BUTTON_SLOT = 11;
    /** Slot of the Coin Shop button in the main menu. */
    public static final int COIN_SHOP_BUTTON_SLOT = 15;

    private record PendingEasify(int slot, String questId) {}

    private final CyberQuests plugin;
    private final Map<UUID, PendingEasify> pendingRerolls = new HashMap<>();

    public MenuManager(CyberQuests plugin) { this.plugin = plugin; }

    public void openMain(Player player) {
        Inventory inv = createMenu(QuestMenuHolder.Type.MAIN, 27, Messages.QUEST_MENU_TITLE);
        inv.setItem(QUEST_LIST_BUTTON_SLOT, button(Material.WRITABLE_BOOK, Messages.ACTIVE_QUESTS_NAME, Messages.ACTIVE_QUESTS_LORE));
        inv.setItem(COIN_SHOP_BUTTON_SLOT, button(Material.EMERALD, Messages.COIN_SHOP_NAME, Messages.COIN_SHOP_LORE));
        player.openInventory(inv);
    }

    public void openQuestList(Player player) {
        Inventory inv = createMenu(QuestMenuHolder.Type.LIST, 36, Messages.QUEST_LIST_TITLE);
        populateQuestList(inv, player);
        player.openInventory(inv);
    }

    /** Fills (or refreshes) the quest list. Cooldown slots show a barrier and have no Easify button. */
    public void populateQuestList(Inventory inv, Player player) {
        PlayerData data = plugin.getQuestManager().getData(player.getUniqueId());
        long now = System.currentTimeMillis();

        for (int i = 0; i < PlayerData.SLOT_COUNT; i++) {
            inv.setItem(QUEST_SLOTS[i], null);
            inv.setItem(EASIFY_SLOTS[i], null);

            ActiveQuest active = data.getSlot(i);
            if (active == null) {
                long until = data.getCooldownUntil(i);
                if (until > 0L) inv.setItem(QUEST_SLOTS[i], cooldownBarrier(until - now));
                continue;
            }
            QuestDefinition quest = plugin.getQuestManager().getDefinition(active.getQuestId());
            if (quest == null) continue;

            inv.setItem(QUEST_SLOTS[i], questDisplay(active, quest));
            if (plugin.getQuestManager().canEasify(data, i)) {
                inv.setItem(EASIFY_SLOTS[i], button(Material.CLOCK, Messages.REROLL_BUTTON_NAME, Messages.REROLL_BUTTON_LORE));
            }
        }
    }

    /** Live-updates the cooldown timers when the player is looking at the quest list. */
    public void refreshQuestListIfOpen(Player player) {
        Inventory top = player.getOpenInventory().getTopInventory();
        if (!(top.getHolder() instanceof QuestMenuHolder holder) || holder.getType() != QuestMenuHolder.Type.LIST) return;
        populateQuestList(top, player);
    }

    private ItemStack cooldownBarrier(long remainingMillis) {
        String time = QuestManager.formatDuration(remainingMillis);
        return button(Material.BARRIER, Messages.SLOT_COOLDOWN_NAME, Messages.slotCooldownLore(time));
    }

    public void openRerollConfirmation(Player player, int index) {
        PlayerData data = plugin.getQuestManager().getData(player.getUniqueId());
        if (index < 0 || index >= PlayerData.SLOT_COUNT) return;
        long now = System.currentTimeMillis();
        if (data.isOnCooldown(index, now)) {
            player.sendMessage(Messages.slotOnCooldown(
                    QuestManager.formatDuration(data.getCooldownUntil(index) - now)));
            return;
        }
        ActiveQuest active = data.getSlot(index);
        if (active == null || !plugin.getQuestManager().canEasify(data, index)) return;
        QuestDefinition quest = plugin.getQuestManager().getDefinition(active.getQuestId());
        if (quest == null) return;

        pendingRerolls.put(player.getUniqueId(), new PendingEasify(index, active.getQuestId()));
        Inventory inv = createMenu(QuestMenuHolder.Type.EASIFY_CONFIRM, 27, Messages.EASIFY_CONFIRM_TITLE);
        inv.setItem(10, button(Material.RED_STAINED_GLASS_PANE, Messages.CANCEL_NAME, Messages.CANCEL_LORE));
        inv.setItem(13, questDisplay(active, quest));
        inv.setItem(16, button(Material.GREEN_STAINED_GLASS_PANE, Messages.CONFIRM_NAME, Messages.CONFIRM_LORE));
        player.openInventory(inv);
    }

    private ItemStack questDisplay(ActiveQuest active, QuestDefinition quest) {
        List<Component> lore = new ArrayList<>();
        lore.add(Messages.difficultyLabel(active.isEasy()));
        lore.add(Messages.progressLine(active.getProgress(), quest.getRequiredAmount()));
        lore.add(Messages.rewardLine(active.isEasy() ? 5 : 10));
        return button(Material.BOOK, Messages.questTitle(quest.getTitle()), lore);
    }

    /**
     * Returns the slot of the pending Easify confirmation, or null if none is pending or the slot's quest
     * is no longer the one that was shown in the confirmation menu.
     */
    public Integer getPendingReroll(Player player) {
        PendingEasify pending = pendingRerolls.get(player.getUniqueId());
        if (pending == null) return null;
        PlayerData data = plugin.getQuestManager().getData(player.getUniqueId());
        ActiveQuest active = data.getSlot(pending.slot());
        if (active == null || !active.getQuestId().equals(pending.questId())) return null;
        return pending.slot();
    }

    public void clearPending(Player player) { pendingRerolls.remove(player.getUniqueId()); }

    private Inventory createMenu(QuestMenuHolder.Type type, int size, Component title) {
        QuestMenuHolder holder = new QuestMenuHolder(type);
        Inventory inv = Bukkit.createInventory(holder, size, title);
        holder.setInventory(inv);
        return inv;
    }

    private ItemStack button(Material material, Component name, List<Component> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(name);
            meta.lore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }
}
