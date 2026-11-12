package me.sbpro.cyberhub.items.selector;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;

import me.sbpro.cyberhub.Messages;

public final class ServerSelectorListener implements Listener {

    private final Plugin plugin;

    public ServerSelectorListener(Plugin plugin) {
        this.plugin = plugin;
    }

    // Right-click (air or block) with the compass opens the menu
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        if (!ServerSelector.isSelector(plugin, player.getInventory().getItemInMainHand())) return;

        event.setCancelled(true);
        player.openInventory(new ServerSelectorHolder().getInventory());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof ServerSelectorHolder holder)) return;

        // Nothing in the menu (or your own inventory while it's open) can be moved
        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getClickedInventory() != holder.getInventory()) return;

        ServerSelectorHolder.Entry entry = holder.entry(event.getSlot());
        if (entry == null) return;

        if (entry.close()) player.closeInventory();
        if (entry.server() == null) return;

        if (entry.server().equals("hub")) {
            player.sendMessage(Messages.SELECTOR_ALREADY_HERE);
            return;
        }

        player.sendMessage(Messages.selectorConnecting(entry.displayName()));
        ServerConnector.connect(plugin, player, entry.server());
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder(false) instanceof ServerSelectorHolder) {
            event.setCancelled(true);
        }
    }
}
