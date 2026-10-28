package me.sbpro.cybercombat.manager;

import me.sbpro.cybercombat.Messages;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CombatManager {

    private static final int COMBAT_TIME = 15;

    private final JavaPlugin plugin;
    private final Map<UUID, Integer> combatTimers = new HashMap<>();
    private final Set<UUID> combatLoggedOut = new HashSet<>();

    private BukkitTask timerTask;

    public CombatManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void tag(Player player, Player opponent) {

        boolean wasInCombat = isInCombat(player);

        combatTimers.put(player.getUniqueId(), COMBAT_TIME);
        updateActionBar(player);

        // Only announce entering combat the first time they're tagged,
        // not on every subsequent hit that just refreshes the timer.
        if (!wasInCombat) {
            sendEnteredCombatMessage(player, opponent);
        }
    }

    public boolean isInCombat(Player player) {
        return combatTimers.containsKey(player.getUniqueId());
    }

    public int getRemainingTime(Player player) {
        return combatTimers.getOrDefault(player.getUniqueId(), 0);
    }

    public void remove(Player player) {

        boolean wasInCombat = combatTimers.remove(player.getUniqueId()) != null;

        player.sendActionBar(Component.empty());

        if (wasInCombat) {
            sendLeftCombatMessage(player);
        }
    }

    /**
     * Marks a player as having combat logged.
     */
    public void markCombatLoggedOut(Player player) {
        combatLoggedOut.add(player.getUniqueId());
    }

    /**
     * Checks whether a player combat logged.
     */
    public boolean wasCombatLoggedOut(Player player) {
        return combatLoggedOut.contains(player.getUniqueId());
    }

    /**
     * Removes the combat logout status.
     */
    public void clearCombatLoggedOut(Player player) {
        combatLoggedOut.remove(player.getUniqueId());
    }

    public void startTimer() {
        timerTask = Bukkit.getScheduler().runTaskTimer(
                plugin,
                () -> {

                    Map<UUID, Integer> expired = new HashMap<>();

                    for (Map.Entry<UUID, Integer> entry : combatTimers.entrySet()) {

                        UUID uuid = entry.getKey();
                        int time = entry.getValue();

                        Player player = Bukkit.getPlayer(uuid);

                        if (player == null) {
                            expired.put(uuid, 0);
                            continue;
                        }

                        time--;

                        if (time <= 0) {
                            expired.put(uuid, 0);
                            player.sendActionBar(Component.empty());
                            sendLeftCombatMessage(player);
                            continue;
                        }

                        combatTimers.put(uuid, time);
                        updateActionBar(player);
                    }

                    for (UUID uuid : expired.keySet()) {
                        combatTimers.remove(uuid);
                    }

                },
                20L,
                20L
        );
    }

    public void stopTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    private void updateActionBar(Player player) {
        player.sendActionBar(Messages.combatActionBar(getRemainingTime(player)));
    }

    private void sendEnteredCombatMessage(Player player, Player opponent) {
        player.sendMessage(Messages.enteredCombat(opponent.getName(), COMBAT_TIME));
    }

    private void sendLeftCombatMessage(Player player) {
        player.sendMessage(Messages.LEFT_COMBAT);
    }
}