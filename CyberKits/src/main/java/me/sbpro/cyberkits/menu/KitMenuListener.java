package me.sbpro.cyberkits.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;

import me.sbpro.cyberkits.CyberKits;
import me.sbpro.cyberkits.Kits;
import me.sbpro.cyberkits.kit.Kit;

public final class KitMenuListener implements Listener {

    private final CyberKits plugin;

    public KitMenuListener(CyberKits plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder(false);
        if (!(event.getWhoClicked() instanceof Player player)) return;

        // The editor is a normal inventory: anything can be moved in and out
        if (holder instanceof KitEditorHolder) return;
        if (!(holder instanceof KitsMenuHolder || holder instanceof KitPreviewHolder || holder instanceof KitEditorSelectHolder)) return;

        // Nothing in these menus (or your own inventory while one is open) can be moved
        event.setCancelled(true);
        if (event.getClickedInventory() != event.getInventory()) return;

        // /kits: left-click claims, right-click previews
        if (holder instanceof KitsMenuHolder menu) {
            Kit kit = Kits.bySlot(event.getSlot());
            if (kit == null) return;

            if (event.isRightClick()) {
                plugin.openPreview(player, kit);
            } else if (event.isLeftClick()) {
                plugin.claimer().claim(player, kit);
                menu.render();
            }
            return;
        }

        // Preview: Back button returns to /kits
        if (holder instanceof KitPreviewHolder) {
            if (event.getSlot() == KitPreviewHolder.BACK_SLOT) plugin.openMenu(player);
            return;
        }

        // /kits editor: pick a kit to edit
        Kit kit = Kits.bySlot(event.getSlot());
        if (kit != null) plugin.openEditor(player, kit);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        InventoryHolder holder = event.getInventory().getHolder(false);
        if (holder instanceof KitsMenuHolder || holder instanceof KitPreviewHolder || holder instanceof KitEditorSelectHolder) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof KitEditorHolder editor)) return;
        plugin.saveEditor(event.getPlayer(), editor);
    }
}
