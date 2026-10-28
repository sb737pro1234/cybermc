package me.sbpro.cybercombat.listeners;

import me.sbpro.cybercombat.manager.CombatManager;
import me.sbpro.cybercombat.CombatPermissions;
import me.sbpro.cybercombat.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CombatCommandListener implements Listener {

    private final CombatManager combatManager;

    public CombatCommandListener(CombatManager combatManager) {
        this.combatManager = combatManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {

        Player player = event.getPlayer();

        if (!combatManager.isInCombat(player)) {
            return;
        }

        // Players with the override permission can use commands in combat.
        if (player.hasPermission(CombatPermissions.OVERRIDE)) {
            return;
        }

        event.setCancelled(true);

        player.sendMessage(Messages.COMMAND_BLOCKED);
    }
}
