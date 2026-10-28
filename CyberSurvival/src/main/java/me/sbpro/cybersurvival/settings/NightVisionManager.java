package me.sbpro.cybersurvival.settings;

import me.sbpro.cybersurvival.CyberSurvival;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class NightVisionManager {

    private final CyberSurvival plugin;

    private final File file;
    private final FileConfiguration config;

    public NightVisionManager(CyberSurvival plugin) {
        this.plugin = plugin;

        file = plugin.getSettingsStorage().getFile();
        config = plugin.getSettingsStorage().getConfig();
    }

    public boolean isNightVisionEnabled(UUID uuid) {

        String path = "players." + uuid + ".nightvision";

        if (!config.contains(path)) {
            config.set(path, "disabled");
            save();
            return false;
        }

        return config.getString(path).equalsIgnoreCase("enabled");
    }

    public void enableNightVision(Player player) {
        config.set("players." + player.getUniqueId() + ".nightvision", "enabled");

        player.addPotionEffect(new PotionEffect(
                PotionEffectType.NIGHT_VISION,
                PotionEffect.INFINITE_DURATION,
                0,
                false,
                false
        ));

        save();
    }

    public void disableNightVision(Player player) {
        config.set("players." + player.getUniqueId() + ".nightvision", "disabled");

        player.removePotionEffect(PotionEffectType.NIGHT_VISION);

        save();
    }

    public void toggleNightVision(Player player) {

        if (isNightVisionEnabled(player.getUniqueId())) {
            disableNightVision(player);
        } else {
            enableNightVision(player);
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