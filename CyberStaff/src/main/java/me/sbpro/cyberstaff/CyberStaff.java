package me.sbpro.cyberstaff;

import me.sbpro.cyberstaff.commands.OtherCommand;
import me.sbpro.cyberstaff.commands.chatlock.ChatLockCommand;
import me.sbpro.cyberstaff.commands.chatlock.ChatLockListener;
import me.sbpro.cyberstaff.commands.clearchat.ClearChatCommand;
import me.sbpro.cyberstaff.commands.clearchat.ClearChatListener;
import me.sbpro.cyberstaff.commands.sign.SignCommand;
import me.sbpro.cyberstaff.commands.staffguide.StaffGuideCommand;
import me.sbpro.cyberstaff.commands.staffguide.StaffGuideListener;
import me.sbpro.cyberstaff.config.PunishmentConfig;
import me.sbpro.cyberstaff.commands.CyberStaffCommand;
import me.sbpro.cyberstaff.database.DatabaseManager;
import me.sbpro.cyberstaff.database.OffenceRepository;
import me.sbpro.cyberstaff.database.PlayerRepository;
import me.sbpro.cyberstaff.listeners.PlayerIdentityListener;
import me.sbpro.cyberstaff.listeners.PunishmentChatListener;
import me.sbpro.cyberstaff.listeners.PunishmentLoginListener;
import me.sbpro.cyberstaff.offend.OffenceCommand;
import me.sbpro.cyberstaff.offend.OffendCommand;
import me.sbpro.cyberstaff.punishment.PunishmentCalculator;
import me.sbpro.cyberstaff.punishment.PunishmentEnforcementManager;
import me.sbpro.cyberstaff.punishment.PunishmentManager;
import me.sbpro.cyberstaff.reason.ReasonManager;
import me.sbpro.cyberstaff.staffchat.StaffChatCommand;
import me.sbpro.cyberstaff.staffchat.StaffChatListener;
import me.sbpro.cyberstaff.staffchat.StaffChatManager;
import me.sbpro.cyberstaff.sus.SusCommand;
import me.sbpro.cyberstaff.sus.SusManager;
import me.sbpro.cyberstaff.sus.SusMenuListener;
import me.sbpro.cyberstaff.sus.SusRepository;
import org.bukkit.plugin.java.JavaPlugin;

public final class CyberStaff extends JavaPlugin {

    private DatabaseManager databaseManager;
    private OffenceRepository offenceRepository;
    private PunishmentConfig punishmentConfig;
    private ReasonManager reasonManager;
    private PunishmentCalculator punishmentCalculator;
    private PunishmentManager punishmentManager;
    private PunishmentEnforcementManager enforcementManager;
    private PlayerRepository playerRepository;

    private SusRepository susRepository;
    private SusManager susManager;
    private StaffChatManager staffChatManager;

