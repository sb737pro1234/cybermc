package me.sbpro.cyberstaff.commands;

import me.sbpro.cyberstaff.Messages;
import me.sbpro.cyberstaff.CyberStaff;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/** /cyberstaff reload - reloads punishments.yml and the punishment reasons. */
public class CyberStaffCommand implements CommandExecutor {

    private final CyberStaff plugin;

    public CyberStaffCommand(CyberStaff plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {
        if (!sender.hasPermission("cyber.staff")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length != 1 || !args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(Messages.STAFF_RELOAD_USAGE);
            return true;
        }

        plugin.getPunishmentConfig().reload();
        plugin.getReasonManager().reload();

        sender.sendMessage(Messages.RELOAD_SUCCESS);
        return true;
    }
}
