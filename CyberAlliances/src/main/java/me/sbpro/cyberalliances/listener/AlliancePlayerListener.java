package me.sbpro.cyberalliances.listener;

import me.sbpro.cyberalliances.CyberAlliances;
import me.sbpro.cyberalliances.service.AllianceBuffService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;

public final class AlliancePlayerListener
implements Listener {
    private final CyberAlliances plugin;
    private final AllianceBuffService buffService;

    public AlliancePlayerListener(CyberAlliances plugin, AllianceBuffService buffService) {
        this.plugin = plugin;
        this.buffService = buffService;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        this.buffService.refreshPlayer(event.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        this.plugin.getServer().getScheduler().runTask((Plugin)this.plugin, () -> this.buffService.refreshPlayer(event.getPlayer()));
    }
}

