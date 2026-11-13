package me.sbpro.cyberkits.kit;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/** Stores when each player can next claim each kit, in cooldowns.yml (players.&lt;uuid&gt;.&lt;kit id&gt;: epoch millis). */
public final class CooldownManager {

    private final Plugin plugin;
    private final File file;
    private final Map<UUID, Map<String, Long>> readyAt = new HashMap<>();

    public CooldownManager(Plugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "cooldowns.yml");
        load();
    }

    /** Milliseconds until the kit can be claimed again (0 if claimable). */
    public long remaining(UUID player, Kit kit) {
        Long time = readyAt.getOrDefault(player, Map.of()).get(kit.id());
        return time == null ? 0 : Math.max(0, time - System.currentTimeMillis());
    }

    /** Starts a full, fresh cooldown on the kit (or every kit if null). */
    public void start(UUID player, Kit kit, Iterable<Kit> all) {
        Map<String, Long> times = readyAt.computeIfAbsent(player, uuid -> new HashMap<>());
        long now = System.currentTimeMillis();

        if (kit != null) {
            times.put(kit.id(), now + kit.cooldown().toMillis());
        } else {
            for (Kit each : all) times.put(each.id(), now + each.cooldown().toMillis());
        }
        save();
    }

    /** Clears the cooldown on the kit (or every kit if null). */
    public void reset(UUID player, Kit kit) {
        Map<String, Long> times = readyAt.get(player);
        if (times == null) return;

        if (kit != null) times.remove(kit.id());
        else times.clear();

        if (times.isEmpty()) readyAt.remove(player);
        save();
    }

    public void load() {
        readyAt.clear();
        if (!file.exists()) return;

        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection players = yaml.getConfigurationSection("players");
        if (players == null) return;

        for (String raw : players.getKeys(false)) {
            UUID uuid;
            try {
                uuid = UUID.fromString(raw);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Skipping invalid UUID in cooldowns.yml: " + raw);
                continue;
            }

            ConfigurationSection section = players.getConfigurationSection(raw);
            if (section == null) continue;

            Map<String, Long> times = new HashMap<>();
            for (String kit : section.getKeys(false)) times.put(kit, section.getLong(kit));
            readyAt.put(uuid, times);
        }
    }

    public void save() {
        YamlConfiguration yaml = new YamlConfiguration();
        long now = System.currentTimeMillis();

        readyAt.forEach((uuid, times) -> times.forEach((kit, time) -> {
            // Expired cooldowns don't need storing
            if (time > now) yaml.set("players." + uuid + "." + kit, time);
        }));

        try {
            plugin.getDataFolder().mkdirs();
            yaml.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save cooldowns.yml: " + e.getMessage());
        }
    }
}
