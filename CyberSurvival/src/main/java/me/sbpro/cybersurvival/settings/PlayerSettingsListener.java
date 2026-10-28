package me.sbpro.cybersurvival.settings;

import me.sbpro.cybersurvival.CyberSurvival;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerSettingsListener implements Listener {

    private final CyberSurvival plugin;

    public PlayerSettingsListener(CyberSurvival plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        if (plugin.getNightVisionManager().isNightVisionEnabled(player.getUniqueId())) {
            plugin.getNightVisionManager().enableNightVision(player);
        }

        if (plugin.getScoreboardManager().isScoreboardEnabled(player.getUniqueId())) {
            plugin.getScoreboardManager().enableScoreboard(player);
        } else {
            plugin.getScoreboardManager().disableScoreboard(player);
        }
        if (plugin.getGlowManager().isGlowEnabled(player.getUniqueId())) {
            plugin.getGlowManager().enableGlow(player);
        } else {
            plugin.getGlowManager().disableGlow(player);
        }

        plugin.getBossBarManager().applySavedState(player);
    }

}