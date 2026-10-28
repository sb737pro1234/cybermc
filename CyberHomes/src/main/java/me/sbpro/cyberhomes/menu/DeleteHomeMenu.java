package me.sbpro.cyberhomes.menu;

import me.sbpro.cyberhomes.Messages;
import me.sbpro.cyberhomes.CyberHomes;
import me.sbpro.cyberhomes.model.Home;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class DeleteHomeMenu {

    private final CyberHomes plugin;
    private final Player player;
    private final int homeNumber;
    private Inventory inventory;

    public DeleteHomeMenu(CyberHomes plugin, Player player, int homeNumber) {
        this.plugin = plugin;
        this.player = player;
        this.homeNumber = homeNumber;
        create();
    }

    private void create() {
        HomesMenuHolder holder = new HomesMenuHolder(HomesMenuHolder.Type.DELETE, player.getUniqueId(), homeNumber);
        inventory = Bukkit.createInventory(holder, 27, Messages.deleteTitle(homeNumber));
        holder.setInventory(inventory);

        Home home = plugin.getHomesManager().getHome(player.getUniqueId(), homeNumber);
        String homeName = home == null ? "Home " + homeNumber : home.getName();
        if (homeName == null || homeName.isBlank()) {
            homeName = "Home " + homeNumber;
        }

        inventory.setItem(10, MenuItems.create(Material.RED_STAINED_GLASS_PANE, Messages.CANCEL_BUTTON, Messages.CANCEL_LORE));
        inventory.setItem(13, MenuItems.create(Material.GREEN_BED, Messages.homeName(homeName), Messages.DELETE_PREVIEW_LORE));
        inventory.setItem(16, MenuItems.create(Material.GREEN_STAINED_GLASS_PANE, Messages.CONFIRM_BUTTON, Messages.CONFIRM_LORE));
    }

    public void open() {
        player.openInventory(inventory);
    }
}
