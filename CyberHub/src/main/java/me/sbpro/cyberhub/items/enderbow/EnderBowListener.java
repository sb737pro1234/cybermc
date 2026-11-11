package me.sbpro.cyberhub.items.enderbow;

import io.papermc.paper.event.player.PlayerStopUsingItemEvent;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public final class EnderBowListener implements Listener {

    /** Same launch power as a vanilla bow at full draw. */
    private static final double MAX_SPEED = 3.0;

    private final Plugin plugin;
    private final NamespacedKey pearlKey;

    public EnderBowListener(Plugin plugin) {
        this.plugin = plugin;
        this.pearlKey = new NamespacedKey(plugin, "ender_bow_pearl");
    }

    // Released the draw: fire a pearl scaled by how long it was charged
    @EventHandler
    public void onRelease(PlayerStopUsingItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (!EnderBow.isEnderBow(plugin, item)) return;
        if (player.hasCooldown(item)) return;

        float power = powerForTicks(event.getTicksHeldFor());
        if (power < 0.1F) return;

        Vector velocity = player.getLocation().getDirection().multiply(power * MAX_SPEED);
        EnderPearl pearl = player.launchProjectile(EnderPearl.class, velocity);
        pearl.getPersistentDataContainer().set(pearlKey, PersistentDataType.BOOLEAN, true);

        player.setCooldown(item, EnderBow.COOLDOWN_TICKS);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1F,
                1F / (0.9F + (float) Math.random() * 0.4F) + power * 0.5F);
    }

    // Safety net: never let the bow actually be eaten
    @EventHandler(priority = EventPriority.LOWEST)
    public void onConsume(PlayerItemConsumeEvent event) {
        if (EnderBow.isEnderBow(plugin, event.getItem())) {
            event.setCancelled(true);
        }
    }

    // Teleport the shooter ourselves: no pearl damage, no endermites
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPearlLand(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof EnderPearl pearl)) return;
        if (!pearl.getPersistentDataContainer().has(pearlKey, PersistentDataType.BOOLEAN)) return;

        event.setCancelled(true);
        pearl.remove();

        if (!(pearl.getShooter() instanceof Player player) || !player.isOnline()) return;
        if (!player.getWorld().equals(pearl.getWorld())) return;

        Location target = pearl.getLocation();
        target.setYaw(player.getLocation().getYaw());
        target.setPitch(player.getLocation().getPitch());

        player.setFallDistance(0F);
        player.teleport(target, PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
        player.getWorld().playSound(target, Sound.ENTITY_PLAYER_TELEPORT, 1F, 1F);
    }

    /** Vanilla BowItem draw curve: 1.0 at 20 ticks (1 second). */
    private static float powerForTicks(int ticks) {
        float f = ticks / 20F;
        f = (f * f + f * 2F) / 3F;
        return Math.min(f, 1F);
    }
}
