package me.sbpro.cyberhomes.menu;

import me.sbpro.cyberhomes.Messages;
import me.sbpro.cyberhomes.CyberHomes;
import me.sbpro.cyberhomes.model.Home;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class HomesMenu {

    public static final int[] BED_SLOTS = {10, 11, 12, 13, 14, 15, 16};
    public static final int[] BUTTON_SLOTS = {19, 20, 21, 22, 23, 24, 25};

    private final CyberHomes plugin;
    private final Player viewer;
    private final Player target;
    private Inventory inventory;

    public HomesMenu(CyberHomes plugin, Player viewer, Player target) {
        this.plugin = plugin;
        this.viewer = viewer;
        this.target = target;
        create();
    }

    private void create() {
        Component title = viewer.equals(target)
                ? Messages.HOMES_TITLE
                : Messages.homesTitleOther(target.getName());

        HomesMenuHolder holder = new HomesMenuHolder(HomesMenuHolder.Type.HOMES, target.getUniqueId(), 0);
        inventory = Bukkit.createInventory(holder, 36, title);
        holder.setInventory(inventory);

        for (int i = 0; i < 7; i++) {
            int homeNumber = i + 1;
            inventory.setItem(BED_SLOTS[i], createBed(homeNumber));
            inventory.setItem(BUTTON_SLOTS[i], createButton(homeNumber));
        }
    }

    private ItemStack createBed(int number) {
        boolean hasPermission = getHomeLimit(target) >= number;
        Home home = plugin.getHomesManager().getHome(target.getUniqueId(), number);

        if (!hasPermission) {
            return MenuItems.create(Material.RED_BED, Messages.noAccessName(number), Messages.NO_ACCESS_LORE);
        }
        if (home != null) {
            return MenuItems.create(Material.GREEN_BED, Messages.homeName(getHomeName(home, number)), Messages.TELEPORT_LORE);
        }
        return MenuItems.create(Material.GRAY_BED, Messages.emptyHomeName(number), Messages.NOT_SET_LORE);
    }

    private ItemStack createButton(int number) {
        boolean hasPermission = getHomeLimit(target) >= number;
        Home home = plugin.getHomesManager().getHome(target.getUniqueId(), number);

        if (!hasPermission) {
            return MenuItems.create(Material.RED_DYE, Messages.NO_PERMISSION_BUTTON, Messages.SLOT_LOCKED_LORE);
        }
        if (home != null) {
            return MenuItems.create(Material.GREEN_DYE, Messages.manageName(getHomeName(home, number)), Messages.MANAGE_LORE);
        }
        return MenuItems.create(Material.GRAY_DYE, Messages.setHomeName(number), Messages.CREATE_LORE);
    }

    private String getHomeName(Home home, int number) {
        if (home == null || home.getName() == null || home.getName().isEmpty()) {
            return "Home " + number;
        }
        return home.getName();
    }

    private int getHomeLimit(Player player) {
        for (int i = 7; i >= 1; i--) {
            if (player.hasPermission("cyber.homes." + i)) {
                return i;
            }
        }
        return 0;
    }

    public void open() {
        viewer.openInventory(inventory);
    }
}
