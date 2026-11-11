package me.sbpro.cyberhub.protection;

import java.util.EnumSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.WindCharge;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import me.sbpro.cyberhub.CyberHub;

/**
 * Hub rules for players WITHOUT cyber.builder. Builders get vanilla behaviour.
 * World rules (explosions, natural spawns, fire spread, weather) apply to everyone.
 */
public final class HubProtectionListener implements Listener {

    public static final String BUILDER_PERMISSION = "cyber.builder";

    private static final Set<EntityDamageEvent.DamageCause> BLOCKED_DAMAGE = EnumSet.of(
            EntityDamageEvent.DamageCause.FALL,
            EntityDamageEvent.DamageCause.FIRE,
            EntityDamageEvent.DamageCause.FIRE_TICK,
            EntityDamageEvent.DamageCause.HOT_FLOOR,
            EntityDamageEvent.DamageCause.CAMPFIRE,
            EntityDamageEvent.DamageCause.LAVA,
            EntityDamageEvent.DamageCause.DROWNING,
            EntityDamageEvent.DamageCause.SUFFOCATION,
            EntityDamageEvent.DamageCause.VOID
    );

    private static final Set<CreatureSpawnEvent.SpawnReason> NATURAL_SPAWNS = EnumSet.of(
            CreatureSpawnEvent.SpawnReason.NATURAL,
            CreatureSpawnEvent.SpawnReason.JOCKEY,
            CreatureSpawnEvent.SpawnReason.MOUNT,
            CreatureSpawnEvent.SpawnReason.PATROL,
            CreatureSpawnEvent.SpawnReason.RAID,
            CreatureSpawnEvent.SpawnReason.REINFORCEMENTS,
            CreatureSpawnEvent.SpawnReason.VILLAGE_DEFENSE,
            CreatureSpawnEvent.SpawnReason.VILLAGE_INVASION,
            CreatureSpawnEvent.SpawnReason.TRAP,
            CreatureSpawnEvent.SpawnReason.LIGHTNING
    );

    private final Plugin plugin;

    /** True for the tick a builder runs /summon, so command spawns are allowed for them. */
    private boolean builderSummoning = false;

    public HubProtectionListener(Plugin plugin) {
        this.plugin = plugin;

        // weather: deny — start every world clear
        for (World world : Bukkit.getWorlds()) {
            world.setStorm(false);
            world.setThundering(false);
        }
    }

    private static boolean restricted(Entity entity) {
        return entity instanceof Player player && !player.hasPermission(BUILDER_PERMISSION);
    }

    /** The player behind an attack, whether direct or via a projectile they shot. */
    private static Player attacker(Entity damager) {
        if (damager instanceof Player player) return player;
        if (damager instanceof Projectile projectile && projectile.getShooter() instanceof Player shooter) return shooter;
        return null;
    }

    // ---------------------------------------------------------------- Blocks

