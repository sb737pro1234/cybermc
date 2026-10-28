package me.sbpro.cyberquests.menus;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/** Identifies the quest menus so listeners never have to compare inventory titles. */
public final class QuestMenuHolder implements InventoryHolder {

    public enum Type { MAIN, LIST, EASIFY_CONFIRM }

    private final Type type;
    private Inventory inventory;

    public QuestMenuHolder(Type type) {
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
