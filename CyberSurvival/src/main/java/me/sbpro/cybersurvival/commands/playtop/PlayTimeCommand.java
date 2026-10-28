package me.sbpro.cybersurvival.commands.playtop;

import me.sbpro.cybersurvival.Messages;
import me.sbpro.cybersurvival.util.PlaytimeFormatter;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayTimeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Messages.PLAYER_ONLY);
            return true;
        }

        long playtimeTicks = player.getStatistic(Statistic.PLAY_ONE_MINUTE);

        player.sendMessage(Messages.playtime(PlaytimeFormatter.format(playtimeTicks)));

        return true;
    }
}