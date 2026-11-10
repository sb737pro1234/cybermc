package me.sbpro.cyberstaff.commands.staffguide;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class StaffGuideListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getView().getTopInventory().getHolder() instanceof StaffGuideHolder holder)) {
            return;
        }

        // Display only: nothing can be moved, taken or shift-clicked in any guide menu
        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        // Ignore clicks in the player's own inventory or outside the window
        if (event.getClickedInventory() == null
                || event.getClickedInventory() != event.getView().getTopInventory()) {
            return;
        }

        int slot = event.getRawSlot();

        if (holder.getType() == StaffGuideHolder.Type.MAIN) {
            // Main menu
            switch (slot) {
                case StaffGuideMenus.SLOT_PROTOCOL -> StaffGuideMenus.openProtocol(player);
                case StaffGuideMenus.SLOT_COMMANDS -> StaffGuideMenus.openCommands(player);
                case StaffGuideMenus.SLOT_RULES -> {
                    // Opens the /rules menu (provided by another plugin).
                    player.closeInventory();
                    player.performCommand("rules");
                }
                default -> { }
            }
        } else if (slot == StaffGuideMenus.SLOT_BACK) {
            // Protocol / Commands menus: back arrow returns to the main menu
            StaffGuideMenus.openMain(player);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getView().getTopInventory().getHolder() instanceof StaffGuideHolder)) {
            return;
        }

        // Prevent dragging items into any guide menu
        for (int slot : event.getRawSlots()) {
            if (slot < event.getView().getTopInventory().getSize()) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
