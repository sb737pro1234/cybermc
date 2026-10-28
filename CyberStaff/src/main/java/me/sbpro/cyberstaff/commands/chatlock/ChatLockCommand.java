package me.sbpro.cyberstaff.commands.chatlock;

import me.sbpro.cyberstaff.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatLockCommand implements CommandExecutor {

    public static boolean chatLocked = false;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("cyber.chatlock")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        chatLocked = !chatLocked;

        if (chatLocked) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(Messages.CHAT_LOCKED);}
            Bukkit.getConsoleSender().sendMessage(Messages.CHAT_LOCKED);
        } else {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Messages.CHAT_UNLOCKED);}
            Bukkit.getConsoleSender().sendMessage(Messages.CHAT_UNLOCKED);
        }

        return true;
    }
}