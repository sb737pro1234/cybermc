package me.sbpro.cyberteleport;

import me.sbpro.cyberteleport.command.*;
import me.sbpro.cyberteleport.command.tab.PlayerOnlyTabCompleter;
import me.sbpro.cyberteleport.command.tab.RequestPlayerTabCompleter;
import me.sbpro.cyberteleport.command.tab.TeleportTabCompleter;
import me.sbpro.cyberteleport.gui.ConfirmationMenuListener;
import me.sbpro.cyberteleport.listener.TeleportListener;
import me.sbpro.cyberteleport.request.TeleportRequestManager;
import me.sbpro.cyberteleport.settings.TeleportPreferencesManager;
import me.sbpro.cyberteleport.spectate.SpectateManager;
import me.sbpro.cyberteleport.teleport.TeleportManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CyberTeleport extends JavaPlugin {

    private static CyberTeleport instance;

    private TeleportManager teleportManager;
    private TeleportRequestManager teleportRequestManager;
    private TeleportPreferencesManager teleportPreferencesManager;
    private SpectateManager spectateManager;

    @Override
    public void onEnable() {
        instance = this;

        teleportManager = new TeleportManager(this);

        teleportPreferencesManager =
                new TeleportPreferencesManager(this);

        teleportRequestManager =
                new TeleportRequestManager(
                        this,
                        teleportManager,
                        teleportPreferencesManager
                );

        spectateManager = new SpectateManager();

        if (getCommand("tp") != null) {
            getCommand("tp").setExecutor(
                    new TeleportCommand(this)
            );

            getCommand("tp").setTabCompleter(
                    new TeleportTabCompleter()
            );
        }

        if (getCommand("tpall") != null) {
            getCommand("tpall").setExecutor(
                    new TpallCommand(this)
            );
        }

        if (getCommand("tp2p") != null) {
            getCommand("tp2p").setExecutor(
                    new Tp2pCommand(this)
            );

            getCommand("tp2p").setTabCompleter(
                    new PlayerOnlyTabCompleter()
            );
        }

        if (getCommand("tphere") != null) {
            getCommand("tphere").setExecutor(
                    new TphereCommand(this)
            );

            getCommand("tphere").setTabCompleter(
                    new PlayerOnlyTabCompleter()
            );
        }

        if (getCommand("tpa") != null) {
            getCommand("tpa").setExecutor(
                    new TpaCommand(this)
            );

            getCommand("tpa").setTabCompleter(
                    new RequestPlayerTabCompleter()
            );
        }

        if (getCommand("tpahere") != null) {
            getCommand("tpahere").setExecutor(
                    new TpahereCommand(this)
            );

            getCommand("tpahere").setTabCompleter(
                    new RequestPlayerTabCompleter()
            );
        }

        if (getCommand("tpaccept") != null) {
            getCommand("tpaccept").setExecutor(
                    new TpacceptCommand(this)
            );

            getCommand("tpaccept").setTabCompleter(
                    new RequestPlayerTabCompleter()
            );
        }

        if (getCommand("tpdeny") != null) {
            getCommand("tpdeny").setExecutor(
                    new TpdenyCommand(this)
            );
        }

        if (getCommand("tpacancel") != null) {
            getCommand("tpacancel").setExecutor(
                    new TpacancelCommand(this)
            );
        }

        if (getCommand("tpatoggle") != null) {
            getCommand("tpatoggle").setExecutor(
                    new TpatoggleCommand(this)
            );
        }

        if (getCommand("tpaheretoggle") != null) {
            getCommand("tpaheretoggle").setExecutor(
                    new TpaheretoggleCommand(this)
            );
        }

        if (getCommand("tpauto") != null) {
            getCommand("tpauto").setExecutor(
                    new TpautoCommand(this)
            );
        }

        if (getCommand("spectate") != null) {
            getCommand("spectate").setExecutor(
                    new SpectateCommand(this)
            );

            getCommand("spectate").setTabCompleter(
                    new PlayerOnlyTabCompleter()
            );
        }

        getServer().getPluginManager().registerEvents(
                new TeleportListener(
                        teleportManager,
                        teleportRequestManager,
                        teleportPreferencesManager,
                        spectateManager
                ),
                this
        );

        getServer().getPluginManager().registerEvents(
                new ConfirmationMenuListener(),
                this
        );

        getLogger().info(
                "CyberTeleport has been enabled."
        );
    }

    @Override
    public void onDisable() {
        if (teleportRequestManager != null) {
            teleportRequestManager.shutdown();
        }

        if (teleportPreferencesManager != null) {
            teleportPreferencesManager.shutdown();
        }

        if (spectateManager != null) {
            spectateManager.shutdown();
        }

        getLogger().info(
                "CyberTeleport has been disabled."
        );

        instance = null;
    }

    public static CyberTeleport getInstance() {
        return instance;
    }

    public TeleportManager getTeleportManager() {
        return teleportManager;
    }

    public TeleportRequestManager getTeleportRequestManager() {
        return teleportRequestManager;
    }

    public TeleportPreferencesManager
    getTeleportPreferencesManager() {
        return teleportPreferencesManager;
    }

    public SpectateManager getSpectateManager() {
        return spectateManager;
    }
}