package me.sbpro.cyber.commands.gui;

import me.sbpro.cyber.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MenuType;

public class CraftCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Messages.PLAYER_ONLY);
            return true;
        }

        if (!player.hasPermission("cyber.gui.craft")) {
            player.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        player.openInventory(MenuType.CRAFTING.builder().checkReachable(false).build(player));

        return true;
    }
}