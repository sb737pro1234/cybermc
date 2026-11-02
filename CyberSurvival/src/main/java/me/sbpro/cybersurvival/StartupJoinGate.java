package me.sbpro.cybersurvival;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class StartupJoinGate implements Listener {

    /** Extra wait after the server reports it has finished loading. */
    private static final long GRACE_TICKS = 20L * 15;

    private final JavaPlugin plugin;
    private volatile boolean open = false;

    public StartupJoinGate(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerLoad(ServerLoadEvent event) {
        if (event.getType() != ServerLoadEvent.LoadType.STARTUP) {
            return;
        }
        Bukkit.getScheduler().runTaskLater(plugin, () -> open = true, GRACE_TICKS);
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (!open) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                    Component.text("Server is still starting up, please try again in a few seconds.",
                            NamedTextColor.RED));
        }
    }
}