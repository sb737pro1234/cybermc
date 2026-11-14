package me.sbpro.cybervoidgen.generation;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public final class VoidChunkGenerator extends ChunkGenerator {

    // Spawn platform: a single bedrock block at 0, 0, 0. Players spawn on top of it.
    public static final int SPAWN_X = 0;
    public static final int SPAWN_Y = 0;
    public static final int SPAWN_Z = 0;
    public static final Material SPAWN_BLOCK = Material.BEDROCK;

    // Whole world is jungle (nice bright grass and leaves).
    private static final Biome BIOME = Biome.JUNGLE;
    private static final BiomeProvider BIOME_PROVIDER = new JungleBiomeProvider();

    public static Location spawnLocation(World world) {
        return new Location(world, SPAWN_X + 0.5, SPAWN_Y + 1.0, SPAWN_Z + 0.5);
    }

    @Override
    public void generateNoise(@NotNull WorldInfo worldInfo, @NotNull Random random,
                              int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
        // No terrain. Only the chunk containing 0, 0 gets the spawn block.
        if (chunkX == SPAWN_X >> 4 && chunkZ == SPAWN_Z >> 4) {
            chunkData.setBlock(SPAWN_X & 15, SPAWN_Y, SPAWN_Z & 15, SPAWN_BLOCK);
        }
    }

    @Override
    public @NotNull BiomeProvider getDefaultBiomeProvider(@NotNull WorldInfo worldInfo) {
        return BIOME_PROVIDER;
    }

    // Only used when the world is first created; after that the saved spawn
    // (e.g. one changed with /setworldspawn) is kept.
    @Override
    public @NotNull Location getFixedSpawnLocation(@NotNull World world, @NotNull Random random) {
        return spawnLocation(world);
    }

    @Override
    public boolean shouldGenerateNoise() {
        return false;
    }

    @Override
    public boolean shouldGenerateSurface() {
        return false;
    }

    @Override
    public boolean shouldGenerateCaves() {
        return false;
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return false;
    }

    @Override
    public boolean shouldGenerateMobs() {
        return false;
    }

    @Override
    public boolean shouldGenerateStructures() {
        return false;
    }

    private static final class JungleBiomeProvider extends BiomeProvider {

        @Override
        public @NotNull Biome getBiome(@NotNull WorldInfo worldInfo, int x, int y, int z) {
            return BIOME;
        }

        @Override
        public @NotNull List<Biome> getBiomes(@NotNull WorldInfo worldInfo) {
            return List.of(BIOME);
        }
    }
}
