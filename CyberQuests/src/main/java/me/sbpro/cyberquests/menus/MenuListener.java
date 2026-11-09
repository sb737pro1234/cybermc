package me.sbpro.cyberquests.menus;

import me.sbpro.cyberquests.CyberQuests;
import me.sbpro.cyberquests.Messages;
import me.sbpro.cyberquests.data.PlayerData;
import me.sbpro.cyberquests.quests.QuestManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class MenuListener implements Listener {
    private final CyberQuests plugin;

    public MenuListener(CyberQuests plugin) { this.plugin = plugin; }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!(event.getView().getTopInventory().getHolder() instanceof QuestMenuHolder holder)) return;
        event.setCancelled(true);
        if (event.getClickedInventory() == null || event.getClickedInventory() != event.getView().getTopInventory()) return;

        int slot = event.getRawSlot();
        if (holder.getType() == QuestMenuHolder.Type.HUB) {
            if (slot == MenuManager.TUTORIAL_BUTTON_SLOT) {
                plugin.getMenus().openTutorial(player);
            } else if (slot == MenuManager.QUEST_BOARD_BUTTON_SLOT) {
                if (plugin.getQuestManager().getData(player.getUniqueId()).isBoardUnlocked()) {
                    plugin.getMenus().openQuestList(player);
                } else {
                    player.sendMessage(Messages.QUEST_BOARD_LOCKED);
                }
            }
            return;
        }

        if (holder.getType() == QuestMenuHolder.Type.TUTORIAL) {
            if (slot == MenuManager.TUTORIAL_BACK_SLOT) plugin.getMenus().openHub(player);
            return;
        }

        if (holder.getType() == QuestMenuHolder.Type.LIST) {
            if (slot == MenuManager.QUEST_LIST_BACK_SLOT) {
                plugin.getMenus().openHub(player);
                return;
            }
            PlayerData data = plugin.getQuestManager().getData(player.getUniqueId());
            long now = System.currentTimeMillis();
            for (int i = 0; i < PlayerData.SLOT_COUNT; i++) {
                if (slot == MenuManager.EASIFY_SLOTS[i]) {
                    // The button only exists on Normal quests that are not on cooldown; re-checked here.
                    plugin.getMenus().openRerollConfirmation(player, i);
                    return;
                }
                if (slot == MenuManager.QUEST_SLOTS[i] && data.isOnCooldown(i, now)) {
                    player.sendMessage(Messages.slotOnCooldown(
                            QuestManager.formatDuration(data.getCooldownUntil(i) - now)));
                    return;
                }
            }
            return;
        }

        if (holder.getType() == QuestMenuHolder.Type.EASIFY_CONFIRM) {
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
        if (event.getView().getTopInventory().getHolder() instanceof QuestMenuHolder) event.setCancelled(true);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player player
                && event.getView().getTopInventory().getHolder() instanceof QuestMenuHolder holder
                && holder.getType() == QuestMenuHolder.Type.EASIFY_CONFIRM) {
            plugin.getMenus().clearPending(player);
        }
    }
}
