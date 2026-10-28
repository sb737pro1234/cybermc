package me.sbpro.cyberfastplace;

import me.sbpro.cyberfastplace.commands.FPCommand;
import me.sbpro.cyberfastplace.listeners.FastPlaceListener;
import me.sbpro.cyberfastplace.manager.FastPlaceManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class CyberFastPlace extends JavaPlugin {
    private FastPlaceManager fastPlaceManager;
    private boolean debugMode;

    public void onEnable() {
        this.getLogger().info("CyberFastPlace enabled!");
        this.saveDefaultConfig();
        this.debugMode = this.getConfig().getBoolean("debug", false);
        this.fastPlaceManager = new FastPlaceManager();
        this.getCommand("fp").setExecutor((CommandExecutor) new FPCommand(this.fastPlaceManager));
        this.getServer().getPluginManager().registerEvents((Listener) new FastPlaceListener(this.fastPlaceManager, this), (Plugin) this);
        if (this.debugMode) {
            this.getLogger().info("Debug mode enabled!");
        }
    }

    public void onDisable() {
        this.getLogger().info("CyberFastPlace disabled!");
    }

    public FastPlaceManager getFastPlaceManager() {
        return this.fastPlaceManager;
    }

    public boolean isDebugMode() {
        return this.debugMode;
    }
}