package me.sbpro.grassggquests.menus;

import me.sbpro.grassggquests.GrassGGQuests;
import me.sbpro.grassggquests.Messages;
import me.sbpro.grassggquests.data.PlayerData;
import me.sbpro.grassggquests.quests.QuestManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class MenuListener implements Listener {
    private final GrassGGQuests plugin;

    public MenuListener(GrassGGQuests plugin) { this.plugin = plugin; }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        String title = ChatColor.stripColor(event.getView().getTitle());
        if (!isOurMenu(title)) return;
        event.setCancelled(true);
        if (event.getClickedInventory() == null || event.getClickedInventory() != event.getView().getTopInventory()) return;

        int slot = event.getRawSlot();
        if (title.equals(ChatColor.stripColor(Messages.QUEST_MENU_TITLE))) {
            if (slot == MenuManager.QUEST_LIST_BUTTON_SLOT) {
                plugin.getMenus().openQuestList(player);
            } else if (slot == MenuManager.COIN_SHOP_BUTTON_SLOT) {
                if (!player.hasPermission("grassgg.coins.shop")) return;
                player.openInventory(me.sbpro.grassggcoins.gui.MenuFactory.createShopMenu(plugin.getCoinsPlugin(), player));
            }
            return;
        }

        if (title.equals(ChatColor.stripColor(Messages.QUEST_LIST_TITLE))) {
            PlayerData data = plugin.getQuestManager().getData(player.getUniqueId());
            long now = System.currentTimeMillis();
            for (int i = 0; i < PlayerData.SLOT_COUNT; i++) {
                if (slot == MenuManager.EASIFY_SLOTS[i]) {
                    // The button only exists on Normal quests that are not on cooldown; re-checked here.
                    plugin.getMenus().openRerollConfirmation(player, i);
                    return;
                }
                if (slot == MenuManager.QUEST_SLOTS[i] && data.isOnCooldown(i, now)) {
                    player.sendMessage(Messages.PREFIX + Messages.SLOT_ON_COOLDOWN.replace("%time%",
                            QuestManager.formatDuration(data.getCooldownUntil(i) - now)));
                    return;
                }
            }
            return;
        }

        if (title.equals(ChatColor.stripColor(Messages.EASIFY_CONFIRM_TITLE))) {
            if (slot == 10) {
                plugin.getMenus().clearPending(player);
                plugin.getMenus().openQuestList(player);
            } else if (slot == 16) {
                Integer index = plugin.getMenus().getPendingReroll(player);
                if (index != null) plugin.getQuestManager().easify(player, index);
                plugin.getMenus().clearPending(player);
                plugin.getMenus().openQuestList(player);
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        String title = ChatColor.stripColor(event.getView().getTitle());
        if (isOurMenu(title)) event.setCancelled(true);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player player && ChatColor.stripColor(event.getView().getTitle()).equals(ChatColor.stripColor(Messages.EASIFY_CONFIRM_TITLE))) {
            plugin.getMenus().clearPending(player);
        }
    }

    private boolean isOurMenu(String title) {
        return title.equals(ChatColor.stripColor(Messages.QUEST_MENU_TITLE))
                || title.equals(ChatColor.stripColor(Messages.QUEST_LIST_TITLE))
                || title.equals(ChatColor.stripColor(Messages.EASIFY_CONFIRM_TITLE));
    }
}
