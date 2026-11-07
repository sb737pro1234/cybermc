package me.sbpro.cybermastery.service;

import me.sbpro.cybermastery.CyberMastery;
import me.sbpro.cybermastery.Messages;
import me.sbpro.cybermastery.Settings;
import me.sbpro.cybermastery.data.PlayerData;
import me.sbpro.cybermastery.data.TotalsCache;
import me.sbpro.cybermastery.event.MasteryLevelUpEvent;
import me.sbpro.cybermastery.model.Category;
import me.sbpro.cybermastery.model.Group;
import me.sbpro.cybermastery.model.LevelCurve;
import me.sbpro.cybermastery.model.MasteryType;
import me.sbpro.cybermastery.model.TypeRegistry;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Points, levels and reward claiming. Main thread only.
 */
public final class MasteryService {

    private static final long HOUR_MILLIS = 60L * 60L * 1000L;

    private final CyberMastery plugin;

    /** Hourly placement windows: player -> type id -> {window start millis, units earned}. */
    private final Map<UUID, Map<String, long[]>> placementWindows = new HashMap<>();

    public MasteryService(CyberMastery plugin) {
        this.plugin = plugin;
    }

    private TypeRegistry registry() {
        return plugin.getRegistry();
    }

    // =============================================================
    // Earning
    // =============================================================

    /** False for spectators, creative players (if configured) and players whose data isn't loaded. */
    public boolean canEarn(Player player) {
        if (player.getGameMode() == GameMode.SPECTATOR) {
            return false;
        }
        if (player.getGameMode() == GameMode.CREATIVE && plugin.getSettings().ignoreCreative()) {
            return false;
        }
        return plugin.getDataManager().get(player.getUniqueId()) != null;
    }

    /** Gives a normal action's points (mining, killing, harvesting, catching) a number of times. */
    public void awardActions(Player player, MasteryType type, int actions) {
        if (actions > 0) {
            award(player, type, type.actionUnits() * actions);
        }
    }

    /** Adds units (quarter-points) and handles any level-up. */
    public void award(Player player, MasteryType type, long units) {
        if (units <= 0) {
            return;
        }

        PlayerData data = plugin.getDataManager().get(player.getUniqueId());
        if (data == null) {
            return;
        }

        LevelCurve curve = registry().curve(type.category());

        int oldLevel = curve.levelForUnits(data.units(type.id()));
        data.addUnits(type.id(), units);
        int newLevel = curve.levelForUnits(data.units(type.id()));

        if (newLevel > oldLevel) {
            onLevelUp(player, data, type, oldLevel, newLevel);
        }
    }

    /**
     * Units a placement of this type is worth right now, after the hourly cap.
     * Records the units against the cap, so only call it when the units will be given.
     * Returns 0 when placing gives nothing.
     */
    public long takePlacementUnits(Player player, MasteryType type) {
        Settings settings = plugin.getSettings();

        if (!settings.placementEnabled() || type.category() != Category.BLOCKS) {
            return 0;
        }

        long base = type.actionUnits() * settings.placementPercent() / 100;
        if (base <= 0) {
            return 0;
        }

        long cap = settings.hourlyCap() * LevelCurve.UNITS_PER_POINT;
        if (cap <= 0) {
            return base;
        }

        long now = System.currentTimeMillis();
        Map<String, long[]> windows = placementWindows.computeIfAbsent(player.getUniqueId(), uuid -> new HashMap<>());
        long[] window = windows.get(type.id());

        if (window == null || now - window[0] >= HOUR_MILLIS) {
            window = new long[]{now, 0};
            windows.put(type.id(), window);
        }

        long allowed = Math.min(base, cap - window[1]);
        if (allowed <= 0) {
            return 0;
        }

        window[1] += allowed;
        return allowed;
    }

    /** How much placing this type counts, in percent of a mined block (for lore). 0 if placing gives nothing. */
    public int placementPercent(MasteryType type) {
        Settings settings = plugin.getSettings();
        if (!settings.placementEnabled() || type.category() != Category.BLOCKS) {
            return 0;
        }
        return settings.placementPercent();
    }

    public void forget(UUID uuid) {
        placementWindows.remove(uuid);
    }

    private void onLevelUp(Player player, PlayerData data, MasteryType type, int oldLevel, int newLevel) {
        refreshTotals(data);

        Bukkit.getPluginManager().callEvent(new MasteryLevelUpEvent(player, type, oldLevel, newLevel));

        // Several level-ups from one action only show the highest.
        player.sendActionBar(Messages.levelUpActionBar(type, newLevel));

        boolean maxed = newLevel >= LevelCurve.MAX_LEVEL;
        boolean milestone = newLevel / 5 > oldLevel / 5;

        if (maxed) {
            player.showTitle(Messages.maxLevelTitle(type));
            player.playSound(player.getLocation(), Messages.MAX_LEVEL_SOUND, Messages.SOUND_VOLUME, Messages.SOUND_PITCH);

            if (plugin.getSettings().broadcastMaxLevel()) {
                Bukkit.broadcast(Messages.maxLevelBroadcast(player.getName(), type));
            } else {
                player.sendMessage(Messages.maxLevelBroadcast(player.getName(), type));
            }
            return;
        }

        if (milestone) {
            player.showTitle(Messages.milestoneTitle(type, newLevel));
            player.playSound(player.getLocation(), Messages.LEVEL_UP_SOUND, Messages.SOUND_VOLUME, Messages.SOUND_PITCH);
            player.sendMessage(Messages.milestoneChat(type, newLevel));
            return;
        }

        // Normal level: action bar (above) and a small sound.
        player.playSound(player.getLocation(), Messages.SMALL_LEVEL_SOUND, Messages.SOUND_VOLUME, Messages.SMALL_LEVEL_PITCH);
    }

