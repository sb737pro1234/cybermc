package me.sbpro.cyberhub.items;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import me.sbpro.cyberhub.items.enderbow.EnderBow;
import me.sbpro.cyberhub.items.visibility.VisibilityToggle;
import me.sbpro.cyberhub.items.selector.ServerSelector;

public final class HubItems {

    private HubItems() {}

    /** True for the server selector, visibility toggle and Ender Bow. */
    public static boolean isHubItem(Plugin plugin, ItemStack item) {
        return ServerSelector.isSelector(plugin, item)
                || VisibilityToggle.isToggle(plugin, item)
                || EnderBow.isEnderBow(plugin, item);
    }
}
