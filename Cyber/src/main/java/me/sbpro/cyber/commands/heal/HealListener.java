package me.sbpro.cyber.commands.heal;

import me.sbpro.cyber.Messages;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

public class HealListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        Inventory inventory = event.getInventory();

        if (!(inventory.getHolder() instanceof HealConfirmHolder holder)) {
            return;
        }

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        int slot = event.getRawSlot();

        // Cancel
        if (slot == 10) {
            player.closeInventory();
            return;
        }

        // Confirm
        if (slot == 16) {

            Player target = holder.getTarget();

            // Target left while menu was open
            if (!target.isOnline()) {
                player.closeInventory();
                player.sendMessage(Messages.PLAYER_NO_LONGER_ONLINE);
                return;
            }

            target.setHealth(target.getAttribute(Attribute.MAX_HEALTH).getValue());
            target.setFoodLevel(20);
            target.setSaturation(20);
            target.setFireTicks(0);

            player.closeInventory();

            if (target.equals(player)) {
                player.sendMessage(
                        Messages.HEALTH_RESTORED_SELF
                );
            } else {
                player.sendMessage(
                        Messages.healthRestoredOther(target.getName())
                );

                target.sendMessage(
                        Messages.healthRestoredByAdmin(player.getName())
                );
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {

        if (event.getInventory().getHolder() instanceof HealConfirmHolder) {
            event.setCancelled(true);
        }
    }
}