    // block_break: deny
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        if (restricted(event.getPlayer())) event.setCancelled(true);
    }

    // block_place: deny
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        if (restricted(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if (restricted(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBucketFill(PlayerBucketFillEvent event) {
        if (restricted(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onHangingPlace(HangingPlaceEvent event) {
        if (event.getPlayer() != null && restricted(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onHangingBreak(HangingBreakByEntityEvent event) {
        Player remover = event.getRemover() == null ? null : attacker(event.getRemover());
        if (remover != null && restricted(remover)) event.setCancelled(true);
    }

    // ---------------------------------------------------------- Interactions

    // block_interact: allow | doors: deny | trapdoors: deny | item_interact: allow
    // mob_spawn_egg: deny
    @EventHandler(priority = EventPriority.LOW)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!restricted(player)) return;

        Block block = event.getClickedBlock();

        // Stop crop trampling (counts as breaking)
        if (event.getAction() == Action.PHYSICAL) {
            if (block != null && block.getType() == Material.FARMLAND) event.setCancelled(true);
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && block != null
                && (Tag.DOORS.isTagged(block.getType()) || Tag.TRAPDOORS.isTagged(block.getType()))) {
            event.setUseInteractedBlock(Event.Result.DENY);
        }

        ItemStack item = event.getItem();
        if (item != null && item.getType().name().endsWith("_SPAWN_EGG")) {
            event.setUseItemInHand(Event.Result.DENY);
        }
    }

    // entity_interact: deny
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        if (restricted(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onArmorStand(PlayerArmorStandManipulateEvent event) {
        if (restricted(event.getPlayer())) event.setCancelled(true);
    }

    // windcharges: deny
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof WindCharge
                && event.getEntity().getShooter() instanceof Player player && restricted(player)) {
            event.setCancelled(true);
        }
    }

    // eat: deny
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onConsume(PlayerItemConsumeEvent event) {
        if (restricted(event.getPlayer())) event.setCancelled(true);
    }

    // ---------------------------------------------------------------- Combat

    // pvp: deny | projectile_damage: deny | entity_interact: deny (hitting mobs, armor stands, frames)
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        Player attacker = attacker(event.getDamager());
        Entity victim = event.getEntity();

        if (attacker != null && restricted(attacker)) {
            event.setCancelled(true);
            return;
        }

        if (victim instanceof Player && restricted(victim)
                && (event.getDamager() instanceof Projectile || attacker != null)) {
            event.setCancelled(true);
        }
    }

    // --------------------------------------------------------- Player damage

    // fall / fire / lava / drowning / suffocation / void damage: deny
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        if (!restricted(event.getEntity())) return;
        if (!BLOCKED_DAMAGE.contains(event.getCause())) return;

        Player player = (Player) event.getEntity();
        event.setCancelled(true);

        switch (event.getCause()) {
            case FIRE, FIRE_TICK, LAVA -> player.setFireTicks(0);
            case DROWNING -> player.setRemainingAir(player.getMaximumAir());
            // No damage would mean falling forever, so send them back to spawn
            case VOID -> {
                player.setFallDistance(0F);
                player.teleport(CyberHub.hubSpawn(player.getWorld()));
            }
            default -> {}
        }
    }

    // ------------------------------------------------------ Player mechanics

    // hunger: deny
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onHunger(FoodLevelChangeEvent event) {
        if (!restricted(event.getEntity())) return;

        if (event.getFoodLevel() < event.getEntity().getFoodLevel() || event.getEntity().getFoodLevel() < 20) {
            event.setFoodLevel(20);
            ((Player) event.getEntity()).setSaturation(20F);
        }
    }

    // item_drop: deny
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent event) {
        if (restricted(event.getPlayer())) event.setCancelled(true);
    }

    // item_pickup: deny
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPickup(EntityPickupItemEvent event) {
        if (restricted(event.getEntity())) event.setCancelled(true);
    }

    // ------------------------------------------------------- World mechanics

    // explosions: deny
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent event) {
        event.setCancelled(true);
    }

    // mob_spawn_natural: deny | mob_spawn_command: deny (builders may still /summon)
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onSpawn(CreatureSpawnEvent event) {
        CreatureSpawnEvent.SpawnReason reason = event.getSpawnReason();

        if (NATURAL_SPAWNS.contains(reason)) {
            event.setCancelled(true);
        } else if (reason == CreatureSpawnEvent.SpawnReason.COMMAND && !builderSummoning) {
            event.setCancelled(true);
        }
    }

    // Mobs created by world generation don't fire CreatureSpawnEvent, so clear them from new chunks
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        if (!event.isNewChunk()) return;

        for (Entity entity : event.getChunk().getEntities()) {
            if (entity instanceof Mob) entity.remove();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String label = event.getMessage().toLowerCase().split(" ", 2)[0];
        if (!label.equals("/summon") && !label.equals("/minecraft:summon")) return;
        if (restricted(event.getPlayer())) return;

        builderSummoning = true;
        Bukkit.getScheduler().runTask(plugin, () -> builderSummoning = false);
    }

    // block_burn: deny — fire can't spread or burn blocks; only builders can light fires
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBurn(BlockBurnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onIgnite(BlockIgniteEvent event) {
        Player player = event.getPlayer();
        if (player == null || restricted(player)) event.setCancelled(true);
    }

    // weather: deny
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onWeather(WeatherChangeEvent event) {
        if (event.toWeatherState()) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onThunder(ThunderChangeEvent event) {
        if (event.toThunderState()) event.setCancelled(true);
    }
}
