package me.sbpro.cyber.commands.say;

import me.sbpro.cyber.Messages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;

public class SayCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(Messages.USAGE_SAY);
            return true;
        }

        String message = String.join(" ", args);

        // Console / non-player sender
        if (!(sender instanceof Player)) {

            Component consoleMessage = Messages.sayConsole(message);

            for (Player online : sender.getServer().getOnlinePlayers()) {
                online.sendMessage(consoleMessage);
            }

            return true;
        }

        // Player sender
        Player player = (Player) sender;

        if (!player.hasPermission("cyber.say")) {
            player.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        Title title = Title.title(
                Messages.sayPlayerName(player.getName()),
                Messages.sayText(message),
                Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(3500), Duration.ofSeconds(1))
        );

        for (Player online : player.getServer().getOnlinePlayers()) {
            online.showTitle(title);
        }

        player.sendMessage(Messages.saySent(message));

        return true;
    }
}