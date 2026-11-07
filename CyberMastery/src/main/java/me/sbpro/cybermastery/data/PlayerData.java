package me.sbpro.cybermastery.data;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * One player's mastery progress.
 *
 * Progress is stored per type id in units (quarter-points, 4 units = 1 point).
 * Claimed rewards are stored per type id as a bit mask (bit 0 = level 1 ... bit 44 = level 45).
 *
 * Only touched on the main thread; {@link #copy()} makes a snapshot for async saving.
 */
public final class PlayerData {

    private final UUID uuid;
    private String name;
    private final Map<String, Long> progress = new HashMap<>();
    private final Map<String, Long> claimed = new HashMap<>();
    private boolean dirty;

    public PlayerData(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public UUID uuid() {
        return uuid;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.equals(this.name)) {
            this.name = name;
            dirty = true;
        }
    }

    // =============================================================
    // Progress
    // =============================================================

    public long units(String typeId) {
        return progress.getOrDefault(typeId, 0L);
    }

    public void addUnits(String typeId, long amount) {
        if (amount <= 0) {
            return;
        }
        progress.merge(typeId, amount, (a, b) -> {
            long sum = a + b;
            return sum < 0 ? Long.MAX_VALUE : sum; // overflow guard
        });
        dirty = true;
    }

    /** Admin: sets a type's progress exactly. */
    public void setUnits(String typeId, long units) {
        if (units <= 0) {
            progress.remove(typeId);
        } else {
            progress.put(typeId, units);
        }
        dirty = true;
    }

    /** Admin: removes a type's progress and claimed rewards. */
    public void reset(String typeId) {
        progress.remove(typeId);
        claimed.remove(typeId);
        dirty = true;
    }

    /** Admin: removes all progress and claimed rewards. */
    public void resetAll() {
        progress.clear();
        claimed.clear();
        dirty = true;
    }

    // =============================================================
    // Claimed rewards
    // =============================================================

    /** Admin: forgets claims above a level (used when a level is set lower). */
    public void clearClaimedAbove(String typeId, int level) {
        Long mask = claimed.get(typeId);
        if (mask == null) {
            return;
        }
        long kept = level <= 0 ? 0L : mask & ((1L << Math.min(level, 63)) - 1);
        if (kept == 0) {
            claimed.remove(typeId);
        } else {
            claimed.put(typeId, kept);
        }
        dirty = true;
    }

    public boolean isClaimed(String typeId, int level) {
        return (claimed.getOrDefault(typeId, 0L) & bit(level)) != 0;
    }

    public void setClaimed(String typeId, int level) {
        claimed.merge(typeId, bit(level), (a, b) -> a | b);
        dirty = true;
    }

    private static long bit(int level) {
        return 1L << (level - 1);
    }

    // =============================================================
    // Saving
    // =============================================================

    public boolean isDirty() {
        return dirty;
    }

    public void markClean() {
        dirty = false;
    }

    /** A snapshot that can safely be written on another thread. */
    public PlayerData copy() {
        PlayerData copy = new PlayerData(uuid, name);
        copy.progress.putAll(progress);
        copy.claimed.putAll(claimed);
        return copy;
    }

    public YamlConfiguration toYaml() {
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set("name", name);
        progress.forEach((id, units) -> yaml.set("progress." + id, units));
        claimed.forEach((id, mask) -> yaml.set("claimed." + id, mask));
        return yaml;
    }

    public static PlayerData fromYaml(UUID uuid, String fallbackName, YamlConfiguration yaml) {
        PlayerData data = new PlayerData(uuid, yaml.getString("name", fallbackName));

        ConfigurationSection progressSection = yaml.getConfigurationSection("progress");
        if (progressSection != null) {
            for (String id : progressSection.getKeys(false)) {
                data.progress.put(id, progressSection.getLong(id));
            }
        }

        ConfigurationSection claimedSection = yaml.getConfigurationSection("claimed");
        if (claimedSection != null) {
            for (String id : claimedSection.getKeys(false)) {
                data.claimed.put(id, claimedSection.getLong(id));
            }
        }

        if (fallbackName != null) {
            data.setName(fallbackName);
        }

        return data;
    }
}
