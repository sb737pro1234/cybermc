package me.sbpro.cyber.commands.rules;

import org.bukkit.entity.Player;
import me.sbpro.cyber.Messages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class RulesListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!(event.getView().getTopInventory().getHolder() instanceof RulesMenuHolder)) {
            return;
        }

        // Prevent anything from being moved inside the Rules menu
        if (event.getRawSlot() < event.getView().getTopInventory().getSize()) {
            event.setCancelled(true);
        }

        // When a rules section is clicked, close the menu and send its document link in chat
        RulesCategory category = RulesCategory.fromSlot(event.getRawSlot());
        if (category != null && event.getWhoClicked() instanceof Player player) {
            player.closeInventory();
            player.sendMessage(Messages.RULES_LINK_LINE);
            player.sendMessage(category.link());
            player.sendMessage(Messages.RULES_LINK_LINE);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {

        if (!(event.getView().getTopInventory().getHolder() instanceof RulesMenuHolder)) {
            return;
        }

        // Prevent dragging items into the Rules menu
        for (int slot : event.getRawSlots()) {
            if (slot < event.getView().getTopInventory().getSize()) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
