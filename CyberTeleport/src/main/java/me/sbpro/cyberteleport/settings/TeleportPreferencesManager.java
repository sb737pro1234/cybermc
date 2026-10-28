package me.sbpro.cyberteleport.settings;

import me.sbpro.cyberteleport.message.Messages;
import me.sbpro.cyberteleport.request.TeleportRequestType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Per-player teleport settings. They are saved in {@code preferences.yml} in the plugin data folder
 * so they survive restarts.
 *
 * <p>There are three independent settings:
 * <ul>
 *     <li><b>TPA toggle</b> - blocks requests asking <i>you</i> to teleport <i>to the requester</i>
 *     ({@link TeleportRequestType#TPAHERE} requests).</li>
 *     <li><b>TPAHere toggle</b> - blocks requests asking <i>the requester</i> to teleport <i>to you</i>
 *     ({@link TeleportRequestType#TPA} requests).</li>
 *     <li><b>TPAuto</b> - automatically accepts requests where another player teleports to you
 *     ({@link TeleportRequestType#TPA} requests), unless the TPAHere toggle blocks them.</li>
 * </ul>
 */
public final class TeleportPreferencesManager {

    private final JavaPlugin plugin;
    private final File file;

    private final Set<UUID> tpaDisabled = new HashSet<>();
    private final Set<UUID> tpaHereDisabled = new HashSet<>();
    private final Set<UUID> autoAcceptTpa = new HashSet<>();

    private BukkitTask actionBarTask;

    public TeleportPreferencesManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "preferences.yml");

        load();
        startActionBarTask();
    }

    // ---------------------------------------------------------------
    // TPA toggle
    // ---------------------------------------------------------------

    /** @return true when the player has blocked requests asking them to teleport to the requester */
    public boolean isTpaDisabled(Player player) {
        return tpaDisabled.contains(player.getUniqueId());
    }

    /** @return the new "blocked" state */
    public boolean toggleTpa(Player player) {
        return toggle(tpaDisabled, player.getUniqueId());
    }

    // ---------------------------------------------------------------
    // TPAHere toggle
    // ---------------------------------------------------------------

    /** @return true when the player has blocked requests asking the requester to teleport to them */
    public boolean isTpaHereDisabled(Player player) {
        return tpaHereDisabled.contains(player.getUniqueId());
    }

    /** @return the new "blocked" state */
    public boolean toggleTpaHere(Player player) {
        return toggle(tpaHereDisabled, player.getUniqueId());
    }

    // ---------------------------------------------------------------
    // Request direction
    // ---------------------------------------------------------------

    /**
     * Checks whether the target currently refuses a request of this type.
     * A {@code TPA} request makes the requester teleport to the target (controlled by the TPAHere toggle);
     * a {@code TPAHERE} request makes the target teleport to the requester (controlled by the TPA toggle).
     */
    public boolean isRequestBlocked(Player target, TeleportRequestType type) {
        return switch (type) {
            case TPA -> isTpaHereDisabled(target);
            case TPAHERE -> isTpaDisabled(target);
        };
    }

    // ---------------------------------------------------------------
    // TPAuto
    // ---------------------------------------------------------------

    public boolean toggleAutoAcceptTpa(Player player) {
        return toggle(autoAcceptTpa, player.getUniqueId());
    }

    public boolean isAutoAcceptTpa(Player player) {
        return autoAcceptTpa.contains(player.getUniqueId());
    }

    public boolean shouldAutoAcceptTpa(Player player) {
        return !isTpaHereDisabled(player) && isAutoAcceptTpa(player);
    }

    // ---------------------------------------------------------------
    // Storage
    // ---------------------------------------------------------------

    private boolean toggle(Set<UUID> set, UUID uuid) {
        boolean enabled = !set.remove(uuid);
        if (enabled) {
            set.add(uuid);
        }
        save();
        return enabled;
    }

    private void load() {
        if (!file.exists()) {
            return;
        }

        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        readUuids(yaml, "tpa-disabled", tpaDisabled);
        readUuids(yaml, "tpahere-disabled", tpaHereDisabled);
        readUuids(yaml, "tpauto", autoAcceptTpa);
    }

    private void readUuids(YamlConfiguration yaml, String path, Set<UUID> target) {
        for (String value : yaml.getStringList(path)) {
            try {
                target.add(UUID.fromString(value));
            } catch (IllegalArgumentException ignored) {
                plugin.getLogger().warning("Ignoring invalid UUID in preferences.yml: " + value);
            }
        }
    }

    private void save() {
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set("tpa-disabled", tpaDisabled.stream().map(UUID::toString).toList());
        yaml.set("tpahere-disabled", tpaHereDisabled.stream().map(UUID::toString).toList());
        yaml.set("tpauto", autoAcceptTpa.stream().map(UUID::toString).toList());

        try {
            plugin.getDataFolder().mkdirs();
            yaml.save(file);
        } catch (IOException exception) {
            plugin.getLogger().severe("Could not save preferences.yml: " + exception.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // TPAuto action bar
    // ---------------------------------------------------------------

    private void startActionBarTask() {
        actionBarTask = plugin.getServer()
                .getScheduler()
                .runTaskTimer(plugin, this::updateActionBars, 0L, 20L);
    }

    private void updateActionBars() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (!isAutoAcceptTpa(player)) {
                continue;
            }

            player.sendActionBar(Messages.TPAUTO_ACTION_BAR);
        }
    }

    public void shutdown() {
        if (actionBarTask != null) {
            actionBarTask.cancel();
            actionBarTask = null;
        }
    }
}
