package me.sbpro.cyberhub.items.visibility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/** Stores who has player visibility disabled, in visibility.yml. */
public final class VisibilityManager {

    private static final String PATH = "hidden-players";

    private final Plugin plugin;
    private final File file;
    private final Set<UUID> hiding = new HashSet<>();

    public VisibilityManager(Plugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "visibility.yml");
        load();
    }

    public boolean isHiding(Player player) {
        return hiding.contains(player.getUniqueId());
    }

    public Set<UUID> hiding() {
        return Set.copyOf(hiding);
    }

    public void setHiding(Player player, boolean hide) {
        boolean changed = hide ? hiding.add(player.getUniqueId()) : hiding.remove(player.getUniqueId());
        if (changed) save();
    }

    private void load() {
        hiding.clear();
        if (!file.exists()) return;

        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        for (String raw : yaml.getStringList(PATH)) {
            try {
                hiding.add(UUID.fromString(raw));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Skipping invalid UUID in visibility.yml: " + raw);
            }
        }
    }

    public void save() {
        YamlConfiguration yaml = new YamlConfiguration();
        List<String> uuids = new ArrayList<>();
        for (UUID uuid : hiding) uuids.add(uuid.toString());
        yaml.set(PATH, uuids);

        try {
            plugin.getDataFolder().mkdirs();
            yaml.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save visibility.yml: " + e.getMessage());
        }
    }
}