    // =============================================================
    // Admin
    // =============================================================

    /**
     * Admin: sets a type to exactly the start of a level (no level-up feedback).
     * Claims above the new level are forgotten so they can be claimed again later.
     */
    public void setLevel(PlayerData data, MasteryType type, int level) {
        int clamped = Math.max(0, Math.min(level, LevelCurve.MAX_LEVEL));
        long units = registry().curve(type.category()).totalFor(clamped) * LevelCurve.UNITS_PER_POINT;

        data.setUnits(type.id(), units);
        data.clearClaimedAbove(type.id(), clamped);
        refreshTotals(data);
    }

    /**
     * Admin: gives whole levels through the normal path (action bar, titles, sounds, broadcast).
     * Lands exactly on the start of the new level. Returns false if the type is already max level.
     */
    public boolean giveLevels(Player player, MasteryType type, int levels) {
        PlayerData data = plugin.getDataManager().get(player.getUniqueId());
        if (data == null) {
            return false;
        }

        LevelCurve curve = registry().curve(type.category());
        long units = data.units(type.id());
        int current = curve.levelForUnits(units);

        if (current >= LevelCurve.MAX_LEVEL) {
            return false;
        }

        int target = Math.min(LevelCurve.MAX_LEVEL, current + Math.max(1, levels));
        award(player, type, curve.unitsToLevel(units, target));
        return true;
    }

    /** Admin: resets one type, or every type when type is null. */
    public void reset(PlayerData data, MasteryType type) {
        if (type == null) {
            data.resetAll();
        } else {
            data.reset(type.id());
        }
        refreshTotals(data);
    }

    // =============================================================
    // Levels
    // =============================================================

    public long units(PlayerData data, MasteryType type) {
        return data.units(type.id());
    }

    public int level(PlayerData data, MasteryType type) {
        return registry().curve(type.category()).levelForUnits(data.units(type.id()));
    }

    public long levelSum(PlayerData data, Collection<MasteryType> types) {
        long sum = 0;
        for (MasteryType type : types) {
            sum += level(data, type);
        }
        return sum;
    }

    public long levelSum(PlayerData data, Category category) {
        return levelSum(data, registry().types(category));
    }

    public long levelSum(PlayerData data, Group group) {
        return levelSum(data, group.types());
    }

    /** Every type level added up. */
    public long totalLevels(PlayerData data) {
        long total = 0;
        for (Category category : Category.values()) {
            total += levelSum(data, category);
        }
        return total;
    }

    public TotalsCache.Totals totals(PlayerData data) {
        long[] levels = new long[Category.values().length];
        long total = 0;

        for (Category category : Category.values()) {
            long sum = levelSum(data, category);
            levels[category.ordinal()] = sum;
            total += sum;
        }

        return new TotalsCache.Totals(data.name(), total, levels);
    }

    public void refreshTotals(PlayerData data) {
        plugin.getTotals().put(data.uuid(), totals(data));
    }

    // =============================================================
    // Rewards
    // =============================================================

    /** Levels reached but not claimed yet. */
    public int ready(PlayerData data, MasteryType type) {
        int level = level(data, type);
        int ready = 0;
        for (int n = 1; n <= level; n++) {
            if (!data.isClaimed(type.id(), n)) {
                ready++;
            }
        }
        return ready;
    }

    public int ready(PlayerData data, Collection<MasteryType> types) {
        int ready = 0;
        for (MasteryType type : types) {
            ready += ready(data, type);
        }
        return ready;
    }

    public int readyAll(PlayerData data) {
        return ready(data, registry().allTypes());
    }

    /** Claims one level's reward. False if it isn't reached or was already claimed. */
    public boolean claim(Player player, MasteryType type, int level) {
        PlayerData data = plugin.getDataManager().get(player.getUniqueId());

        if (data == null
                || level < 1
                || level > level(data, type)
                || data.isClaimed(type.id(), level)) {
            return false;
        }

        data.setClaimed(type.id(), level);
        plugin.getRewardHook().onClaim(player, type, level);
        return true;
    }

    /** Claims every ready reward of the given types. Returns how many were claimed. */
    public int claimAll(Player player, Collection<MasteryType> types) {
        PlayerData data = plugin.getDataManager().get(player.getUniqueId());
        if (data == null) {
            return 0;
        }

        int claimed = 0;

        for (MasteryType type : types) {
            int level = level(data, type);
            for (int n = 1; n <= level; n++) {
                if (claim(player, type, n)) {
                    claimed++;
                }
            }
        }

        return claimed;
    }
}
