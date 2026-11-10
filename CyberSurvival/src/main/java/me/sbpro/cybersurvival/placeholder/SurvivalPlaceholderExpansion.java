package me.sbpro.cybersurvival.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.sbpro.cybersurvival.CyberSurvival;
import me.sbpro.cybersurvival.util.PlaytimeFormatter;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;

/** PlaceholderAPI expansion for %cybersurvival_...% placeholders. */
public final class SurvivalPlaceholderExpansion extends PlaceholderExpansion {

    private final CyberSurvival plugin;

    public SurvivalPlaceholderExpansion(CyberSurvival plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "cybersurvival";
    }

    @Override
    public String getAuthor() {
        return "Cyber";
    }

    @Override
    public String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {

        // %cybersurvival_playtime_formatted%
        if (params.equalsIgnoreCase("playtime_formatted")) {
            if (player == null) {
                return "0m";
            }
            // PLAY_ONE_MINUTE is actually total ticks played
            return PlaytimeFormatter.formatShort(player.getStatistic(Statistic.PLAY_ONE_MINUTE));
        }

        return null;
    }
}
