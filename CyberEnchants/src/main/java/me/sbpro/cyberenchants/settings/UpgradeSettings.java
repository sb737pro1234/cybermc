package me.sbpro.cyberenchants.settings;

import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;

import java.util.Set;

/** Behavioural settings of the Enchant Upgrade item (not user-facing text). */
public final class UpgradeSettings {

    private UpgradeSettings() {
    }

    public static final Sound SUCCESS_SOUND = Sound.ENTITY_PLAYER_LEVELUP;
    public static final Sound ERROR_SOUND = Sound.ENTITY_VILLAGER_NO;
    public static final float SOUND_VOLUME = 1.0f;
    public static final float SUCCESS_PITCH = 1.2f;
    public static final float ERROR_PITCH = 1.0f;

    /*
     * Configure which vanilla enchantments are allowed to exceed their
     * normal maximum level.
     *
     * An enchantment is only eligible when:
     * 1. It is in this list.
     * 2. Its vanilla maximum level is 2 or higher.
     * 3. The item currently has it at its vanilla maximum level.
     *
     * Enchantments such as Mending, Infinity and Silk Touch are intentionally
     * not included because increasing them beyond their vanilla level would
     * not make sense for this item.
     */
    public static final Set<Enchantment> UPGRADEABLE_ENCHANTMENTS = Set.of(
            Enchantment.SHARPNESS,
            Enchantment.SMITE,
            Enchantment.BANE_OF_ARTHROPODS,
            Enchantment.KNOCKBACK,
            Enchantment.FIRE_ASPECT,
            Enchantment.LOOTING,
            Enchantment.EFFICIENCY,
            Enchantment.UNBREAKING,
            Enchantment.FORTUNE,
            Enchantment.POWER,
            Enchantment.PUNCH,
            Enchantment.FLAME,
            Enchantment.PROTECTION,
            Enchantment.FIRE_PROTECTION,
            Enchantment.FEATHER_FALLING,
            Enchantment.BLAST_PROTECTION,
            Enchantment.PROJECTILE_PROTECTION,
            Enchantment.RESPIRATION,
            Enchantment.AQUA_AFFINITY,
            Enchantment.DEPTH_STRIDER,
            Enchantment.THORNS,
            Enchantment.FROST_WALKER,
            Enchantment.SOUL_SPEED,
            Enchantment.SWIFT_SNEAK,
            Enchantment.LURE,
            Enchantment.LUCK_OF_THE_SEA,
            Enchantment.IMPALING,
            Enchantment.RIPTIDE,
            Enchantment.LOYALTY,
            Enchantment.CHANNELING,
            Enchantment.MULTISHOT,
            Enchantment.QUICK_CHARGE,
            Enchantment.PIERCING,
            Enchantment.WIND_BURST,
            Enchantment.BREACH,
            Enchantment.DENSITY,
            Enchantment.SWEEPING_EDGE,
            Enchantment.LUNGE
    );
}
