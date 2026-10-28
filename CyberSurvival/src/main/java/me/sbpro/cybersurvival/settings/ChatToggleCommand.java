package me.sbpro.cybersurvival.settings;

import me.sbpro.cybersurvival.Messages;

import me.sbpro.cybersurvival.CyberSurvival;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatToggleCommand implements CommandExecutor {

    private final CyberSurvival plugin;

    public ChatToggleCommand(CyberSurvival plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Messages.PLAYER_ONLY);
            return true;
        }

        if (plugin.getChatToggleManager().isChatDisabled(player.getUniqueId())) {
            plugin.getChatToggleManager().enableChat(player.getUniqueId());
            player.sendMessage(Messages.chatVisibilityToggled(true));
        } else {
            plugin.getChatToggleManager().disableChat(player.getUniqueId());
            player.sendMessage(Messages.chatVisibilityToggled(false));
        }

        return true;
    }
}