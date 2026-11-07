package me.sbpro.cybermastery.model;

import org.bukkit.Material;

/**
 * One levelable type, e.g. Stone, Zombie or Wheat.
 *
 * @param id       unique id across all categories (used in data files and placeholders)
 * @param category the category it belongs to
 * @param group    the Blocks group it belongs to, or null outside Blocks
 * @param name     display name
 * @param icon     menu icon
 * @param points   whole points one action gives
 * @param includes what the type includes, shown in its lore
 */
public record MasteryType(
        String id,
        Category category,
        Group group,
        String name,
        Material icon,
        long points,
        String includes
) {

    /** Stored units one normal action (mine, kill, harvest, catch) gives. */
    public long actionUnits() {
        return points * LevelCurve.UNITS_PER_POINT;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof MasteryType type && type.id.equals(id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "MasteryType[" + id + "]";
    }
}
