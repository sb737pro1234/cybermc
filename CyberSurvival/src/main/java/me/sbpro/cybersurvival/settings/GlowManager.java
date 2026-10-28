package me.sbpro.cybersurvival.settings;

import me.sbpro.cybersurvival.CyberSurvival;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class GlowManager {

    private final CyberSurvival plugin;

    private final File file;
    private final FileConfiguration config;

    public GlowManager(CyberSurvival plugin) {
        this.plugin = plugin;

        file = plugin.getSettingsStorage().getFile();
        config = plugin.getSettingsStorage().getConfig();
    }

    public boolean isGlowEnabled(UUID uuid) {

        String path = "players." + uuid + ".glow";

        if (!config.contains(path)) {
            config.set(path, "disabled");
            save();
            return false;
        }

        return config.getString(path).equalsIgnoreCase("enabled");
    }

    public void enableGlow(Player player) {

        config.set("players." + player.getUniqueId() + ".glow", "enabled");

        player.setGlowing(true);

        save();
    }

    public void disableGlow(Player player) {

        config.set("players." + player.getUniqueId() + ".glow", "disabled");

        player.setGlowing(false);

        save();
    }

    public void toggleGlow(Player player) {

        if (isGlowEnabled(player.getUniqueId())) {
            disableGlow(player);
        } else {
            enableGlow(player);
        }

    }

    private void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}