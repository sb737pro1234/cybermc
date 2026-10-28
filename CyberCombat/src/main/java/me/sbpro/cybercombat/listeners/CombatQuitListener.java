package me.sbpro.cybercombat.listeners;

import me.sbpro.cybercombat.manager.CombatManager;
import me.sbpro.cybercombat.CombatPermissions;
import me.sbpro.cybercombat.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class CombatQuitListener implements Listener {

    private final CombatManager combatManager;

    public CombatQuitListener(CombatManager combatManager) {
        this.combatManager = combatManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        if (!combatManager.isInCombat(player)) {
            return;
        }

        /*
         * Players with the override permission can leave safely.
         * Their combat timer is reset.
         */
        if (player.hasPermission(CombatPermissions.OVERRIDE)) {
            combatManager.remove(player);
            return;
        }

        /*
         * Mark the player as having combat logged.
         */
        combatManager.markCombatLoggedOut(player);

        /*
         * Tell everyone that the player combat logged.
         */
        Bukkit.broadcast(Messages.combatLogout(player.getName()));

        Location location = player.getLocation();

        /*
         * Drop main inventory.
         */
        for (ItemStack item : player.getInventory().getContents()) {

            if (item == null || item.getType().isAir()) {
                continue;
            }

            location.getWorld().dropItemNaturally(location, item);
        }

        /*
         * Drop armor.
         */
        for (ItemStack item : player.getInventory().getArmorContents()) {

            if (item == null || item.getType().isAir()) {
                continue;
            }

            location.getWorld().dropItemNaturally(location, item);
        }

        /*
         * Drop off-hand item.
         */
        ItemStack offHand = player.getInventory().getItemInOffHand();

        if (!offHand.getType().isAir()) {
            location.getWorld().dropItemNaturally(location, offHand);
        }

        /*
         * Clear inventory to prevent duplication.
         */
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.getInventory().setItemInOffHand(null);

        /*
         * Kill the player.
         */
        player.setHealth(0);

        /*
         * Reset combat timer.
         */
        combatManager.remove(player);
    }
}
