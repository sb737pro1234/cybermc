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

public class ContentCommand implements CommandExecutor {

    private final ChatLockManager chatLockManager;

    public ContentCommand(ChatLockManager chatLockManager) {
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
            player.sendMessage(Messages.usageUrl("content"));
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
                Messages.mediaTitle(player.getName(), "HAS UPLOADED"),
                Messages.mediaSubtitle(displayUrl)
        );

        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.showTitle(title));

        // Chat announcement (displays without scheme, opens with full URL)
        Bukkit.broadcast(Messages.mediaBroadcast("NEW CONTENT:", player.getName(), displayUrl, url, "Click to open content"));

        // Lock chat for 5 seconds
        chatLockManager.lockChat();

        return true;
    }
}
