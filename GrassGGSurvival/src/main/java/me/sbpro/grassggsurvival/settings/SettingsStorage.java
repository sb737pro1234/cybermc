package me.sbpro.grassggsurvival.settings;

import me.sbpro.grassggsurvival.GrassGGSurvival;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Single shared copy of settings/data/settings.yml.
 *
 * Every settings manager used to load its own copy of this file and then
 * save the whole thing, which meant one manager could overwrite another
 * manager's changes. They now all share this one configuration object.
 */
public class SettingsStorage {

    private final File file;
    private final FileConfiguration config;

    public SettingsStorage(GrassGGSurvival plugin) {

        File folder = new File(plugin.getDataFolder(), "settings/data");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        file = new File(folder, "settings.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public File getFile() {
        return file;
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
