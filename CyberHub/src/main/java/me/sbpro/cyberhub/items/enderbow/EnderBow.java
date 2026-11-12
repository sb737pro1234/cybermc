package me.sbpro.cyberhub.items.enderbow;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;

import net.kyori.adventure.key.Key;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import me.sbpro.cyberhub.Messages;

/**
 * The Ender Bow is not a real bow: real bows can't be drawn without an arrow.
 * It's a stick that looks like a bow (item model) and is "consumable" with the
 * bow-draw animation, so it can be held to charge and released to fire with no ammo.
 */
public final class EnderBow {

    private EnderBow() {}

    /** Hotbar slot 5. */
    public static final int SLOT = 4;
    /** 3 second cooldown after firing. */
    public static final int COOLDOWN_TICKS = 60;

    public static NamespacedKey key(Plugin plugin) {
        return new NamespacedKey(plugin, "ender_bow");
    }

    public static ItemStack create(Plugin plugin) {
        ItemStack bow = new ItemStack(Material.STICK);

        bow.editMeta(meta -> {
            meta.displayName(Messages.ENDER_BOW_NAME);
            meta.lore(Messages.ENDER_BOW_LORE);
            meta.setItemModel(NamespacedKey.minecraft("bow"));
            meta.setMaxStackSize(1);
            meta.setEnchantmentGlintOverride(true);
            meta.getPersistentDataContainer().set(key(plugin), PersistentDataType.BOOLEAN, true);
        });

        // Long enough that it's never actually "consumed"; silent, no particles
        bow.setData(DataComponentTypes.CONSUMABLE, Consumable.consumable()
                .consumeSeconds(3600F)
                .animation(ItemUseAnimation.BOW)
                .sound(Key.key("minecraft", "intentionally_empty"))
                .hasConsumeParticles(false)
                .build());

        return bow;
    }

    public static boolean isEnderBow(Plugin plugin, ItemStack item) {
        if (item == null || item.getType() != Material.STICK || !item.hasItemMeta()) {
            return false;
        }

        return item.getItemMeta().getPersistentDataContainer().has(key(plugin), PersistentDataType.BOOLEAN);
    }
}
