package me.sbpro.cybermastery.listener;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import me.sbpro.cybermastery.CyberMastery;
import me.sbpro.cybermastery.tracking.PlacedBlockTracker;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

/**
 * Keeps the placed-block flags correct when blocks move, change or disappear
 * without a player breaking them, so flags never leak or get lost.
 */
public final class TrackerListener implements Listener {

    private final CyberMastery plugin;

    public TrackerListener(CyberMastery plugin) {
        this.plugin = plugin;
    }

    private PlacedBlockTracker tracker() {
        return plugin.getTracker();
    }

    // =============================================================
    // Pistons: flags move with the blocks
    // =============================================================

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent event) {
        tracker().move(event.getBlocks(), facing(event.getBlock(), event.getDirection()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPistonRetract(BlockPistonRetractEvent event) {
        // Sticky pistons pull blocks back towards themselves.
        tracker().move(event.getBlocks(), facing(event.getBlock(), event.getDirection()).getOppositeFace());
    }

    private static BlockFace facing(Block piston, BlockFace fallback) {
        return piston.getBlockData() instanceof Directional directional ? directional.getFacing() : fallback;
    }

    // =============================================================
    // Blocks removed without a player
    // =============================================================

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent event) {
        unmarkAll(event.blockList());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        unmarkAll(event.blockList());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBurn(BlockBurnEvent event) {
        tracker().unmarkPlaced(event.getBlock());
    }

    /** Blocks broken by physics, e.g. losing the block they stood on. */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDestroy(BlockDestroyEvent event) {
        tracker().unmarkPlaced(event.getBlock());
    }

    /** Water and lava washing blocks away (crops, torches ...). Solid blocks are never washed away. */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFlow(BlockFromToEvent event) {
        Block to = event.getToBlock();
        if (!to.getType().isSolid()) {
            tracker().unmarkPlaced(to);
        }
    }

    /** Ice melting, coral dying ... only clear the flag if the block is really gone. */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFade(BlockFadeEvent event) {
        Material to = event.getNewState().getType();
        if (to.isAir() || !plugin.getRegistry().isTrackedBlock(to)) {
            tracker().unmarkPlaced(event.getBlock());
        }
    }

    // =============================================================
    // Blocks appearing
    // =============================================================

    /**
     * Blocks made by lava and water (stone, basalt, cobblestone generators ...) are natural,
     * so they count when mined. Any old flag left in that space is cleared.
     *
     * Only clears when the space was air or a liquid, so placed concrete powder that
     * hardens into concrete keeps its placed flag. Grass spreading is ignored.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onForm(BlockFormEvent event) {
        if (event instanceof BlockSpreadEvent) {
            return;
        }
        Material before = event.getBlock().getType();
        if (before.isAir() || before == Material.WATER || before == Material.LAVA) {
            tracker().unmarkPlaced(event.getBlock());
        }
    }

    /** Melons, pumpkins, sugar cane, cactus, bamboo and kelp growing into a new space are natural. */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onGrow(BlockGrowEvent event) {
        Material before = event.getBlock().getType();
        if (before.isAir() || before == Material.WATER) {
            tracker().unmarkPlaced(event.getBlock());
        }
    }

    // =============================================================
    // Falling blocks and mobs changing blocks
    // =============================================================

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        Block block = event.getBlock();
        Material to = event.getTo();

        if (event.getEntity() instanceof FallingBlock falling) {
            if (to.isAir()) {
                // Starting to fall: the flag travels with the falling block.
                if (tracker().isPlaced(block)) {
                    tracker().unmarkPlaced(block);
                    falling.getPersistentDataContainer().set(tracker().placedKey(), PersistentDataType.BYTE, (byte) 1);
                }
            } else {
                // Landing.
                boolean wasPlaced = falling.getPersistentDataContainer().has(tracker().placedKey(), PersistentDataType.BYTE);
                if (wasPlaced && plugin.getRegistry().isTrackedBlock(to)) {
                    tracker().markPlaced(block);
                } else {
                    tracker().unmarkPlaced(block);
                }
            }
            return;
        }

        // Endermen, ravagers, withers, silverfish ...
        if (to.isAir() || !plugin.getRegistry().isTrackedBlock(to)) {
            tracker().unmarkPlaced(block);
        }
    }

    // =============================================================
    // Saving
    // =============================================================

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChunkUnload(ChunkUnloadEvent event) {
        tracker().unload(event.getChunk());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldSave(WorldSaveEvent event) {
        tracker().flushWorld(event.getWorld());
    }

    private void unmarkAll(List<Block> blocks) {
        for (Block block : blocks) {
            tracker().unmarkPlaced(block);
        }
    }
}
