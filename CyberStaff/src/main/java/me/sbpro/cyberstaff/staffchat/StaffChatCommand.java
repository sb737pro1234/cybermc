package me.sbpro.cyberstaff.staffchat;

import me.sbpro.cyberstaff.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public final class StaffChatCommand
        implements CommandExecutor, TabCompleter {

    private final StaffChatManager manager;

    public StaffChatCommand(
            StaffChatManager manager
    ) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!(sender instanceof Player player)) {

            sender.sendMessage(
                    Messages.PLAYER_ONLY
            );

            return true;
        }

        if (!player.hasPermission(
                "cyber.staff.chat"
        )) {

            player.sendMessage(
                    Messages.NO_PERMISSION
            );

            return true;
        }

        manager.toggle(player);

        if (manager.isEnabled(player)) {

            player.sendMessage(
                    Messages.STAFF_CHAT_ENABLED
            );

        } else {

            player.sendMessage(
                    Messages.STAFF_CHAT_DISABLED
            );
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String alias,
            String[] args
    ) {

        return List.of();
    }
}