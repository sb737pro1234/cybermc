package me.sbpro.cybersurvival.commands.playtop;

import me.sbpro.cybersurvival.Messages;
import me.sbpro.cybersurvival.CyberSurvival;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayTopCommand implements CommandExecutor {

    private final CyberSurvival plugin;

    public PlayTopCommand(CyberSurvival plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("cyber.admin")) {
                sender.sendMessage(Messages.NO_PERMISSION);
                return true;
            }

            // Rebuild the cached leaderboard so the menu shows up-to-date playtimes.
            PlayTopMenu.refresh(plugin);
            sender.sendMessage(Messages.PLAYTOP_RELOADED);
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Messages.PLAYER_ONLY);
            return true;
        }

        PlayTopMenu.open(plugin, player);
        return true;
    }
}
