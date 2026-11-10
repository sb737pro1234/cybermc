
package me.sbpro.cyberalliances.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.sbpro.cyberalliances.CyberAlliances;
import me.sbpro.cyberalliances.data.AllianceStorage;
import me.sbpro.cyberalliances.model.Alliance;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public final class AlliancePlaceholderExpansion
extends PlaceholderExpansion {
    private final CyberAlliances plugin;
    private final AllianceStorage allianceStorage;

    public AlliancePlaceholderExpansion(CyberAlliances plugin, AllianceStorage allianceStorage) {
        this.plugin = plugin;
        this.allianceStorage = allianceStorage;
    }

    @NotNull
    public String getIdentifier() {
        return "cyberalliances";
    }

    @NotNull
    public String getAuthor() {
        return "Cyber";
    }

    @NotNull
    public String getVersion() {
        return this.plugin.getPluginMeta().getVersion();
    }

    public boolean persist() {
        return true;
    }

    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) {
            return "";
        }
        // %cyberalliances_name%
        if (params.equalsIgnoreCase("name")) {
            return this.allianceStorage.getAllianceByPlayer(player.getUniqueId()).map(alliance -> alliance.getName()).orElse("None");
        }
        // %cyberalliances_members%
        if (params.equalsIgnoreCase("members")) {
            return this.allianceStorage.getAllianceByPlayer(player.getUniqueId())
                    .map(alliance -> String.valueOf(alliance.getMembers().size()))
                    .orElse("0");
        }
        // %cyberalliances_members_max%
        if (params.equalsIgnoreCase("members_max")) {
            return String.valueOf(Alliance.MAX_MEMBERS);
        }
        return "";
    }
}

