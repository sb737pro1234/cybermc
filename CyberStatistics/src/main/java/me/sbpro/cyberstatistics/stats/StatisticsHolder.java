package me.sbpro.cyberstatistics.stats;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Marks an inventory as a statistics menu and stores what each clickable slot does.
 */
public final class StatisticsHolder implements InventoryHolder {

    private final Map<Integer, Consumer<Player>> actions = new HashMap<>();
    private Inventory inventory;

    void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    void onClick(int slot, Consumer<Player> action) {
        actions.put(slot, action);
    }

    @Nullable Consumer<Player> getAction(int slot) {
        return actions.get(slot);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
