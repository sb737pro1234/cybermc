package me.sbpro.cyberkits.menu;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import me.sbpro.cyberkits.Messages;
import me.sbpro.cyberkits.kit.Kit;
import me.sbpro.cyberkits.kit.KitItemManager;

/** Editable 45-slot inventory of a kit's items. Saved to kititems.yml when closed. */
public final class KitEditorHolder implements InventoryHolder {

    private final Kit kit;
    private final Inventory inventory;

    public KitEditorHolder(Kit kit, KitItemManager kitItems) {
        this.kit = kit;
        this.inventory = Bukkit.createInventory(this, KitItemManager.EDITOR_SIZE, Messages.editorTitle(kit));
        inventory.setContents(kitItems.layout(kit));
    }

    public Kit kit() {
        return kit;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
