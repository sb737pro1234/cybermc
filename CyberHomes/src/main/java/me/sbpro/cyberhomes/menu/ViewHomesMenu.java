package me.sbpro.cyberhomes.menu;

import me.sbpro.cyberhomes.Messages;
import me.sbpro.cyberhomes.CyberHomes;
import me.sbpro.cyberhomes.model.Home;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

/** Read-only view of another player's homes (staff). */
public final class ViewHomesMenu {

    public static final int[] SLOTS = {10, 11, 12, 13, 14, 15, 16};

    private final Inventory inventory;

    public ViewHomesMenu(CyberHomes plugin, UUID targetId, String targetName) {
        HomesMenuHolder holder = new HomesMenuHolder(HomesMenuHolder.Type.VIEW, targetId, 0);
        this.inventory = Bukkit.createInventory(holder, 27, Messages.viewTitle(targetName));
        holder.setInventory(inventory);

        for (int number = 1; number <= 7; number++) {
            Home home = plugin.getHomesManager().getHome(targetId, number);
            inventory.setItem(SLOTS[number - 1], home == null
                    ? MenuItems.create(Material.GRAY_BED, Messages.emptyHomeName(number), Messages.NOT_SET_LORE)
                    : MenuItems.create(Material.GREEN_BED, Messages.homeName(home.getName()), Messages.TELEPORT_LORE));
        }
    }

    public void open(Player viewer) {
        viewer.openInventory(inventory);
    }
}
