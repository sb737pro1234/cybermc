package me.sbpro.cyber.commands.message;

import me.sbpro.cyber.Messages;
import me.sbpro.cyber.Cyber;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ReplyCommand implements CommandExecutor {

    private final Cyber main;

    public ReplyCommand(Cyber main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.PLAYER_ONLY);
            return true;
        }

        Player player = (Player) sender;

        if (main.getMessageManager().isMessagesDisabled(player.getUniqueId())) {
            player.sendMessage(Messages.PRIVATE_MESSAGES_DISABLED_SELF);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(Messages.USAGE_REPLY);
            return true;
        }

        UUID partnerUuid = main.getMessageManager().getLastPartner(player.getUniqueId());

        if (partnerUuid == null) {
            player.sendMessage(Messages.NOBODY_TO_REPLY);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return true;
        }

        Player target = Bukkit.getPlayer(partnerUuid);

        if (target == null || !target.isOnline()) {
            player.sendMessage(Messages.PLAYER_NO_LONGER_ONLINE);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            main.getMessageManager().clearPartner(player.getUniqueId());
            return true;
        }

        if (main.getMessageManager().isMessagesDisabled(target.getUniqueId())) {
            player.sendMessage(Messages.privateMessagesDisabledOther(target.getName()));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return true;
        }

        String message = String.join(" ", args);

        main.getMessageManager().sendMessage(player, target, message);

        return true;
    }
}