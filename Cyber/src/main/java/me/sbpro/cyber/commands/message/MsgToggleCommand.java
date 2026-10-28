package me.sbpro.cyber.commands.message;

import me.sbpro.cyber.Messages;
import me.sbpro.cyber.Cyber;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MsgToggleCommand implements CommandExecutor {

    private final Cyber main;

    public MsgToggleCommand(Cyber main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.PLAYER_ONLY);
            return true;
        }

        Player player = (Player) sender;
        MessageManager manager = main.getMessageManager();

        if (args.length == 0) {
            boolean nowDisabled = manager.toggleMessagesDisabled(player.getUniqueId());
            sendStateMessage(player, nowDisabled);
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("on")) {
                manager.setMessagesDisabled(player.getUniqueId(), true);
                sendStateMessage(player, true);
                return true;
            }

            if (args[0].equalsIgnoreCase("off")) {
                manager.setMessagesDisabled(player.getUniqueId(), false);
                sendStateMessage(player, false);
                return true;
            }
        }

        player.sendMessage(Messages.USAGE_MSGTOGGLE);
        return true;
    }

    private void sendStateMessage(Player player, boolean disabled) {
        if (disabled) {
            player.sendMessage(Messages.PRIVATE_MESSAGES_OFF);
        } else {
            player.sendMessage(Messages.PRIVATE_MESSAGES_ON);
        }
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
    }
}