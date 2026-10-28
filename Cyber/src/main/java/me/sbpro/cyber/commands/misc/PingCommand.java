package me.sbpro.cyber.commands.misc;

import me.sbpro.cyber.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // /ping
        if (args.length == 0) {

            if (!(sender instanceof Player player)) {
                sender.sendMessage(Messages.SPECIFY_PLAYER_FROM_CONSOLE);
                return true;
            }

            player.sendMessage(Messages.ping(player.getPing()));
            return true;
        }

        // /ping <player>
        if (args.length == 1) {

            if (sender instanceof Player player && !player.hasPermission("cyber.ping.others")) {
                player.sendMessage(Messages.NO_PERMISSION_PING_OTHERS);
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);

            if (target == null) {
                sender.sendMessage(Messages.PLAYER_NOT_ONLINE_PREFIXED);
                return true;
            }

            sender.sendMessage(Messages.pingOther(target.getName(), target.getPing())
            );

            return true;
        }

        sender.sendMessage(Messages.USAGE_PING);
        return true;
    }
}