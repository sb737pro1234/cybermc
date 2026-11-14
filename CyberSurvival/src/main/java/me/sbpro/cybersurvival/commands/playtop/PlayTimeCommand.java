package me.sbpro.cybersurvival.commands.playtop;

import me.sbpro.cybersurvival.Messages;
import me.sbpro.cybersurvival.util.PlaytimeFormatter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayTimeCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // /playtime
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Messages.PLAYER_ONLY);
                return true;
            }

            player.sendMessage(Messages.playtime(PlaytimeFormatter.format(player.getStatistic(Statistic.PLAY_ONE_MINUTE))));
            return true;
        }

        // /playtime <player>
        OfflinePlayer target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            target = Bukkit.getOfflinePlayerIfCached(args[0]);
        }

        if (target == null || (!target.isOnline() && !target.hasPlayedBefore())) {
            sender.sendMessage(Messages.PLAYER_NOT_FOUND);
            return true;
        }

        String playtime = PlaytimeFormatter.format(target.getStatistic(Statistic.PLAY_ONE_MINUTE));

        if (sender instanceof Player player && player.getUniqueId().equals(target.getUniqueId())) {
            player.sendMessage(Messages.playtime(playtime));
            return true;
        }

        String name = target.getName() != null ? target.getName() : args[0];
        sender.sendMessage(Messages.playtimeOther(name, playtime));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            return List.of();
        }

        String prefix = args[0].toLowerCase();
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(prefix))
                .toList();
    }
}
