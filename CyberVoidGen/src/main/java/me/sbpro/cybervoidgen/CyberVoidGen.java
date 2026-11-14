package me.sbpro.cybervoidgen;

import me.sbpro.cybervoidgen.generation.VoidChunkGenerator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

public final class CyberVoidGen extends JavaPlugin {

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, @Nullable String id) {
        // Multiverse-Core resolves this through the plugin name: -g CyberVoidGen
        return new VoidChunkGenerator();
    }
}
