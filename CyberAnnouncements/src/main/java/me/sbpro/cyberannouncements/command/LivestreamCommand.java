package me.sbpro.cyberannouncements.command;

import me.sbpro.cyberannouncements.Messages;
import me.sbpro.cyberannouncements.manager.ChatLockManager;
import me.sbpro.cyberannouncements.util.UrlUtils;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LivestreamCommand implements CommandExecutor {

    private final ChatLockManager chatLockManager;

    public LivestreamCommand(ChatLockManager chatLockManager) {
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

        if (!player.hasPermission("cyber.media.announce")) {
            player.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Messages.usageUrl("livestream"));
            return true;
        }

        String url = UrlUtils.normalizeUrl(args[0]);

        if (url == null) {
            player.sendMessage(Messages.INVALID_URL);
            return true;
        }

        String displayUrl = UrlUtils.stripScheme(url);

        // Title
        Title title = Title.title(
                Messages.mediaTitle(player.getName(), "IS STREAMING"),
                Messages.mediaSubtitle(displayUrl)
        );

        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.showTitle(title));

        // Chat announcement (displays without scheme, opens with full URL)
        Bukkit.broadcast(Messages.mediaBroadcast("NEW LIVESTREAM:", player.getName(), displayUrl, url, "Click to open stream"));

        // Lock chat for 5 seconds
        chatLockManager.lockChat();

        return true;
    }
}
