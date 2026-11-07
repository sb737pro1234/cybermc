package me.sbpro.cybermastery.listener;

import me.sbpro.cybermastery.CyberMastery;
import me.sbpro.cybermastery.model.Category;
import me.sbpro.cybermastery.model.MasteryType;
import me.sbpro.cybermastery.tracking.PlacedBlockTracker;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.entity.Player;

import java.util.EnumSet;
import java.util.Set;

/**
 * Farming rules.
 *
 * Age crops (wheat, carrots, cocoa, nether wart ...) count only at maximum growth,
 * even when the player planted them.
 * Column crops (sugar cane, cactus, bamboo, kelp) count every grown piece that breaks,
 * but never a piece a player placed (e.g. the planted base).
 * Melons and pumpkins count unless a player placed them.
 */
public final class FarmingListener implements Listener {

    private static final Set<Material> COLUMN_CROPS = EnumSet.of(
            Material.SUGAR_CANE,
            Material.CACTUS,
            Material.BAMBOO,
            Material.KELP,
            Material.KELP_PLANT
    );

    /** Highest column we look up (bamboo grows to 16). */
    private static final int MAX_COLUMN = 32;

    private final CyberMastery plugin;

    public FarmingListener(CyberMastery plugin) {
        this.plugin = plugin;
    }

    /** Called by BlockListener for a broken Farming block (after its own flag was removed). */
    public void handleBreak(Player player, Block block, MasteryType type, boolean placed, boolean earn) {
        Material material = block.getType();
        PlacedBlockTracker tracker = plugin.getTracker();

        if (COLUMN_CROPS.contains(material)) {
            int pieces = placed ? 0 : 1;

            // Everything above breaks too: count the grown pieces, drop the flags of placed ones.
            Block above = block.getRelative(BlockFace.UP);
            for (int i = 0; i < MAX_COLUMN && sameColumn(material, above.getType()); i++) {
                if (tracker.isPlaced(above)) {
                    tracker.unmarkPlaced(above);
                } else {
                    pieces++;
                }
                above = above.getRelative(BlockFace.UP);
            }

            if (earn) {
                plugin.getService().awardActions(player, type, pieces);
            }
            return;
        }

        if (!earn) {
            return;
        }

        if (block.getBlockData() instanceof Ageable ageable) {
            if (ageable.getAge() >= ageable.getMaximumAge()) {
                plugin.getService().awardActions(player, type, 1);
            }
            return;
        }

        // Melons and pumpkins.
        if (!placed) {
            plugin.getService().awardActions(player, type, 1);
        }
    }

    /** Sweet berries picked with right click (the bush stays). */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onHarvest(PlayerHarvestBlockEvent event) {
        Block block = event.getHarvestedBlock();
        MasteryType type = plugin.getRegistry().blockType(block.getType());

        if (type == null || type.category() != Category.FARMING) {
            return;
        }

        if (!(block.getBlockData() instanceof Ageable ageable) || ageable.getAge() < ageable.getMaximumAge()) {
            return;
        }

        if (plugin.getService().canEarn(event.getPlayer())) {
            plugin.getService().awardActions(event.getPlayer(), type, 1);
        }
    }

    private static boolean sameColumn(Material base, Material other) {
        if (base == Material.KELP || base == Material.KELP_PLANT) {
            return other == Material.KELP || other == Material.KELP_PLANT;
        }
        return base == other;
    }
}
