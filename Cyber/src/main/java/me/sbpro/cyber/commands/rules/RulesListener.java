package me.sbpro.cyber.commands.rules;

import org.bukkit.entity.Player;
import me.sbpro.cyber.Messages;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class RulesListener implements Listener {

    private static final String RULES_URL =
            "https://docs.google.com/document/d/1wZWo1C1_vQj826vAjiInfvQced7srQfRcTJrb9H-BoQ/edit?usp=sharing";

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!(event.getView().getTopInventory().getHolder() instanceof RulesMenuHolder)) {
            return;
        }

        // Prevent anything from being moved inside the Rules menu
        if (event.getRawSlot() < event.getView().getTopInventory().getSize()) {
            event.setCancelled(true);
        }

        // When the book is clicked
        if (event.getRawSlot() == 13 && event.getWhoClicked() instanceof Player player) {
            player.sendMessage(Messages.RULES_LINK_LINE);
            player.sendMessage(Messages.RULES_LINK_MESSAGE.clickEvent(ClickEvent.openUrl(RULES_URL)));
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
