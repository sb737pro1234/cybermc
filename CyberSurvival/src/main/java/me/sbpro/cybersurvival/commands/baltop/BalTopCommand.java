package me.sbpro.cybersurvival.commands.baltop;

import me.sbpro.cybersurvival.Messages;
import me.sbpro.cybersurvival.CyberSurvival;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalTopCommand implements CommandExecutor {

    private final CyberSurvival plugin;

    public BalTopCommand(CyberSurvival plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("cyber.admin")) {
                sender.sendMessage(Messages.NO_PERMISSION);
                return true;
            }

            // Rebuild the cached leaderboard so the menu shows up-to-date balances.
            BalTopMenu.refresh(plugin);
            sender.sendMessage(Messages.BALTOP_RELOADED);
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Messages.PLAYER_ONLY);
            return true;
        }

        BalTopMenu.open(plugin, player);
        return true;
    }
}
