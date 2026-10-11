package me.sbpro.grassggquests.quests;

import me.sbpro.grassggquests.GrassGGQuests;
import me.sbpro.grassggquests.Quests;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

/**
 * Remembers which mining-quest blocks were placed by players so they can be ignored when broken.
 *
 * Positions are stored in each chunk's persistent data container, so the information survives restarts
 * and is removed together with the chunk. Only materials used by BREAK_BLOCK quests are tracked, which
 * keeps the stored data small. A per-chunk in-memory cache avoids rewriting the container on every action.
 */
public class PlacedBlockTracker {
    private record ChunkKey(UUID world, int x, int z) {}

    private static final class Entry {
        final Chunk chunk;
        final Set<Long> positions = new HashSet<>();
        boolean dirty;
        Entry(Chunk chunk) { this.chunk = chunk; }
    }

    private final NamespacedKey key;
    private final Set<Material> tracked = EnumSet.noneOf(Material.class);
    private final Map<ChunkKey, Entry> cache = new HashMap<>();

    public PlacedBlockTracker(GrassGGQuests plugin) {
        this.key = new NamespacedKey(plugin, "placed_blocks");
        for (QuestDefinition quest : Quests.QUESTS.values()) {
            if (quest.getType() != QuestType.BREAK_BLOCK) continue;
            for (String target : quest.getTargets()) {
                Material material = Material.matchMaterial(target);
                if (material != null) tracked.add(material);
            }
        }
    }

    public boolean isTrackedMaterial(Material material) { return tracked.contains(material); }

    /** Marks the block as player-placed. Non-tracked materials clear any stale mark instead. */
    public void mark(Block block, Material type) {
        if (!tracked.contains(type)) { unmark(block); return; }
        Entry entry = entry(block.getChunk());
        if (entry.positions.add(encode(block))) entry.dirty = true;
    }

    public void unmark(Block block) {
        Entry entry = entry(block.getChunk());
        if (entry.positions.remove(encode(block))) entry.dirty = true;
    }

    public boolean isPlaced(Block block) {
        return entry(block.getChunk()).positions.contains(encode(block));
    }

    /** Returns true if the block was player-placed and removes the mark (the block is being broken). */
    public boolean consume(Block block) {
        Entry entry = entry(block.getChunk());
        boolean placed = entry.positions.remove(encode(block));
        if (placed) entry.dirty = true;
        return placed;
    }

    private long encode(Block block) {
        return ((long) (block.getY() + 2048) << 8) | ((block.getX() & 15) << 4) | (block.getZ() & 15);
    }

    private Entry entry(Chunk chunk) {
        ChunkKey chunkKey = new ChunkKey(chunk.getWorld().getUID(), chunk.getX(), chunk.getZ());
        Entry entry = cache.get(chunkKey);
        if (entry != null) return entry;
        entry = new Entry(chunk);
        long[] stored = chunk.getPersistentDataContainer().get(key, PersistentDataType.LONG_ARRAY);
        if (stored != null) for (long value : stored) entry.positions.add(value);
        cache.put(chunkKey, entry);
        return entry;
    }

    private void flush(Entry entry) {
        if (!entry.dirty) return;
        entry.dirty = false;
        PersistentDataContainer container = entry.chunk.getPersistentDataContainer();
        if (entry.positions.isEmpty()) {
            container.remove(key);
            return;
        }
        long[] values = new long[entry.positions.size()];
        int index = 0;
        for (long value : entry.positions) values[index++] = value;
        container.set(key, PersistentDataType.LONG_ARRAY, values);
    }

    /** Writes changed chunks into their persistent data (called periodically so autosaves include them). */
    public void flushDirty() {
        for (Entry entry : cache.values()) flush(entry);
    }

    /** Writes everything; used on shutdown. */
    public void flushAll() { flushDirty(); }

    public void onChunkUnload(Chunk chunk) {
        Entry entry = cache.remove(new ChunkKey(chunk.getWorld().getUID(), chunk.getX(), chunk.getZ()));
        if (entry != null) flush(entry);
    }
}
