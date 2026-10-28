package me.sbpro.cyberstatistics.stats;

import me.sbpro.cyberstatistics.Messages;
import me.sbpro.cyberstatistics.CyberStatistics;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatisticsCommand implements CommandExecutor {

    private final CyberStatistics plugin;

    public StatisticsCommand(CyberStatistics plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Messages.PLAYER_ONLY);
            return true;
        }

        if (!player.hasPermission("cyber.statistics")) {
            player.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length == 0) {
            StatisticsMenu.open(player, player);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(Messages.PLAYER_NOT_ONLINE);
            return true;
        }

        StatisticsMenu.open(player, target);

        return true;
    }
}