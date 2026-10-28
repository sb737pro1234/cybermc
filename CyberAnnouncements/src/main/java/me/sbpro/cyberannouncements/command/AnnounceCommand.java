package me.sbpro.cyberannouncements.command;

import me.sbpro.cyberannouncements.Messages;
import me.sbpro.cyberannouncements.manager.ChatLockManager;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AnnounceCommand implements CommandExecutor {

    private final ChatLockManager chatLockManager;

    public AnnounceCommand(ChatLockManager chatLockManager) {
        this.chatLockManager = chatLockManager;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Messages.PLAYER_ONLY);
            return true;
        }

        if (!player.hasPermission("cyber.announce")) {
            player.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Messages.USAGE_ANNOUNCE);
            return true;
        }

        String announcement = String.join(" ", args);

        // Title
        Title title = Title.title(Messages.ANNOUNCEMENT_TITLE, Messages.ANNOUNCEMENT_SUBTITLE);

        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.showTitle(title));

        // Chat announcement
        Bukkit.broadcast(Messages.announcementBroadcast(player.getName(), announcement));

        // Lock chat for 5 seconds
        chatLockManager.lockChat();

        return true;
    }
}
