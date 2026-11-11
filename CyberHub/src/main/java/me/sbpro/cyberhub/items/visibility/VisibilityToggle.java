package me.sbpro.cyberhub.items.visibility;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.UseCooldown;

import net.kyori.adventure.key.Key;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import me.sbpro.cyberhub.Messages;

public final class VisibilityToggle {

    private VisibilityToggle() {}

    /** Hotbar slot 8. */
    public static final int SLOT = 7;
    /** 3 second cooldown between toggles. */
    public static final int COOLDOWN_TICKS = 60;

    public static NamespacedKey key(Plugin plugin) {
        return new NamespacedKey(plugin, "visibility_toggle");
    }

    /** Green dye while players are visible, grey dye while hidden. */
    public static ItemStack create(Plugin plugin, boolean visible) {
        ItemStack item = new ItemStack(visible ? Material.LIME_DYE : Material.GRAY_DYE);

        item.editMeta(meta -> {
            meta.displayName(visible ? Messages.VISIBILITY_ON_NAME : Messages.VISIBILITY_OFF_NAME);
            meta.lore(Messages.VISIBILITY_LORE);
            meta.getPersistentDataContainer().set(key(plugin), PersistentDataType.BOOLEAN, true);
        });

        // Shared cooldown group so the cooldown carries over when the dye swaps colour
        item.setData(DataComponentTypes.USE_COOLDOWN, UseCooldown.useCooldown(COOLDOWN_TICKS / 20F)
                .cooldownGroup(Key.key("cyberhub", "visibility_toggle"))
                .build());

        return item;
    }

    public static boolean isToggle(Plugin plugin, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;

        return item.getItemMeta().getPersistentDataContainer().has(key(plugin), PersistentDataType.BOOLEAN);
    }
}
