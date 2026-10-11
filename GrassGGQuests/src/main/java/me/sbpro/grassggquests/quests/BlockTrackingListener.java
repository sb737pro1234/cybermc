package me.sbpro.grassggquests.quests;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.ArrayList;
import java.util.List;

/** Keeps the {@link PlacedBlockTracker} accurate: placements, moved blocks, falling blocks and destroyed blocks. */
public class BlockTrackingListener implements Listener {
    private static final String FALLING_TAG = "grassggquests_placed";

    private final PlacedBlockTracker tracker;

    public BlockTrackingListener(PlacedBlockTracker tracker) { this.tracker = tracker; }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        tracker.mark(block, block.getType());
    }

    /** Stone generators: formed stone is not natural stone. */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onForm(BlockFormEvent event) {
        Material formed = event.getNewState().getType();
        if (formed == Material.STONE) tracker.mark(event.getBlock(), formed);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent event) {
        shift(event.getBlocks(), event.getDirection());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPistonRetract(BlockPistonRetractEvent event) {
        shift(event.getBlocks(), event.getDirection().getOppositeFace());
    }

    private void shift(List<Block> blocks, BlockFace direction) {
        List<Block> moved = new ArrayList<>();
        for (Block block : blocks) if (tracker.isTrackedMaterial(block.getType()) && tracker.isPlaced(block)) moved.add(block);
        if (moved.isEmpty()) return;
        for (Block block : moved) tracker.unmark(block);
        for (Block block : moved) tracker.mark(block.getRelative(direction), block.getType());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        for (Block block : event.blockList()) tracker.unmark(block);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent event) {
        for (Block block : event.blockList()) tracker.unmark(block);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBurn(BlockBurnEvent event) {
        tracker.unmark(event.getBlock());
    }

    /** A player-placed gravity block that starts falling keeps its "placed" status when it lands. */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFallingBlock(EntityChangeBlockEvent event) {
        if (!(event.getEntity() instanceof FallingBlock falling)) return;
        Block block = event.getBlock();
        if (event.getTo().isAir()) {
            // The block is turning into a falling entity.
            if (tracker.isTrackedMaterial(block.getType()) && tracker.consume(block)) falling.addScoreboardTag(FALLING_TAG);
        } else if (falling.getScoreboardTags().contains(FALLING_TAG)) {
            tracker.mark(block, event.getTo());
        }
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        tracker.onChunkUnload(event.getChunk());
    }
}