    @Override
    public void onEnable() {

        saveDefaultConfig();
        saveResource("punishments.yml", false);

        /*
         * ============================================================
         *                    CONFIGURATION
         * ============================================================
         */

        this.punishmentConfig =
                new PunishmentConfig(this);

        this.reasonManager =
                new ReasonManager(
                        this,
                        punishmentConfig
                );

        this.punishmentCalculator =
                new PunishmentCalculator();

        /*
         * ============================================================
         *                       DATABASE
         * ============================================================
         */

        this.databaseManager =
                new DatabaseManager(this);

        if (!databaseManager.start()) {

            getLogger().severe(
                    "Database startup failed. Disabling CyberStaff."
            );

            getServer()
                    .getPluginManager()
                    .disablePlugin(this);

            return;
        }

        /*
         * ============================================================
         *                    OFFENCE DATABASE
         * ============================================================
         */

        this.offenceRepository =
                new OffenceRepository(
                        databaseManager
                );

        if (!offenceRepository.createTables()) {

            getLogger().severe(
                    "Database schema creation failed. Disabling CyberStaff."
            );

            getServer()
                    .getPluginManager()
                    .disablePlugin(this);

            return;
        }

        /*
         * ============================================================
         *                    PLAYER DATABASE
         * ============================================================
         */

        this.playerRepository =
                new PlayerRepository(
                        databaseManager
                );

        if (!playerRepository.createTable()) {

            getLogger().severe(
                    "Player database table creation failed. Disabling CyberStaff."
            );

            getServer()
                    .getPluginManager()
                    .disablePlugin(this);

            return;
        }

        /*
         * ============================================================
         *                         SUS SYSTEM
         * ============================================================
         */

        this.susRepository =
                new SusRepository(
                        databaseManager
                );

        if (!susRepository.createTable()) {

            getLogger().severe(
                    "SUS database table creation failed. Disabling CyberStaff."
            );

            getServer()
                    .getPluginManager()
                    .disablePlugin(this);

            return;
        }

        this.susManager =
                new SusManager(
                        this,
                        susRepository
                );

        /*
         * ============================================================
         *                       STAFF CHAT
         * ============================================================
         */

        // Staff Chat

        this.staffChatManager =
                new StaffChatManager(this);

        staffChatManager.start();

        StaffChatCommand staffChatCommand =
                new StaffChatCommand(
                        staffChatManager
                );

        getCommand("staffchat")
                .setExecutor(staffChatCommand);

        getCommand("staffchat")
                .setTabCompleter(staffChatCommand);

        getServer()
                .getPluginManager()
                .registerEvents(
                        new StaffChatListener(
                                staffChatManager
                        ),
                        this
                );

        /*
         * ============================================================
         *                    PUNISHMENT SYSTEM
         * ============================================================
         */

        this.punishmentManager =
                new PunishmentManager(
                        this,
                        offenceRepository,
                        punishmentConfig,
                        reasonManager,
                        punishmentCalculator
                );

        OffendCommand offendCommand =
                new OffendCommand(this);

        OffenceCommand offenceCommand =
                new OffenceCommand(this);

        SusCommand susCommand =
                new SusCommand(this);

        /*
         * ============================================================
         *                      /OFFEND
         * ============================================================
         */

        getCommand("cyberstaff")
                .setExecutor(new CyberStaffCommand(this));

        getCommand("offend")
                .setExecutor(offendCommand);

        getCommand("offend")
                .setTabCompleter(offendCommand);

        /*
         * ============================================================
         *                      /OFFENCE
         * ============================================================
         */

        getCommand("offence")
                .setExecutor(offenceCommand);

        getCommand("offence")
                .setTabCompleter(offenceCommand);

        /*
         * ============================================================
         *                        /SUS
         * ============================================================
         */

        getCommand("sus")
                .setExecutor(susCommand);

        getCommand("sus")
                .setTabCompleter(susCommand);

        /*
         * ============================================================
         *                 PUNISHMENT ENFORCEMENT
         * ============================================================
         */

        this.enforcementManager =
                new PunishmentEnforcementManager(this);

        getServer()
                .getPluginManager()
                .registerEvents(
                        new PunishmentLoginListener(
                                this,
                                enforcementManager
                        ),
                        this
                );

        getServer()
                .getPluginManager()
                .registerEvents(
                        new PunishmentChatListener(
                                this,
                                enforcementManager
                        ),
                        this
                );

        /*
         * ============================================================
         *                    PLAYER IDENTITY
         * ============================================================
         */

        getServer()
                .getPluginManager()
                .registerEvents(
                        new PlayerIdentityListener(this),
                        this
                );

        /*
         * ============================================================
         *                       SUS GUI
         * ============================================================
         */

        getServer()
                .getPluginManager()
                .registerEvents(
                        new SusMenuListener(
                                this,
                                susManager
                        ),
                        this
                );

        /*
         * ============================================================
         *                      START SYSTEMS
         * ============================================================
         */

        enforcementManager.start();
        susManager.start();

        /*
         * ============================================================
         *                    OTHER COMMANDS
         * ============================================================
         */

        OtherCommand otherCommand =
                new OtherCommand(this);

        getCommand("ban")
                .setExecutor(otherCommand);

        getCommand("mute")
                .setExecutor(otherCommand);

        getCommand("warn")
                .setExecutor(otherCommand);

        getCommand("punish")
                .setExecutor(otherCommand);

        /*
         * ============================================================
         *                      CHAT LOCK
         * ============================================================
         */

        getCommand("chatlock")
                .setExecutor(
                        new ChatLockCommand()
                );

        getServer()
                .getPluginManager()
                .registerEvents(
                        new ChatLockListener(),
                        this
                );

        /*
         * ============================================================
         *                      CLEAR CHAT
         * ============================================================
         */

        ClearChatCommand clearChatCommand =
                new ClearChatCommand();

        getCommand("clearchat")
                .setExecutor(clearChatCommand);

        getServer()
                .getPluginManager()
                .registerEvents(
                        new ClearChatListener(),
                        this
                );

        getCommand("sign").setExecutor(new SignCommand());

        getLogger().info(
                "CyberStaff enabled."
        );

        getCommand("staffguide").setExecutor(new StaffGuideCommand());
        getServer().getPluginManager().registerEvents(new StaffGuideListener(), this);
    }

    @Override
    public void onDisable() {

        if (enforcementManager != null) {
            enforcementManager.shutdown();
        }

        if (susManager != null) {
            susManager.shutdown();
        }

        if (offenceRepository != null) {
            offenceRepository.shutdown();
        }

        if (susRepository != null) {
            susRepository.shutdown();
        }

        if (databaseManager != null) {
            databaseManager.close();
        }

        if (playerRepository != null) {
            playerRepository.shutdown();
        }

        if (staffChatManager != null) {
            staffChatManager.stop();
        }
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public OffenceRepository getOffenceRepository() {
        return offenceRepository;
    }

    public PunishmentConfig getPunishmentConfig() {
        return punishmentConfig;
    }

    public ReasonManager getReasonManager() {
        return reasonManager;
    }

    public PunishmentCalculator getPunishmentCalculator() {
        return punishmentCalculator;
    }

    public PunishmentManager getPunishmentManager() {
        return punishmentManager;
    }

    public PunishmentEnforcementManager getEnforcementManager() {
        return enforcementManager;
    }

    public PlayerRepository getPlayerRepository() {
        return playerRepository;
    }

    public SusRepository getSusRepository() {
        return susRepository;
    }

    public SusManager getSusManager() {
        return susManager;
    }

    public StaffChatManager getStaffChatManager() {
        return staffChatManager;
    }
}