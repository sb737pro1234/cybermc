package me.sbpro.cyber.commands.misc;

import me.sbpro.cyber.Messages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class YouTubeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Messages.PLAYER_ONLY);
            return true;
        }

        Component message = Messages.PREFIX
                .append(Component.text("youtube.com/@MrGrassYT1")
                        .clickEvent(ClickEvent.openUrl("https://youtube.com/@MrGrassYT1"))
                        .hoverEvent(HoverEvent.showText(Messages.CLICK_TO_OPEN_YOUTUBE))
                        .color(NamedTextColor.RED));
        player.sendMessage(message);

        return true;
    }
}