package me.sbpro.cyberquests.menus;

import me.sbpro.cyberquests.CyberQuests;
import me.sbpro.cyberquests.Tutorial;
import me.sbpro.cyberquests.Messages;
import me.sbpro.cyberquests.data.PlayerData;
import me.sbpro.cyberquests.quests.ActiveQuest;
import me.sbpro.cyberquests.quests.TutorialStep;
import net.kyori.adventure.text.format.NamedTextColor;
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
    /** Back button in the quest list (bottom row, centre). */
    public static final int QUEST_LIST_BACK_SLOT = 31;

    /** Hub buttons. */
    public static final int TUTORIAL_BUTTON_SLOT = 11;
    public static final int QUEST_BOARD_BUTTON_SLOT = 15;

    /**
     * Tutorial menu layout (54 slots): info book on top, steps in three rows of seven, and the Quest Board goal
     * in the slot straight after the last step. Holds up to 20 steps plus the goal.
     */
    public static final int TUTORIAL_INFO_SLOT = 4;
    public static final int[] TUTORIAL_STEP_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34};
    public static final int TUTORIAL_BACK_SLOT = 49;

    private record PendingEasify(int slot, String questId) {}

    private final CyberQuests plugin;
    private final Map<UUID, PendingEasify> pendingRerolls = new HashMap<>();

    public MenuManager(CyberQuests plugin) { this.plugin = plugin; }

    /** The /quests hub: Tutorial (progression quests) and the Quest Board (locked until the Tutorial is finished). */
    public void openHub(Player player) {
        PlayerData data = plugin.getQuestManager().getData(player.getUniqueId());
        Inventory inv = createMenu(QuestMenuHolder.Type.HUB, 27, Messages.HUB_MENU_TITLE);
        inv.setItem(TUTORIAL_BUTTON_SLOT, button(Material.FILLED_MAP, Messages.TUTORIAL_NAME,
                Messages.tutorialButtonLore(data.getTutorialStep(), Tutorial.size())));
        inv.setItem(QUEST_BOARD_BUTTON_SLOT, data.isBoardUnlocked()
                ? button(Material.WRITABLE_BOOK, Messages.QUEST_BOARD_NAME, Messages.QUEST_BOARD_LORE)
                : button(Material.WRITABLE_BOOK, Messages.QUEST_BOARD_LOCKED_NAME, Messages.QUEST_BOARD_LOCKED_LORE));
        player.openInventory(inv);
    }

    public void openTutorial(Player player) {
        Inventory inv = createMenu(QuestMenuHolder.Type.TUTORIAL, 54, Messages.TUTORIAL_MENU_TITLE);
        inv.setItem(TUTORIAL_INFO_SLOT, button(Material.BOOK, Messages.TUTORIAL_INFO_NAME, Messages.TUTORIAL_INFO_LORE));
        inv.setItem(TUTORIAL_BACK_SLOT, backButton());
        populateTutorial(inv, player);
        player.openInventory(inv);
    }

    /** Completed steps are lime glass, the current step shows its icon and progress, later steps are gray glass. */
    public void populateTutorial(Inventory inv, Player player) {
        PlayerData data = plugin.getQuestManager().getData(player.getUniqueId());
        int current = data.getTutorialStep();
        int shown = Math.min(Tutorial.size(), TUTORIAL_STEP_SLOTS.length - 1); // keep one slot for the goal
        for (int i = 0; i < shown; i++) {
            TutorialStep step = Tutorial.get(i);
            QuestDefinition quest = step.quest();
            ItemStack item;
            if (i < current) {
                item = button(Material.LIME_STAINED_GLASS_PANE,
                        Messages.tutorialStepName(i + 1, quest.getTitle(), NamedTextColor.GREEN),
                        Messages.tutorialCompletedLore(quest.getDescription(), step.rewardLabel()));
            } else if (i == current) {
                item = button(step.icon(),
                        Messages.tutorialStepName(i + 1, quest.getTitle(), Messages.CUSTOM),
                        Messages.tutorialCurrentLore(quest.getDescription(), data.getTutorialProgress(),
                                quest.getRequiredAmount(), step.rewardLabel()));
            } else {
                item = button(Material.GRAY_STAINED_GLASS_PANE,
                        Messages.tutorialStepName(i + 1, quest.getTitle(), NamedTextColor.GRAY),
                        Messages.tutorialLockedLore(quest.getDescription(), step.rewardLabel()));
            }
            inv.setItem(TUTORIAL_STEP_SLOTS[i], item);
        }
        inv.setItem(TUTORIAL_STEP_SLOTS[shown], data.isBoardUnlocked()
                ? button(Material.WRITABLE_BOOK, Messages.QUEST_BOARD_NAME, Messages.QUEST_BOARD_GOAL_LORE)
                : button(Material.WRITABLE_BOOK, Messages.QUEST_BOARD_LOCKED_NAME, Messages.QUEST_BOARD_GOAL_LOCKED_LORE));
    }

    /** Live-updates progress when the player is looking at the Tutorial menu. */
    public void refreshTutorialIfOpen(Player player) {
        Inventory top = player.getOpenInventory().getTopInventory();
        if (!(top.getHolder() instanceof QuestMenuHolder holder) || holder.getType() != QuestMenuHolder.Type.TUTORIAL) return;
        populateTutorial(top, player);
    }

    public void openQuestList(Player player) {
        Inventory inv = createMenu(QuestMenuHolder.Type.LIST, 36, Messages.QUEST_LIST_TITLE);
        inv.setItem(QUEST_LIST_BACK_SLOT, backButton());
        populateQuestList(inv, player);
        player.openInventory(inv);
    }

    private ItemStack backButton() {
        return button(Material.ARROW, Messages.BACK_NAME, Messages.BACK_LORE);
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
