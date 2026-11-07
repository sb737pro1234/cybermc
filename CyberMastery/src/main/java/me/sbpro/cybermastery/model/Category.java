package me.sbpro.cybermastery.model;

import org.bukkit.Material;

/**
 * The five mastery categories, in the order they appear in /mastery (slots 20 to 24).
 * Display names and explanations live in Messages.
 */
public enum Category {

    BLOCKS("blocks", Material.GRASS_BLOCK, 20),
    SLAYING("slaying", Material.NETHERITE_SWORD, 21),
    FARMING("farming", Material.NETHERITE_HOE, 22),
    FORAGING("foraging", Material.NETHERITE_AXE, 23),
    FISHING("fishing", Material.FISHING_ROD, 24);

    private final String key;
    private final Material icon;
    private final int menuSlot;

    Category(String key, Material icon, int menuSlot) {
        this.key = key;
        this.icon = icon;
        this.menuSlot = menuSlot;
    }

    public String key() {
        return key;
    }

    public Material icon() {
        return icon;
    }

    public int menuSlot() {
        return menuSlot;
    }

    /** True for categories whose types are matched by block material. */
    public boolean isBlockBased() {
        return this == BLOCKS || this == FARMING || this == FORAGING;
    }

    public static Category fromKey(String key) {
        for (Category category : values()) {
            if (category.key.equalsIgnoreCase(key)) {
                return category;
            }
        }
        return null;
    }

    public static Category fromMenuSlot(int slot) {
        for (Category category : values()) {
            if (category.menuSlot == slot) {
                return category;
            }
        }
        return null;
    }
}
