package me.sbpro.cyberannouncements.command;

import me.sbpro.cyberannouncements.Messages;
import me.sbpro.cyberannouncements.CyberAnnouncements;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AnnouncementsCommand implements CommandExecutor {

    private final CyberAnnouncements plugin;

    public AnnouncementsCommand(CyberAnnouncements plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!sender.hasPermission("cyber.announcements.admin")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {

            plugin.getAnnouncementManager().reload();

            sender.sendMessage(Messages.CONFIG_RELOADED);

            return true;
        }

        sender.sendMessage(Messages.USAGE_RELOAD);

        return true;
    }
}