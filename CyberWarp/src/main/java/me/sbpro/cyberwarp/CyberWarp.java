package me.sbpro.cyberwarp;

import me.sbpro.cyberwarp.commands.crates.CratesCommand;
import me.sbpro.cyberwarp.commands.crates.SetCratesCommand;
import me.sbpro.cyberwarp.commands.spawn.SetSpawnCommand;
import me.sbpro.cyberwarp.commands.spawn.SpawnCommand;
import me.sbpro.cyberwarp.commands.warzone.WarzoneCommand;
import me.sbpro.cyberwarp.commands.warzone.SetWarzoneCommand;
import me.sbpro.cyberwarp.warp.WarpHelper;
import org.bukkit.plugin.java.JavaPlugin;

public final class CyberWarp extends JavaPlugin {

    private WarpHelper warpHelper;

    @Override
    public void onEnable() {

        getLogger().info("Cyber Warp » Plugin Starting.");

        saveDefaultConfig();

        // Warp Helper
        warpHelper = new WarpHelper(this);

        // Teleport Commands
        getCommand("spawn").setExecutor(new SpawnCommand(warpHelper));
        getCommand("warzone").setExecutor(new WarzoneCommand(warpHelper));
        getCommand("crates").setExecutor(new CratesCommand(warpHelper));

        // Set Warp Commands
        getCommand("setspawn").setExecutor(new SetSpawnCommand(warpHelper));
        getCommand("setwarzone").setExecutor(new SetWarzoneCommand(warpHelper));
        getCommand("setcrates").setExecutor(new SetCratesCommand(warpHelper));

        // Listeners
        getServer().getPluginManager().registerEvents(warpHelper, this);

        getLogger().info("Cyber Warp » Plugin Enabled.");
    }

    @Override
    public void onDisable() {

        if (warpHelper != null) {
            warpHelper.shutdown();
        }

        getLogger().info("Cyber Warp » Plugin Disabled.");
    }

    public WarpHelper getWarpHelper() {
        return warpHelper;
    }
}