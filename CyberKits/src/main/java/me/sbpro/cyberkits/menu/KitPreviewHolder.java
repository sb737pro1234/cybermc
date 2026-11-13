package me.sbpro.cyberkits.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import me.sbpro.cyberkits.Messages;
import me.sbpro.cyberkits.kit.Kit;
import me.sbpro.cyberkits.kit.KitItemManager;

/** Read-only view of a kit's items (right-click in /kits). Top 45 slots = items, Back button in slot 49. */
public final class KitPreviewHolder implements InventoryHolder {

    public static final int SIZE = 54;
    public static final int BACK_SLOT = 49;

    private final Inventory inventory;

    public KitPreviewHolder(Kit kit, KitItemManager kitItems) {
        this.inventory = Bukkit.createInventory(this, SIZE, Messages.previewTitle(kit));

        ItemStack[] layout = kitItems.layout(kit);
        for (int slot = 0; slot < layout.length; slot++) inventory.setItem(slot, layout[slot]);

        ItemStack filler = MenuItems.filler();
        for (int slot = KitItemManager.EDITOR_SIZE; slot < SIZE; slot++) inventory.setItem(slot, filler);
        inventory.setItem(BACK_SLOT, MenuItems.item(Material.ARROW, Messages.PREVIEW_BACK_NAME, Messages.PREVIEW_BACK_LORE));
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
