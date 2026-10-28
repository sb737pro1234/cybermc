package me.sbpro.cybercombat;

import me.sbpro.cybercombat.listeners.CombatCommandListener;
import me.sbpro.cybercombat.listeners.CombatDeathListener;
import me.sbpro.cybercombat.listeners.CombatJoinListener;
import me.sbpro.cybercombat.listeners.CombatListener;
import me.sbpro.cybercombat.listeners.CombatQuitListener;
import me.sbpro.cybercombat.manager.CombatManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CyberCombat extends JavaPlugin {

    private CombatManager combatManager;

    @Override
    public void onEnable() {
        combatManager = new CombatManager(this);

        getServer().getPluginManager().registerEvents(
                new CombatListener(combatManager),
                this
        );

        getServer().getPluginManager().registerEvents(
                new CombatCommandListener(combatManager),
                this
        );

        getServer().getPluginManager().registerEvents(
                new CombatQuitListener(combatManager),
                this
        );

        getServer().getPluginManager().registerEvents(
                new CombatJoinListener(combatManager),
                this
        );

        getServer().getPluginManager().registerEvents(
                new CombatDeathListener(combatManager),
                this
        );

        combatManager.startTimer();

        getLogger().info("CyberCombat has been enabled!");
    }

    @Override
    public void onDisable() {
        if (combatManager != null) {
            combatManager.stopTimer();
        }

        getLogger().info("CyberCombat has been disabled!");
    }

    public CombatManager getCombatManager() {
        return combatManager;
    }
}