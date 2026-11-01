package me.sbpro.cyberstatistics.stats;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import java.util.function.Consumer;

public final class StatisticsListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        Inventory top = event.getView().getTopInventory();

        if (!(top.getHolder() instanceof StatisticsHolder holder)) {
            return;
        }

        // Nothing may be taken, moved or shift-clicked in or out of the menu.
        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if (event.getClickedInventory() != top) {
            return;
        }

        Consumer<Player> action = holder.getAction(event.getSlot());

        if (action == null) {
            return;
        }

        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
        action.accept(player);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (event.getView().getTopInventory().getHolder() instanceof StatisticsHolder) {
            event.setCancelled(true);
        }
    }
}
