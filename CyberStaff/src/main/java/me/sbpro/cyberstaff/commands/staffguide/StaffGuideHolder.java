package me.sbpro.cyberstaff.commands.staffguide;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/** Marks the Staff Guide menus so the listener never has to compare inventory titles. */
public final class StaffGuideHolder implements InventoryHolder {

    public enum Type { MAIN, PROTOCOL, COMMANDS }

    private final Type type;
    private Inventory inventory;

    public StaffGuideHolder(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
