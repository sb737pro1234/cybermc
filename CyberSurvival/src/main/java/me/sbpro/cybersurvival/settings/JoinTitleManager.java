package me.sbpro.cybersurvival.settings;

import me.sbpro.cybersurvival.CyberSurvival;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/** Whether the "CyberMC / Welcome to CyberMC" title is shown on join. Enabled by default. */
public class JoinTitleManager {

    private final File file;
    private final FileConfiguration config;

    public JoinTitleManager(CyberSurvival plugin) {

        file = plugin.getSettingsStorage().getFile();
        config = plugin.getSettingsStorage().getConfig();
    }

    public boolean isJoinTitleEnabled(UUID uuid) {

        String path = "players." + uuid + ".jointitle";

        if (!config.contains(path)) {
            config.set(path, "enabled");
            save();
            return true;
        }

        return config.getString(path).equalsIgnoreCase("enabled");
    }

    public void toggleJoinTitle(UUID uuid) {

        config.set(
                "players." + uuid + ".jointitle",
                isJoinTitleEnabled(uuid) ? "disabled" : "enabled"
        );

        save();
    }

    private void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
