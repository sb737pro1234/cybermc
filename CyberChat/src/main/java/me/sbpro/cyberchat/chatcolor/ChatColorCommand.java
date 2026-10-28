package me.sbpro.cyberchat.chatcolor;

import me.sbpro.cyberchat.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatColorCommand implements CommandExecutor {

    private final ChatColorManager manager;

    public ChatColorCommand(
            ChatColorManager manager
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

            sender.sendMessage(Messages.PLAYER_ONLY);

            return true;
        }

        if (!player.hasPermission(
                "cyber.chatcolor.command"
        )) {

            player.sendMessage(Messages.NO_PERMISSION);

            return true;
        }

        ChatColorMenu.open(
                player,
                manager
        );

        return true;
    }
}