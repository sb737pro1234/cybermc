package me.sbpro.cybermastery.tracking;

import me.sbpro.cybermastery.CyberMastery;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Remembers which block positions were placed by players, and which positions
 * have already given placement points, in each chunk's PersistentDataContainer.
 *
 * Positions are packed into one int each (x, z inside the chunk and y).
 * A chunk's sets are read from its PDC the first time they are needed and
 * written back when the chunk unloads, the world saves, or the plugin disables.
 *
 * Only blocks of tracked types are marked as placed, so normal building
 * doesn't fill chunks with data. Main thread only.
 */
public final class PlacedBlockTracker {

    private final NamespacedKey placedKey;
    private final NamespacedKey creditedKey;

    private final Map<UUID, Map<Long, ChunkFlags>> worlds = new HashMap<>();

    public PlacedBlockTracker(CyberMastery plugin) {
        this.placedKey = new NamespacedKey(plugin, "placed");
        this.creditedKey = new NamespacedKey(plugin, "credited");
    }

    /** Key used to tag falling blocks that came from a placed block. */
    public NamespacedKey placedKey() {
        return placedKey;
    }

    // =============================================================
    // Placed blocks
    // =============================================================

    public boolean isPlaced(Block block) {
        return flags(block).placed.contains(pack(block));
    }

    public void markPlaced(Block block) {
        ChunkFlags flags = flags(block);
        if (flags.placed.add(pack(block))) {
            flags.dirty = true;
        }
    }

    public void unmarkPlaced(Block block) {
        ChunkFlags flags = flags(block);
        if (flags.placed.remove(pack(block))) {
            flags.dirty = true;
        }
    }

    /** Pistons: moves the placed flags of the moved blocks one step in the direction. */
    public void move(List<Block> blocks, BlockFace direction) {
        List<Block> flagged = new ArrayList<>();

        for (Block block : blocks) {
            if (isPlaced(block)) {
                flagged.add(block);
            }
        }

        // Remove every old flag first, so blocks in a row don't overwrite each other.
        for (Block block : flagged) {
            unmarkPlaced(block);
        }

        for (Block block : flagged) {
            markPlaced(block.getRelative(direction));
        }
    }

    // =============================================================
    // Credited positions (placement points are given once per position, ever)
    // =============================================================

    public boolean isCredited(Block block) {
        return flags(block).credited.contains(pack(block));
    }

    public void markCredited(Block block) {
        ChunkFlags flags = flags(block);
        if (flags.credited.add(pack(block))) {
            flags.dirty = true;
        }
    }

    // =============================================================
    // Saving
    // =============================================================

    /** ChunkUnloadEvent: write this chunk's sets and forget them. */
    public void unload(Chunk chunk) {
        Map<Long, ChunkFlags> chunks = worlds.get(chunk.getWorld().getUID());
        if (chunks == null) {
            return;
        }

        ChunkFlags flags = chunks.remove(chunkKey(chunk.getX(), chunk.getZ()));
        if (flags != null && flags.dirty) {
            write(chunk.getPersistentDataContainer(), flags);
        }
    }

    /** WorldSaveEvent: write every changed chunk of the world. */
    public void flushWorld(World world) {
        Map<Long, ChunkFlags> chunks = worlds.get(world.getUID());
        if (chunks == null) {
            return;
        }

        Iterator<Map.Entry<Long, ChunkFlags>> iterator = chunks.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Long, ChunkFlags> entry = iterator.next();
            ChunkFlags flags = entry.getValue();

            int chunkX = (int) (entry.getKey() & 0xFFFFFFFFL);
            int chunkZ = (int) (entry.getKey() >>> 32);

            if (!world.isChunkLoaded(chunkX, chunkZ)) {
                iterator.remove();
                continue;
            }

            if (flags.dirty) {
                write(world.getChunkAt(chunkX, chunkZ).getPersistentDataContainer(), flags);
            }
        }
    }

    /** onDisable: write everything. */
    public void flushAll(Iterable<World> loadedWorlds) {
        for (World world : loadedWorlds) {
            flushWorld(world);
        }
        worlds.clear();
    }

    // =============================================================
    // Internals
    // =============================================================

    private ChunkFlags flags(Block block) {
        World world = block.getWorld();
        int chunkX = block.getX() >> 4;
        int chunkZ = block.getZ() >> 4;

        Map<Long, ChunkFlags> chunks = worlds.computeIfAbsent(world.getUID(), uuid -> new HashMap<>());
        long key = chunkKey(chunkX, chunkZ);

        ChunkFlags flags = chunks.get(key);
        if (flags == null) {
            PersistentDataContainer pdc = world.getChunkAt(chunkX, chunkZ).getPersistentDataContainer();
            flags = new ChunkFlags(
                    toSet(pdc.get(placedKey, PersistentDataType.INTEGER_ARRAY)),
                    toSet(pdc.get(creditedKey, PersistentDataType.INTEGER_ARRAY))
            );
            chunks.put(key, flags);
        }
        return flags;
    }

    private void write(PersistentDataContainer pdc, ChunkFlags flags) {
        writeSet(pdc, placedKey, flags.placed);
        writeSet(pdc, creditedKey, flags.credited);
        flags.dirty = false;
    }

    private static void writeSet(PersistentDataContainer pdc, NamespacedKey key, Set<Integer> set) {
        if (set.isEmpty()) {
            pdc.remove(key);
            return;
        }
        int[] array = new int[set.size()];
        int index = 0;
        for (int value : set) {
            array[index++] = value;
        }
        pdc.set(key, PersistentDataType.INTEGER_ARRAY, array);
    }

    private static Set<Integer> toSet(int[] array) {
        Set<Integer> set = new HashSet<>();
        if (array != null) {
            for (int value : array) {
                set.add(value);
            }
        }
        return set;
    }

    private static long chunkKey(int chunkX, int chunkZ) {
        return ((long) chunkZ << 32) | (chunkX & 0xFFFFFFFFL);
    }

    /** y (offset by 2048) in the high bits, then x and z inside the chunk. */
    private static int pack(Block block) {
        return ((block.getY() + 2048) << 8) | ((block.getX() & 15) << 4) | (block.getZ() & 15);
    }

    private static final class ChunkFlags {
        private final Set<Integer> placed;
        private final Set<Integer> credited;
        private boolean dirty;

        private ChunkFlags(Set<Integer> placed, Set<Integer> credited) {
            this.placed = placed;
            this.credited = credited;
        }
    }
}
