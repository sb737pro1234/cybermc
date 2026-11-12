package me.sbpro.cyberhub.items.selector;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import me.sbpro.cyberhub.Messages;

/** The compass that opens the server selector menu. */
public final class ServerSelector {

    private ServerSelector() {}

    /** Hotbar slot 1. */
    public static final int SLOT = 0;

    public static NamespacedKey key(Plugin plugin) {
        return new NamespacedKey(plugin, "server_selector");
    }

    public static ItemStack create(Plugin plugin) {
        ItemStack item = new ItemStack(Material.COMPASS);

        item.editMeta(meta -> {
            meta.displayName(Messages.SELECTOR_ITEM_NAME);
            meta.lore(Messages.SELECTOR_ITEM_LORE);
            meta.getPersistentDataContainer().set(key(plugin), PersistentDataType.BOOLEAN, true);
        });

        return item;
    }

    public static boolean isSelector(Plugin plugin, ItemStack item) {
        if (item == null || item.getType() != Material.COMPASS || !item.hasItemMeta()) return false;

        return item.getItemMeta().getPersistentDataContainer().has(key(plugin), PersistentDataType.BOOLEAN);
    }
}
