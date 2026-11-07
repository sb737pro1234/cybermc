package me.sbpro.cybermastery.listener;

import me.sbpro.cybermastery.CyberMastery;
import me.sbpro.cybermastery.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/** Loads data before join (off the main thread) and saves it on quit. */
public final class PlayerConnectionListener implements Listener {

    private final CyberMastery plugin;

    public PlayerConnectionListener(CyberMastery plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            return;
        }
        plugin.getDataManager().loadBlocking(event.getUniqueId(), event.getName());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData data = plugin.getDataManager().loadNow(player.getUniqueId(), player.getName());
        plugin.getService().refreshTotals(data);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerData data = plugin.getDataManager().get(player.getUniqueId());

        if (data != null) {
            plugin.getService().refreshTotals(data);
        }

        plugin.getService().forget(player.getUniqueId());
        plugin.getDataManager().unload(player.getUniqueId());
    }
}
