package me.sbpro.cybermastery;

import org.bukkit.configuration.file.FileConfiguration;

/** Values from config.yml. */
public record Settings(
        boolean ignoreCreative,
        int autosaveMinutes,
        boolean broadcastMaxLevel,
        boolean placementEnabled,
        int placementPercent,
        long hourlyCap
) {

    public static Settings from(FileConfiguration config) {
        return new Settings(
                config.getBoolean("ignore-creative", true),
                Math.max(1, config.getInt("autosave-minutes", 5)),
                config.getBoolean("broadcast-max-level", true),
                config.getBoolean("placement.enabled", true),
                Math.max(0, Math.min(100, config.getInt("placement.percent", 25))),
                Math.max(0, config.getLong("placement.hourly-cap", 100))
        );
    }
}
