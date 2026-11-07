package me.sbpro.cybermastery.listener;

import me.sbpro.cybermastery.CyberMastery;
import me.sbpro.cybermastery.model.MasteryType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

/**
 * Fishing: only cod, salmon, pufferfish and tropical fish count.
 * Treasure and junk are caught with the same event but match no type.
 */
public final class FishListener implements Listener {

    private final CyberMastery plugin;

    public FishListener(CyberMastery plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFish(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) {
            return;
        }

        if (!(event.getCaught() instanceof Item item)) {
            return;
        }

        MasteryType type = plugin.getRegistry().fishType(item.getItemStack().getType());
        if (type == null || !plugin.getService().canEarn(event.getPlayer())) {
            return;
        }

        plugin.getService().awardActions(event.getPlayer(), type, 1);
    }
}
