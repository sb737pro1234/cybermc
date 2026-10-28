package me.sbpro.cyberstatistics;

import me.sbpro.cyberstatistics.stats.StatisticsCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class CyberStatistics extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("statistics").setExecutor(new StatisticsCommand(this));
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
