package me.sbpro.cyberhub.items;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/** Hub items stay in their hotbar slots: no moving, swapping, dropping or offhand. */
public final class HubItemLockListener implements Listener {

    private final Plugin plugin;

    public HubItemLockListener(Plugin plugin) {
        this.plugin = plugin;
    }

    private boolean isHubItem(ItemStack item) {
        return HubItems.isHubItem(plugin, item);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        // Clicked item, item on the cursor, or the item a number key / F would swap in
        boolean locked = isHubItem(event.getCurrentItem()) || isHubItem(event.getCursor());

        if (!locked && event.getHotbarButton() >= 0) {
            locked = isHubItem(player.getInventory().getItem(event.getHotbarButton()));
        }

        if (!locked && event.getClick() == ClickType.SWAP_OFFHAND) {
            locked = isHubItem(player.getInventory().getItemInOffHand());
        }

        if (locked) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onDrag(InventoryDragEvent event) {
        if (isHubItem(event.getOldCursor())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onSwapHands(PlayerSwapHandItemsEvent event) {
        if (isHubItem(event.getMainHandItem()) || isHubItem(event.getOffHandItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent event) {
        if (isHubItem(event.getItemDrop().getItemStack())) event.setCancelled(true);
    }
}
