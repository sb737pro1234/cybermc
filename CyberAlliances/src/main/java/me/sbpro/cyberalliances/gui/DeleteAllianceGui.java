package me.sbpro.cyberalliances.gui;

import me.sbpro.cyberalliances.CyberAlliances;
import me.sbpro.cyberalliances.Messages;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class DeleteAllianceGui {
    private DeleteAllianceGui() {
    }

    public static void open(CyberAlliances plugin, Player player, String allianceName) {
        Inventory inventory = Bukkit.createInventory((InventoryHolder)new DeleteGuiHolder(allianceName), (int)27, Messages.DELETE_TITLE);
        inventory.setItem(10, DeleteAllianceGui.createButton(Material.RED_STAINED_GLASS_PANE, Messages.DELETE_CANCEL_BUTTON));
        inventory.setItem(13, DeleteAllianceGui.createButton(Material.GRASS_BLOCK, Messages.deleteAllianceName(allianceName)));
        inventory.setItem(16, DeleteAllianceGui.createButton(Material.GREEN_STAINED_GLASS_PANE, Messages.DELETE_CONFIRM_BUTTON));
        player.openInventory(inventory);
    }

    private static ItemStack createButton(Material material, Component name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(name);
        item.setItemMeta(meta);
        return item;
    }
}

