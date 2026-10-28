package me.sbpro.cyberhomes.menu;

import me.sbpro.cyberhomes.model.Home;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.UUID;

/** Identifies the Homes menus (and the data they need) so listeners never compare inventory titles. */
public final class HomesMenuHolder implements InventoryHolder {

    public enum Type { HOMES, VIEW, DELETE }

    private final Type type;
    private final UUID targetId;
    private final int homeNumber;
    private Inventory inventory;

    public HomesMenuHolder(Type type, UUID targetId, int homeNumber) {
        this.type = type;
        this.targetId = targetId;
        this.homeNumber = homeNumber;
    }

    public Type getType() {
        return type;
    }

    /** Owner of the homes shown (HOMES / VIEW) or of the home being deleted (DELETE). */
    public UUID getTargetId() {
        return targetId;
    }

    /** Home slot being deleted (DELETE only). */
    public int getHomeNumber() {
        return homeNumber;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
