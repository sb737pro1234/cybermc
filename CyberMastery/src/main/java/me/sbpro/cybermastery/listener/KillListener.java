package me.sbpro.cybermastery.listener;

import me.sbpro.cybermastery.CyberMastery;
import me.sbpro.cybermastery.model.MasteryType;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * Slaying: killing mobs. Players and baby mobs give nothing. Spawner mobs count.
 */
public final class KillListener implements Listener {

    private final CyberMastery plugin;

    public KillListener(CyberMastery plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity instanceof Player) {
            return;
        }

        Player killer = entity.getKiller();
        if (killer == null) {
            return;
        }

        if (entity instanceof Ageable ageable && !ageable.isAdult()) {
            return;
        }

        MasteryType type = plugin.getRegistry().mobType(entity.getType());
        if (type == null || !plugin.getService().canEarn(killer)) {
            return;
        }

        plugin.getService().awardActions(killer, type, 1);
    }
}
