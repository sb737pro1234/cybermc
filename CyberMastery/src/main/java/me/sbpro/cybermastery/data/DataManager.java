package me.sbpro.cybermastery.data;

import me.sbpro.cybermastery.CyberMastery;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

/**
 * Loads and saves plugins/CyberMastery/data/&lt;uuid&gt;.yml.
 *
 * All file reads and writes run on one IO thread, in order, so a quick
 * rejoin always loads the file after the previous quit's save has finished.
 */
public final class DataManager {

    private static final int OFFLINE_CACHE_SIZE = 64;

    private final CyberMastery plugin;
    private final File folder;
    private final ExecutorService io;

    private final Map<UUID, PlayerData> online = new ConcurrentHashMap<>();

    /** Recently used offline players, for placeholders. */
    private final Map<UUID, PlayerData> offlineCache = Collections.synchronizedMap(
            new LinkedHashMap<>(16, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<UUID, PlayerData> eldest) {
                    return size() > OFFLINE_CACHE_SIZE;
                }
            }
    );

    public DataManager(CyberMastery plugin) {
        this.plugin = plugin;
        this.folder = new File(plugin.getDataFolder(), "data");
        this.io = Executors.newSingleThreadExecutor(runnable -> {
            Thread thread = new Thread(runnable, "CyberMastery-IO");
            thread.setDaemon(true);
            return thread;
        });

        if (!folder.exists() && !folder.mkdirs()) {
            plugin.getLogger().severe("Could not create the data folder!");
        }
    }

    public ExecutorService io() {
        return io;
    }

    // =============================================================
    // Online players
    // =============================================================

    /** Data of an online player, or null if not loaded. */
    public PlayerData get(UUID uuid) {
        return online.get(uuid);
    }

    public Collection<PlayerData> online() {
        return online.values();
    }

    /**
     * Loads a player's data on the IO thread and waits for it.
     * Only call this off the main thread (AsyncPlayerPreLoginEvent).
     */
    public void loadBlocking(UUID uuid, String name) {
        try {
            PlayerData data = io.submit(() -> read(uuid, name)).get(10, TimeUnit.SECONDS);
            offlineCache.remove(uuid);
            online.put(uuid, data);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException | TimeoutException exception) {
            plugin.getLogger().log(Level.SEVERE, "Could not load mastery data for " + name, exception);
        }
    }

    /** Main-thread fallback (plugin reloads, or if pre-login loading failed). */
    public PlayerData loadNow(UUID uuid, String name) {
        PlayerData data = online.get(uuid);
        if (data == null) {
            data = read(uuid, name);
            offlineCache.remove(uuid);
            online.put(uuid, data);
        }
        data.setName(name);
        return data;
    }

    /** Saves and forgets an online player (quit). */
    public void unload(UUID uuid) {
        PlayerData data = online.remove(uuid);
        if (data == null) {
            return;
        }
        saveAsync(data);
        offlineCache.put(uuid, data.copy());
    }

    // =============================================================
    // Offline players (placeholders)
    // =============================================================

    /** Data of any player: online, cached, or read from disk. Never null. */
    public PlayerData getAnyone(UUID uuid) {
        PlayerData data = online.get(uuid);
        if (data != null) {
            return data;
        }
        data = offlineCache.get(uuid);
        if (data != null) {
            return data;
        }
        data = read(uuid, null);
        offlineCache.put(uuid, data);
        return data;
    }

    // =============================================================
    // Saving
    // =============================================================

    /** Snapshots on the calling (main) thread, writes on the IO thread. */
    public void saveAsync(PlayerData data) {
        PlayerData snapshot = data.copy();
        data.markClean();
        io.execute(() -> write(snapshot));
    }

    /** Saves every online player whose data changed. */
    public void autosave() {
        for (PlayerData data : online.values()) {
            if (data.isDirty()) {
                saveAsync(data);
            }
        }
    }

    /** onDisable: queue every save and wait for the IO thread to finish. */
    public void shutdown() {
        for (PlayerData data : online.values()) {
            saveAsync(data);
        }

        io.shutdown();

        try {
            if (!io.awaitTermination(15, TimeUnit.SECONDS)) {
                plugin.getLogger().severe("Timed out while saving mastery data!");
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }

    // =============================================================
    // Files
    // =============================================================

    private File file(UUID uuid) {
        return new File(folder, uuid + ".yml");
    }

    private PlayerData read(UUID uuid, String name) {
        File file = file(uuid);
        if (!file.exists()) {
            return new PlayerData(uuid, name);
        }
        return PlayerData.fromYaml(uuid, name, YamlConfiguration.loadConfiguration(file));
    }

    private void write(PlayerData snapshot) {
        File file = file(snapshot.uuid());
        File temp = new File(folder, snapshot.uuid() + ".yml.tmp");

        try {
            Files.writeString(temp.toPath(), snapshot.toYaml().saveToString(), StandardCharsets.UTF_8);
            Files.move(temp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException exception) {
            plugin.getLogger().log(Level.SEVERE, "Could not save mastery data for " + snapshot.uuid(), exception);
        }
    }
}
