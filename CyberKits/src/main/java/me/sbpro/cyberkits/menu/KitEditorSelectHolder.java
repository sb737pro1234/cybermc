package me.sbpro.cyberkits.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import me.sbpro.cyberkits.Kits;
import me.sbpro.cyberkits.Messages;
import me.sbpro.cyberkits.kit.Kit;
import me.sbpro.cyberkits.kit.KitItemManager;

/** /kits editor: pick which kit to edit. Same layout as the /kits menu. */
public final class KitEditorSelectHolder implements InventoryHolder {

    private final Inventory inventory;

    public KitEditorSelectHolder(KitItemManager kitItems) {
        this.inventory = Bukkit.createInventory(this, Kits.MENU_SIZE, Messages.EDITOR_SELECT_TITLE);

        ItemStack filler = MenuItems.filler();
        for (int slot = 0; slot < inventory.getSize(); slot++) inventory.setItem(slot, filler);

        for (Kit kit : Kits.KITS) {
            if (kit.slot() < 0 || kit.slot() >= inventory.getSize()) continue;
            inventory.setItem(kit.slot(), MenuItems.item(Material.CHEST_MINECART,
                    Messages.kitName(kit), Messages.editorSelectLore(kitItems.count(kit))));
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
