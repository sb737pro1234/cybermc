package me.sbpro.cybercoins.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.sbpro.cybercoins.CyberCoins;
import me.sbpro.cybercoins.util.AmountFormatter;
import org.bukkit.OfflinePlayer;

public final class CoinsPlaceholderExpansion extends PlaceholderExpansion {

    private final CyberCoins plugin;

    public CoinsPlaceholderExpansion(CyberCoins plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "cybercoins";
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
        if (player == null) {
            return "0";
        }

        if (params.equalsIgnoreCase("balance") || params.equalsIgnoreCase("coins")) {
            return AmountFormatter.format(plugin.getCoinManager().getCoins(player.getUniqueId()));
        }

        return null;
    }
}
