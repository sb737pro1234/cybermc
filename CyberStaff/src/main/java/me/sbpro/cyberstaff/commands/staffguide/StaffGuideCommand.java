package me.sbpro.cyberstaff.commands.staffguide;

import me.sbpro.cyberstaff.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffGuideCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player) {
            StaffGuideMenus.openMain(player);
        } else {
            sender.sendMessage(Messages.PLAYER_ONLY);
        }

        return true;
    }
}