package me.sbpro.cybersurvival.settings;

import me.sbpro.cybersurvival.CyberSurvival;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class BossBarManager {

    private static final String TITLE = "ɢʀᴀѕѕɢɢ.ᴍʏ.ᴘᴇʙʙʟᴇ.ʜᴏѕᴛ";

    private final CyberSurvival plugin;

    private final File file;
    private final FileConfiguration config;

    // One bossbar instance shared by every viewer.
    private final BossBar bossBar = BossBar.bossBar(
            Component.text(TITLE),
            1.0f,
            BossBar.Color.GREEN,
            BossBar.Overlay.PROGRESS
    );

    public BossBarManager(CyberSurvival plugin) {
        this.plugin = plugin;

        file = plugin.getSettingsStorage().getFile();
        config = plugin.getSettingsStorage().getConfig();
    }

    // Enabled by default for everyone.
    public boolean isBossBarEnabled(UUID uuid) {

        String path = "players." + uuid + ".bossbar";

        if (!config.contains(path)) {
            config.set(path, "enabled");
            save();
            return true;
        }

        return config.getString(path).equalsIgnoreCase("enabled");
    }

    public void enableBossBar(Player player) {

        config.set("players." + player.getUniqueId() + ".bossbar", "enabled");

        player.showBossBar(bossBar);

        save();
    }

    public void disableBossBar(Player player) {

        config.set("players." + player.getUniqueId() + ".bossbar", "disabled");

        player.hideBossBar(bossBar);

        save();
    }

    public void toggleBossBar(Player player) {

        if (isBossBarEnabled(player.getUniqueId())) {
            disableBossBar(player);
        } else {
            enableBossBar(player);
        }

    }

    // Shows or hides the bar according to the saved preference (used on join).
    public void applySavedState(Player player) {

        if (isBossBarEnabled(player.getUniqueId())) {
            player.showBossBar(bossBar);
        } else {
            player.hideBossBar(bossBar);
        }
    }

    // Removes the bar from everyone (used on plugin disable).
    public void hideAll() {

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.hideBossBar(bossBar);
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
