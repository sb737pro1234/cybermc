package me.sbpro.cyber.commands.gamemode;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/** Marks the gamemode confirmation menu so the listener never has to compare inventory titles. */
public final class GamemodeConfirmHolder implements InventoryHolder {

    private Inventory inventory;

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
