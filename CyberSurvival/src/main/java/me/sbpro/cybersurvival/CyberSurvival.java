package me.sbpro.cybersurvival;

// Java Util Imports
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

// Bukkit Imports
import me.sbpro.cybersurvival.commands.playtop.PlayTimeCommand;
import me.sbpro.cybersurvival.commands.playtop.PlayTopCommand;
import me.sbpro.cybersurvival.commands.playtop.PlayTopListener;
import me.sbpro.cybersurvival.commands.ranks.RanksCommand;
import me.sbpro.cybersurvival.commands.ranks.RanksListener;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

// Vault
import net.milkbowl.vault.economy.Economy;

// My Imports
import me.sbpro.cybersurvival.enchants.AutoSmeltEnchant;
import me.sbpro.cybersurvival.settings.*;
import me.sbpro.cybersurvival.commands.*;
import me.sbpro.cybersurvival.commands.baltop.BalTopCommand;
import me.sbpro.cybersurvival.commands.baltop.BalTopListener;
import me.sbpro.cybersurvival.commands.baltop.BalTopMenu;
import me.sbpro.cybersurvival.listeners.HidePlayerListener;
import me.sbpro.cybersurvival.placeholder.SurvivalPlaceholderExpansion;

public final class CyberSurvival extends JavaPlugin implements Listener {

    private static CyberSurvival instance;

    private Economy economy;

    private final Set<UUID> hiddenPlayerUsers = new HashSet<>();

    // Economy
    private Economy setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return null;
        }

        RegisteredServiceProvider<Economy> rsp =
                getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) {
            return null;
        }

        return rsp.getProvider();
    }

    public Economy getEconomy() {
        return economy;
    }

    // Settings Managers
    private SettingsStorage settingsStorage;

    public SettingsStorage getSettingsStorage() {
        return settingsStorage;
    }

    private BossBarManager bossBarManager;

    public BossBarManager getBossBarManager() {
        return bossBarManager;
    }

    private ChatToggleManager chatToggleManager;

    public ChatToggleManager getChatToggleManager() {
        return chatToggleManager;
    }

    private NightVisionManager nightVisionManager;

    public NightVisionManager getNightVisionManager() {
        return nightVisionManager;
    }

    private ScoreboardManager scoreboardManager;

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    private GlowManager glowManager;

    private JoinTitleManager joinTitleManager;

    public JoinTitleManager getJoinTitleManager() {
        return joinTitleManager;
    }

    public GlowManager getGlowManager() {
        return glowManager;
    }

    // Enable
    @Override
    public void onEnable() {

        instance = this;

        getLogger().info("Cyber » Server Starting.");

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        // Commands
        HidePlayerCommand hidePlayerCommand = new HidePlayerCommand(this);
        getCommand("hideplayer").setExecutor(hidePlayerCommand);
        getCommand("hideplayer").setTabCompleter(hidePlayerCommand);

        getCommand("glow").setExecutor(new GlowCommand(this));
        getCommand("settings").setExecutor(new SettingsCommand(this));
        getCommand("chattoggle").setExecutor(new ChatToggleCommand(this));
        getCommand("bossbartoggle").setExecutor(new BossBarToggleCommand(this));

        AutoSmeltEnchant autoSmelt = new AutoSmeltEnchant(this);
        getCommand("customenchant").setExecutor(new CustomEnchantCommand(autoSmelt));

        getCommand("baltop").setExecutor(new BalTopCommand(this));

        // Listeners / Events
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new HidePlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new SettingsListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerSettingsListener(this), this);
        getServer().getPluginManager().registerEvents(new BalTopListener(), this);

        // Economy
        economy = setupEconomy();

        if (economy == null) {
            getLogger().severe("Vault economy not found! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Build the balance leaderboard immediately, then refresh it silently every 5 minutes.
        BalTopMenu.refresh(this);
        getServer().getScheduler().runTaskTimer(
                this,
                () -> BalTopMenu.refresh(this),
                20L * 60L * 5L,
                20L * 60L * 5L
        );

        // Managers
        settingsStorage = new SettingsStorage(this);
        chatToggleManager = new ChatToggleManager(this);
        bossBarManager = new BossBarManager(this);
        scoreboardManager = new ScoreboardManager(this);
        nightVisionManager = new NightVisionManager(this);
        glowManager = new GlowManager(this);
        joinTitleManager = new JoinTitleManager(this);

        getServer().getPluginManager().registerEvents(new RanksListener(), this);
        getCommand("ranks").setExecutor(new RanksCommand());

        getCommand("playtop").setExecutor(new PlayTopCommand(this));
        getServer().getPluginManager().registerEvents(new PlayTopListener(), this);
        getCommand("playtime").setExecutor(new PlayTimeCommand());

        // PlaceholderAPI
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new SurvivalPlaceholderExpansion(this).register();
            getLogger().info("PlaceholderAPI expansion registered: %cybersurvival%.");
        }

        getCommand("reboot").setExecutor(new RebootCommand(this));
         getServer().getPluginManager().registerEvents(new StartupJoinGate(this), this);
    }

    // Disable
    @Override
    public void onDisable() {

        if (bossBarManager != null) {
            bossBarManager.hideAll();
        }

        getLogger().info("Cyber » Server Stopping.");
    }

    // Player Join
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        // Welcome title, every join (can be turned off in /settings > Visuals)
        if (joinTitleManager.isJoinTitleEnabled(player.getUniqueId())) {
            player.showTitle(Messages.joinTitle(player.getName()));
        }

        if (!player.hasPlayedBefore()){


// Teleport first-time players to SurvivalSpawn.
// Delayed so other plugins can finish their join teleport first.
            Bukkit.getScheduler().runTaskLater(this, () -> {

                World world = Bukkit.getWorld("SurvivalSpawn");

                if (world != null && player.isOnline()) {
                    player.teleport(new Location(world, 0.5, 1, 0.5));
                }

            }, 10L);
        }
    }

    // Player Respawn
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {

        Player player = event.getPlayer();

        // If the player has no bed / respawn anchor set,
        // send them to SurvivalSpawn.
        if (player.getRespawnLocation() == null) {

            World world = Bukkit.getWorld("SurvivalSpawn");

            if (world != null) {
                event.setRespawnLocation(
                        new Location(world, 0.5, 1, 0.5)
                );
            }
        }
    }

    // Advancement
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        event.message(null);
    }

    // Instance
    public static CyberSurvival getInstance() {
        return instance;
    }

    // Hidden Players
    public Set<UUID> getHiddenPlayerUsers() {
        return hiddenPlayerUsers;
    }

    public boolean isHidingPlayers(UUID uuid) {
        return hiddenPlayerUsers.contains(uuid);
    }

    public void setHidingPlayers(UUID uuid, boolean hiding) {

        if (hiding) {
            hiddenPlayerUsers.add(uuid);
        } else {
            hiddenPlayerUsers.remove(uuid);
        }
    }
}