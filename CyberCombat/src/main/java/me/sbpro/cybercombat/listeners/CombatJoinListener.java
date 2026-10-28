package me.sbpro.cybercombat.listeners;

import me.sbpro.cybercombat.manager.CombatManager;
import me.sbpro.cybercombat.Messages;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.Duration;

public class CombatJoinListener implements Listener {

    private final CombatManager combatManager;

    public CombatJoinListener(CombatManager combatManager) {
        this.combatManager = combatManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        if (!combatManager.wasCombatLoggedOut(player)) {
            return;
        }

        player.showTitle(
                Title.title(
                        Messages.YOU_DIED_TITLE,
                        Messages.LOGGED_OUT_SUBTITLE,
                        Title.Times.times(
                                Duration.ofMillis(500),
                                Duration.ofSeconds(3),
                                Duration.ofMillis(500)
                        )
                )
        );

        combatManager.clearCombatLoggedOut(player);
    }
}
