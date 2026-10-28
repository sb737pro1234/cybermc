package me.sbpro.cyber.color;

import me.sbpro.cyber.Messages;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ColorsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        sender.sendMessage(Component.empty());
        sender.sendMessage(Messages.COLORS_HEADER);
        sender.sendMessage(Messages.colorRow(0));
        sender.sendMessage(Messages.colorRow(8));
        sender.sendMessage(Component.empty());
        sender.sendMessage(Messages.FORMATTING_HEADER);
        sender.sendMessage(Messages.FORMAT_BOLD_STRIKETHROUGH);
        sender.sendMessage(Messages.FORMAT_ITALIC_UNDERLINE);
        sender.sendMessage(Messages.FORMAT_OBFUSCATED_RESET);
        sender.sendMessage(Component.empty());

        return true;
    }
}