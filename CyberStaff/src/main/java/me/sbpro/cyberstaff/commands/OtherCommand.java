package me.sbpro.cyberstaff.commands;

import me.sbpro.cyberstaff.CyberStaff;
import me.sbpro.cyberstaff.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class OtherCommand implements CommandExecutor {

    private final CyberStaff plugin;

    public OtherCommand(CyberStaff plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {
        if(!sender.hasPermission("cyber.staff")){
            sender.sendMessage(Messages.NO_PERMISSION);
        } else {
            sender.sendMessage(Messages.COMMAND_NOT_AVAILABLE);
        }

        return true;
    }
}