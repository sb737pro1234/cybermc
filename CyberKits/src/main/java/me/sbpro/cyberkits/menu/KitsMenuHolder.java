package me.sbpro.cyberkits.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import me.sbpro.cyberkits.CyberKits;
import me.sbpro.cyberkits.Kits;
import me.sbpro.cyberkits.Messages;
import me.sbpro.cyberkits.kit.Kit;

/**
 * The /kits menu. Each kit is a minecart showing its state:
 * chest minecart = claimable, minecart = claimed (cooldown), furnace minecart = locked (no permission).
 */
public final class KitsMenuHolder implements InventoryHolder {

    private final CyberKits plugin;
    private final Player player;
    private final Inventory inventory;

    public KitsMenuHolder(CyberKits plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.inventory = Bukkit.createInventory(this, Kits.MENU_SIZE, Messages.MENU_TITLE);
        render();
    }

    /** Redraws every kit icon (called every second while open, so cooldowns count down). */
    public void render() {
        for (Kit kit : Kits.KITS) {
            if (kit.slot() < 0 || kit.slot() >= inventory.getSize()) continue;
            inventory.setItem(kit.slot(), icon(kit));
        }
    }

    private ItemStack icon(Kit kit) {
        ItemStack item = switch (plugin.claimer().state(player, kit)) {
            case CLAIMABLE -> MenuItems.item(Material.CHEST_MINECART, Messages.kitName(kit), Messages.kitLoreClaimable(kit));
            case COOLDOWN -> MenuItems.item(Material.MINECART, Messages.kitName(kit),
                    Messages.kitLoreCooldown(kit, plugin.cooldowns().remaining(player.getUniqueId(), kit)));
            case LOCKED -> MenuItems.item(Material.FURNACE_MINECART, Messages.kitName(kit), Messages.kitLoreLocked(kit));
        };
        return item;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
