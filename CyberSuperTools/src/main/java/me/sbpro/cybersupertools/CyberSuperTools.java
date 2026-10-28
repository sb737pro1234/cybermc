package me.sbpro.cybersupertools;

import me.sbpro.cybersupertools.commands.SuperToolsCommand;
import me.sbpro.cybersupertools.listeners.SuperToolListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class CyberSuperTools extends JavaPlugin {

    @Override
    public void onEnable() {
        SuperToolsCommand command = new SuperToolsCommand(this);
        getCommand("supertools").setExecutor(command);
        getCommand("supertools").setTabCompleter(command);

        getServer().getPluginManager().registerEvents(new SuperToolListener(this), this);

        getLogger().info("CyberSuperTools enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("CyberSuperTools disabled.");
    }
}
