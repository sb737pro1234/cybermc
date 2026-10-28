package me.sbpro.cyberwarp.warp;

import me.sbpro.cyberwarp.Messages;
import me.sbpro.cyberwarp.CyberWarp;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WarpHelper implements Listener {

    private final CyberWarp plugin;

    private final Map<UUID, Listener> movementListeners = new HashMap<>();
    private final Map<UUID, BukkitRunnable> teleportTasks = new HashMap<>();

    public WarpHelper(CyberWarp plugin) {
        this.plugin = plugin;
    }

    /**
     * Teleports a player to a configured warp.
     *
     * @param player Player being teleported
     * @param warpName Config key of the warp
     */
    public void teleport(Player player, String warpName) {

        Location location = plugin.getConfig().getLocation(warpName);

        if (location == null) {

            player.sendMessage(
                    Messages.warpLocationNotSet(warpName)
            );

            return;
        }

        // Cancel any existing teleport.
        cancelTeleport(player);

        player.sendMessage(
                Messages.teleportingToWarp(warpName)
        );

        Listener moveListener = new Listener() {

            @EventHandler
            public void onPlayerMove(PlayerMoveEvent event) {

                if (!event.getPlayer().equals(player)) {
                    return;
                }

                if (event.getTo() == null) {
                    return;
                }

                // Looking around does not count as movement.
                if (event.getFrom().getBlockX() == event.getTo().getBlockX()
                        && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
                    return;
                }

                player.sendActionBar(Component.empty());

                player.sendMessage(
                        Messages.TELEPORT_CANCELLED_MOVED
                );

                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);

                cancelTeleport(player);
            }
        };

        movementListeners.put(
                player.getUniqueId(),
                moveListener
        );

        plugin.getServer()
                .getPluginManager()
                .registerEvents(moveListener, plugin);

        BukkitRunnable task = new BukkitRunnable() {

            int countdown = 4;

            @Override
            public void run() {

                if (!player.isOnline()) {
                    cancelTeleport(player);
                    cancel();
                    return;
                }

                if (!movementListeners.containsKey(player.getUniqueId())) {
                    cancel();
                    return;
                }

                if (countdown <= 0) {

                    Listener listener =
                            movementListeners.remove(
                                    player.getUniqueId()
                            );

                    if (listener != null) {
                        HandlerList.unregisterAll(listener);
                    }

                    teleportTasks.remove(player.getUniqueId());

                    player.teleport(location);

                    player.playSound(
                            player.getLocation(),
                            Sound.ENTITY_ENDERMAN_TELEPORT,
                            1.0f,
                            1.0f
                    );

                    player.sendActionBar(Component.empty());

                    player.sendMessage(
                            Messages.teleportedToWarp(warpName)
                    );

                    cancel();
                    return;
                }

                player.playSound(
                        player.getLocation(),
                        Sound.BLOCK_NOTE_BLOCK_HAT,
                        1.0f,
                        1.0f
                );

                Component actionBar = Messages.teleportCountdown(countdown);

                player.sendActionBar(actionBar);

                countdown--;
            }
        };

        teleportTasks.put(
                player.getUniqueId(),
                task
        );

        task.runTaskTimer(
                plugin,
                0L,
                20L
        );
    }

    /**
     * Cancels an active teleport for a player.
     */
    public void cancelTeleport(Player player) {

        UUID uuid = player.getUniqueId();

        BukkitRunnable task =
                teleportTasks.remove(uuid);

        if (task != null) {
            task.cancel();
        }

        Listener listener =
                movementListeners.remove(uuid);

        if (listener != null) {
            HandlerList.unregisterAll(listener);
        }

        player.sendActionBar(Component.empty());
    }

    /**
     * Cancels all active teleports when the plugin shuts down.
     */
    public void shutdown() {

        for (BukkitRunnable task : teleportTasks.values()) {
            task.cancel();
        }

        for (Listener listener : movementListeners.values()) {
            HandlerList.unregisterAll(listener);
        }

        teleportTasks.clear();
        movementListeners.clear();
    }

    public void setWarp(String warpName, Location location) {

        plugin.getConfig().set(warpName, location);
        plugin.saveConfig();
    }
}