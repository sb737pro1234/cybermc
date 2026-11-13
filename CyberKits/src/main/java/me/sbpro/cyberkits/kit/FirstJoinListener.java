package me.sbpro.cyberkits.kit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.sbpro.cyberkits.CyberKits;
import me.sbpro.cyberkits.Kits;

/** Gives the Starter kit on a player's first join and starts its 12 hour cooldown. */
public final class FirstJoinListener implements Listener {

    /** Short delay so other plugins' first-join handling (inventory clear, teleport) runs first. */
    private static final long DELAY_TICKS = 10L;

    private final CyberKits plugin;

    public FirstJoinListener(CyberKits plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPlayedBefore()) return;

        Kit kit = Kits.byId(Kits.FIRST_JOIN_KIT);
        if (kit == null) return;

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (player.isOnline()) plugin.claimer().giveFirstJoin(player, kit);
        }, DELAY_TICKS);
    }
}
