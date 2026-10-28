package me.sbpro.cyberannouncements;

import me.sbpro.cyberannouncements.command.AnnounceCommand;
import me.sbpro.cyberannouncements.command.AnnouncementsCommand;
import me.sbpro.cyberannouncements.command.ContentCommand;
import me.sbpro.cyberannouncements.command.LivestreamCommand;
import me.sbpro.cyberannouncements.listener.ChatLockListener;
import me.sbpro.cyberannouncements.manager.AnnouncementManager;
import me.sbpro.cyberannouncements.manager.ChatLockManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CyberAnnouncements extends JavaPlugin {

    private AnnouncementManager announcementManager;
    private ChatLockManager chatLockManager;

    @Override
    public void onEnable() {
        announcementManager = new AnnouncementManager(this);
        chatLockManager = new ChatLockManager();

        getCommand("announcements").setExecutor(new AnnouncementsCommand(this));
        getCommand("announce").setExecutor(new AnnounceCommand(chatLockManager));
        getCommand("livestream").setExecutor(new LivestreamCommand(chatLockManager));
        getCommand("content").setExecutor(new ContentCommand(chatLockManager));

        getServer().getPluginManager().registerEvents(
                new ChatLockListener(chatLockManager),
                this
        );

        announcementManager.start();

        getLogger().info("CyberAnnouncements has been enabled!");
    }

    @Override
    public void onDisable() {
        if (announcementManager != null) {
            announcementManager.stop();
        }

        getLogger().info("CyberAnnouncements has been disabled!");
    }

    public AnnouncementManager getAnnouncementManager() {
        return announcementManager;
    }
}