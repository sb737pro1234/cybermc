package me.sbpro.cybermastery;

import me.sbpro.cybermastery.command.MasteryCommand;
import me.sbpro.cybermastery.data.DataManager;
import me.sbpro.cybermastery.data.PlayerData;
import me.sbpro.cybermastery.data.TotalsCache;
import me.sbpro.cybermastery.gui.MasteryMenuHolder;
import me.sbpro.cybermastery.listener.BlockListener;
import me.sbpro.cybermastery.listener.FarmingListener;
import me.sbpro.cybermastery.listener.FishListener;
import me.sbpro.cybermastery.listener.KillListener;
import me.sbpro.cybermastery.listener.MasteryMenuListener;
import me.sbpro.cybermastery.listener.PlayerConnectionListener;
import me.sbpro.cybermastery.listener.TrackerListener;
import me.sbpro.cybermastery.model.TypeRegistry;
import me.sbpro.cybermastery.placeholder.MasteryPlaceholderExpansion;
import me.sbpro.cybermastery.reward.NoRewardHook;
import me.sbpro.cybermastery.reward.RewardHook;
import me.sbpro.cybermastery.service.MasteryService;
import me.sbpro.cybermastery.tracking.PlacedBlockTracker;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.Objects;
import java.util.logging.Level;

public final class CyberMastery extends JavaPlugin {

    private Settings settings;
    private TypeRegistry registry;
    private PlacedBlockTracker tracker;
    private DataManager dataManager;
    private TotalsCache totals;
    private MasteryService service;
    private RewardHook rewardHook = new NoRewardHook();
    private BukkitTask autosaveTask;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        File masteryFile = new File(getDataFolder(), "mastery.yml");
        boolean firstRun = !masteryFile.exists();

        if (firstRun) {
            saveResource("mastery.yml", false);
        }

        this.settings = Settings.from(getConfig());
        this.registry = loadRegistry();

        if (firstRun) {
            getLogger().info("First run - mastery.yml created. Types per group:");
            registry.summary().forEach(line -> getLogger().info(line));
        } else {
            getLogger().info("Loaded " + registry.allTypes().size() + " mastery types.");
        }

        this.tracker = new PlacedBlockTracker(this);
        this.dataManager = new DataManager(this);
        this.totals = new TotalsCache(this);
        this.service = new MasteryService(this);

        MasteryCommand masteryCommand = new MasteryCommand(this);

        Objects.requireNonNull(
                getCommand("mastery"),
                "mastery command missing from plugin.yml"
        ).setExecutor(masteryCommand);

        Objects.requireNonNull(
                getCommand("mastery"),
                "mastery command missing from plugin.yml"
        ).setTabCompleter(masteryCommand);

        Objects.requireNonNull(
                getCommand("masterytop"),
                "masterytop command missing from plugin.yml"
        ).setExecutor(masteryCommand);

        Objects.requireNonNull(
                getCommand("masterytop"),
                "masterytop command missing from plugin.yml"
        ).setTabCompleter(masteryCommand);

        FarmingListener farmingListener = new FarmingListener(this);

        Bukkit.getPluginManager().registerEvents(new BlockListener(this, farmingListener), this);
        Bukkit.getPluginManager().registerEvents(farmingListener, this);
        Bukkit.getPluginManager().registerEvents(new TrackerListener(this), this);
        Bukkit.getPluginManager().registerEvents(new KillListener(this), this);
        Bukkit.getPluginManager().registerEvents(new FishListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerConnectionListener(this), this);
        Bukkit.getPluginManager().registerEvents(new MasteryMenuListener(this), this);

        // Players already online (e.g. after a server reload).
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData data = dataManager.loadNow(player.getUniqueId(), player.getName());
            service.refreshTotals(data);
        }

        startAutosave();

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {

            new MasteryPlaceholderExpansion(this).register();

            getLogger().info(
                    "PlaceholderAPI expansion registered: %cybermastery%."
            );
        }

        getLogger().info("CyberMastery enabled.");
    }

    @Override
    public void onDisable() {

        closeMenus();

        if (autosaveTask != null) {
            autosaveTask.cancel();
        }

        if (tracker != null) {
            tracker.flushAll(Bukkit.getWorlds());
        }

        if (dataManager != null) {
            for (PlayerData data : dataManager.online()) {
                service.refreshTotals(data);
            }
            totals.saveAsync(dataManager);
            dataManager.shutdown();
        }

        getLogger().info("CyberMastery disabled.");
    }

    /** /mastery reload: config.yml and mastery.yml. Player progress is kept. */
    public boolean reloadMastery() {
        try {
            reloadConfig();
            this.settings = Settings.from(getConfig());
            this.registry = loadRegistry();

            // Open menus point at the old types, so close them.
            closeMenus();

            startAutosave();

            for (PlayerData data : dataManager.online()) {
                service.refreshTotals(data);
            }

            getLogger().info("Reloaded " + registry.allTypes().size() + " mastery types.");
            return true;
        } catch (Exception exception) {
            getLogger().log(Level.SEVERE, "Could not reload CyberMastery", exception);
            return false;
        }
    }

    private TypeRegistry loadRegistry() {
        File masteryFile = new File(getDataFolder(), "mastery.yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(masteryFile);
        return TypeRegistry.load(yaml.getConfigurationSection("categories"), getLogger());
    }

    private void startAutosave() {
        if (autosaveTask != null) {
            autosaveTask.cancel();
        }

        long interval = settings.autosaveMinutes() * 60L * 20L;

        autosaveTask = Bukkit.getScheduler().runTaskTimer(
                this,
                () -> {
                    dataManager.autosave();
                    totals.saveAsync(dataManager);
                },
                interval,
                interval
        );
    }

    private void closeMenus() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getOpenInventory().getTopInventory().getHolder() instanceof MasteryMenuHolder) {
                player.closeInventory();
            }
        }
    }

    public Settings getSettings() {
        return settings;
    }

    public TypeRegistry getRegistry() {
        return registry;
    }

    public PlacedBlockTracker getTracker() {
        return tracker;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public TotalsCache getTotals() {
        return totals;
    }

    public MasteryService getService() {
        return service;
    }

    public RewardHook getRewardHook() {
        return rewardHook;
    }

    /** Lets another plugin (or a future version) plug in level rewards. */
    public void setRewardHook(RewardHook rewardHook) {
        this.rewardHook = rewardHook == null ? new NoRewardHook() : rewardHook;
    }
}
