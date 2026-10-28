package me.sbpro.cyberprotect;

import me.sbpro.cyberprotect.command.CyberProtectCommand;
import me.sbpro.cyberprotect.listeners.SpawnProtectionListener;
import me.sbpro.cyberprotect.region.RegionManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class CyberProtect extends JavaPlugin {

    private File regionsFile;
    private FileConfiguration regionsConfig;

    private RegionManager regionManager;

    @Override
    public void onEnable() {

        loadRegions();

        regionManager = new RegionManager(this);
        regionManager.loadRegions();

        getServer().getPluginManager().registerEvents(
                new SpawnProtectionListener(this),
                this
        );

        CyberProtectCommand command =
                new CyberProtectCommand(this);

        if (getCommand("cyberprotect") != null) {
            getCommand("cyberprotect").setExecutor(command);
            getCommand("cyberprotect").setTabCompleter(command);
        }

        getLogger().info("CyberProtect enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("CyberProtect disabled.");
    }

    public void loadRegions() {

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        regionsFile = new File(
                getDataFolder(),
                "regions.yml"
        );

        if (!regionsFile.exists()) {
            saveResource("regions.yml", false);
        }

        regionsConfig =
                YamlConfiguration.loadConfiguration(regionsFile);
    }

    public void reloadPlugin() {

        loadRegions();
        regionManager.loadRegions();
    }

    public FileConfiguration getRegionsConfig() {
        return regionsConfig;
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }
}