package me.sbpro.cybersurvival.commands;

import me.sbpro.cybersurvival.Messages;

import me.sbpro.cybersurvival.CyberSurvival;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GlowCommand implements CommandExecutor {

    private final CyberSurvival plugin;

    public GlowCommand(CyberSurvival plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Messages.PLAYER_ONLY);
            return true;
        }

        plugin.getGlowManager().toggleGlow(player);

        player.sendMessage(Messages.glowToggled(plugin.getGlowManager().isGlowEnabled(player.getUniqueId())));

        return true;
    }
}