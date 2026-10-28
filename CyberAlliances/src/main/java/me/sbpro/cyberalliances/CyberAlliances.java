/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.PluginCommand
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package me.sbpro.cyberalliances;

import me.sbpro.cyberalliances.ChatToggleService;
import me.sbpro.cyberalliances.InviteService;
import me.sbpro.cyberalliances.command.AllianceCommand;
import me.sbpro.cyberalliances.data.AllianceStorage;
import me.sbpro.cyberalliances.gui.AllianceLevelGuiListener;
import me.sbpro.cyberalliances.gui.DeleteAllianceListener;
import me.sbpro.cyberalliances.hook.AlliancePlaceholderExpansion;
import me.sbpro.cyberalliances.listener.AllianceChatListener;
import me.sbpro.cyberalliances.listener.AlliancePlayerListener;
import me.sbpro.cyberalliances.listener.AllianceXpListener;
import me.sbpro.cyberalliances.service.AllianceBuffService;
import me.sbpro.cyberalliances.service.AllianceXpService;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class CyberAlliances
extends JavaPlugin {
    private AllianceStorage allianceStorage;
    private ChatToggleService chatToggleService;
    private InviteService inviteService;
    private AllianceBuffService buffService;
    private AllianceXpService xpService;

    public void onEnable() {
        this.allianceStorage = new AllianceStorage(this);
        this.chatToggleService = new ChatToggleService();
        this.inviteService = new InviteService();
        this.buffService = new AllianceBuffService(this, this.allianceStorage);
        this.xpService = new AllianceXpService(this, this.allianceStorage, this.buffService);
        AllianceCommand allianceCommand = new AllianceCommand(this, this.allianceStorage, this.chatToggleService, this.inviteService, this.buffService, this.xpService);
        PluginCommand command = this.getCommand("alliance");
        if (command != null) {
            command.setExecutor((CommandExecutor)allianceCommand);
            command.setTabCompleter((TabCompleter)allianceCommand);
        }
        this.getServer().getPluginManager().registerEvents((Listener)new DeleteAllianceListener(this.allianceStorage, this.chatToggleService, this.buffService), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new AllianceChatListener(this.allianceStorage, this.chatToggleService), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new AllianceXpListener(this.xpService), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new AlliancePlayerListener(this, this.buffService), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new AllianceLevelGuiListener(this, this.allianceStorage, this.xpService), (Plugin)this);
        if (this.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new AlliancePlaceholderExpansion(this, this.allianceStorage).register();
        }
        this.buffService.refreshAllOnlinePlayers();
    }

    public AllianceStorage getAllianceStorage() {
        return this.allianceStorage;
    }

    public ChatToggleService getChatToggleService() {
        return this.chatToggleService;
    }

    public InviteService getInviteService() {
        return this.inviteService;
    }

    public AllianceBuffService getBuffService() {
        return this.buffService;
    }

    public AllianceXpService getXpService() {
        return this.xpService;
    }
}

