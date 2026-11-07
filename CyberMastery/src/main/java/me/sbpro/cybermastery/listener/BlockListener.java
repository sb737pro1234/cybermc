package me.sbpro.cybermastery.listener;

import me.sbpro.cybermastery.CyberMastery;
import me.sbpro.cybermastery.model.Category;
import me.sbpro.cybermastery.model.MasteryType;
import me.sbpro.cybermastery.service.MasteryService;
import me.sbpro.cybermastery.tracking.PlacedBlockTracker;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Placing and breaking blocks.
 *
 * Blocks:   mining a natural block gives its points; placing gives a share once per position.
 * Foraging: mining a natural log, stem, wood or hyphae gives its points. Placing gives nothing.
 * Farming:  handled by {@link FarmingListener#handleBreak}.
 *
 * Runs at MONITOR and ignores cancelled events, so protected areas never give points.
 */
public final class BlockListener implements Listener {

    private final CyberMastery plugin;
    private final FarmingListener farming;

    public BlockListener(CyberMastery plugin, FarmingListener farming) {
        this.plugin = plugin;
        this.farming = farming;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        PlacedBlockTracker tracker = plugin.getTracker();

        // Beds and similar place more than one block: flag them all, no points.
        if (event instanceof BlockMultiPlaceEvent multi) {
            for (BlockState state : multi.getReplacedBlockStates()) {
                Block block = state.getBlock();
                if (plugin.getRegistry().isTrackedBlock(block.getType())) {
                    tracker.markPlaced(block);
                }
            }
            return;
        }

        Block block = event.getBlockPlaced();
        MasteryType type = plugin.getRegistry().blockType(block.getType());

        if (type == null) {
            return;
        }

        tracker.markPlaced(block);

        Player player = event.getPlayer();
        MasteryService service = plugin.getService();

        if (type.category() != Category.BLOCKS || !service.canEarn(player)) {
            return;
        }

        // Each position only ever gives placement points once.
        if (tracker.isCredited(block)) {
            return;
        }

        long units = service.takePlacementUnits(player, type);
        if (units <= 0) {
            return;
        }

        tracker.markCredited(block);
        service.award(player, type, units);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        PlacedBlockTracker tracker = plugin.getTracker();

        boolean placed = tracker.isPlaced(block);
        if (placed) {
            tracker.unmarkPlaced(block);
        }

        MasteryType type = plugin.getRegistry().blockType(block.getType());
        if (type == null) {
            return;
        }

        Player player = event.getPlayer();
        boolean earn = plugin.getService().canEarn(player);

        if (type.category() == Category.FARMING) {
            farming.handleBreak(player, block, type, placed, earn);
            return;
        }

        // Blocks and Foraging: player-placed blocks never give points.
        if (earn && !placed) {
            plugin.getService().awardActions(player, type, 1);
        }
    }
}
