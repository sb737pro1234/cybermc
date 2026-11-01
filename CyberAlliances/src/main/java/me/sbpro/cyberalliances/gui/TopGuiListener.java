package me.sbpro.cyberalliances.gui;

import me.sbpro.cyberalliances.CyberAlliances;
import me.sbpro.cyberalliances.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public final class TopGuiListener implements Listener {
    private final CyberAlliances plugin;

    public TopGuiListener(CyberAlliances plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof TopGuiHolder holder)) {
            return;
        }

        // Block everything, including shift-clicks coming from the player's own inventory.
        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        if (event.getClickedInventory() != event.getInventory() || event.getCurrentItem() == null) {
            return;
        }

        switch (event.getSlot()) {
            case TopGui.SLOT_PREVIOUS -> {
                if (holder.getPage() <= 0) {
                    player.sendMessage(Messages.NO_PREVIOUS_PAGE);
                    return;
                }
                TopGui.open(plugin, player, holder.getPage() - 1);
            }
            case TopGui.SLOT_NEXT -> {
                if (holder.getPage() >= TopGui.getMaxPage(plugin)) {
                    player.sendMessage(Messages.NO_NEXT_PAGE);
                    return;
                }
                TopGui.open(plugin, player, holder.getPage() + 1);
            }
            case TopGui.SLOT_YOU -> TopGui.openOwnPage(plugin, player);
            default -> {
                // Alliance entries are display-only.
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof TopGuiHolder) {
            event.setCancelled(true);
        }
    }
}
