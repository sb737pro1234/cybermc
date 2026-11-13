package me.sbpro.cyberkits.menu;

import java.util.List;

import net.kyori.adventure.text.Component;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/** Shared menu item builders. */
final class MenuItems {

    private MenuItems() {}

    static ItemStack item(Material material, Component name, List<Component> lore) {
        ItemStack item = new ItemStack(material);
        item.editMeta(meta -> {
            meta.displayName(name);
            if (!lore.isEmpty()) meta.lore(lore);
        });
        return item;
    }

    static ItemStack filler() {
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        item.editMeta(meta -> meta.setHideTooltip(true));
        return item;
    }
}
