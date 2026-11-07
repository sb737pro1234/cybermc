package me.sbpro.cybermastery.data;

import me.sbpro.cybermastery.CyberMastery;
import me.sbpro.cybermastery.model.Category;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * plugins/CyberMastery/totals.yml: each player's total levels and levels per category,
 * so the placeholders work for offline players without reading their data file.
 */
public final class TotalsCache {

    /** A player's cached levels. categoryLevels is indexed by Category.ordinal(). */
    public record Totals(String name, long total, long[] categoryLevels) {

        public long category(Category category) {
            return categoryLevels[category.ordinal()];
        }
    }

    private final CyberMastery plugin;
    private final File file;
    private final Map<UUID, Totals> totals = new ConcurrentHashMap<>();
    private volatile boolean dirty;

    public TotalsCache(CyberMastery plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "totals.yml");
        load();
    }

    public Totals get(UUID uuid) {
        return totals.get(uuid);
    }

    public void put(UUID uuid, Totals value) {
        Totals previous = totals.put(uuid, value);
        if (previous == null
                || previous.total() != value.total()
                || !Arrays.equals(previous.categoryLevels(), value.categoryLevels())
                || !Objects.equals(previous.name(), value.name())) {
            dirty = true;
        }
    }

    // =============================================================
    // Leaderboard (/mastery top)
    // =============================================================

    /** Players with at least one level, most total levels first, then by name. */
    public List<TopMasteryEntry> getTop(int limit) {
        if (limit <= 0) {
            return List.of();
        }

        List<TopMasteryEntry> entries = new ArrayList<>();

        for (Map.Entry<UUID, Totals> entry : totals.entrySet()) {
            long levels = entry.getValue().total();

            // Only players who have levelled something are shown.
            if (levels <= 0) {
                continue;
            }

            String playerName = entry.getValue().name();

            if (playerName == null || playerName.isBlank()) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(entry.getKey());
                playerName = offlinePlayer.getName();
            }

            if (playerName == null || playerName.isBlank()) {
                playerName = "Unknown Player";
            }

            entries.add(new TopMasteryEntry(entry.getKey(), playerName, levels));
        }

        entries.sort(
                Comparator.comparingLong(TopMasteryEntry::levels)
                        .reversed()
                        .thenComparing(
                                TopMasteryEntry::playerName,
                                String.CASE_INSENSITIVE_ORDER
                        )
        );

        if (entries.size() > limit) {
            return new ArrayList<>(entries.subList(0, limit));
        }

        return entries;
    }

    /**
     * Returns the player's position across the full leaderboard.
     * Returns 0 when the player has no levels / is not ranked.
     */
    public int getPosition(UUID uuid) {
        List<TopMasteryEntry> entries = getTop(Integer.MAX_VALUE);

        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).uuid().equals(uuid)) {
                return i + 1;
            }
        }

        return 0;
    }

    private void load() {
        if (!file.exists()) {
            return;
        }

        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

        for (String key : yaml.getKeys(false)) {
            ConfigurationSection section = yaml.getConfigurationSection(key);
            if (section == null) {
                continue;
            }

            UUID uuid;
            try {
                uuid = UUID.fromString(key);
            } catch (IllegalArgumentException exception) {
                continue;
            }

            long[] levels = new long[Category.values().length];
            for (Category category : Category.values()) {
                levels[category.ordinal()] = section.getLong(category.key());
            }

            totals.put(uuid, new Totals(
                    section.getString("name"),
                    section.getLong("levels"),
                    levels
            ));
        }
    }

    /** Snapshot on the main thread, write on the IO thread. */
    public void saveAsync(DataManager dataManager) {
        if (!dirty) {
            return;
        }
        dirty = false;

        Map<UUID, Totals> snapshot = new HashMap<>(totals);
        dataManager.io().execute(() -> write(snapshot));
    }

    private void write(Map<UUID, Totals> snapshot) {
        YamlConfiguration yaml = new YamlConfiguration();

        snapshot.forEach((uuid, value) -> {
            String path = uuid.toString();
            yaml.set(path + ".name", value.name());
            yaml.set(path + ".levels", value.total());
            for (Category category : Category.values()) {
                yaml.set(path + "." + category.key(), value.category(category));
            }
        });

        File temp = new File(file.getParentFile(), "totals.yml.tmp");

        try {
            Files.writeString(temp.toPath(), yaml.saveToString(), StandardCharsets.UTF_8);
            Files.move(temp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException exception) {
            plugin.getLogger().log(Level.SEVERE, "Could not save totals.yml", exception);
        }
    }
}
