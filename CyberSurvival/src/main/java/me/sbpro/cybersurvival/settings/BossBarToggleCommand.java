package me.sbpro.cybersurvival.settings;

import me.sbpro.cybersurvival.Messages;

import me.sbpro.cybersurvival.CyberSurvival;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BossBarToggleCommand implements CommandExecutor {

    private final CyberSurvival plugin;

    public BossBarToggleCommand(CyberSurvival plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Messages.PLAYER_ONLY);
            return true;
        }

        plugin.getBossBarManager().toggleBossBar(player);

        player.sendMessage(Messages.bossBarToggled(plugin.getBossBarManager().isBossBarEnabled(player.getUniqueId())));

        return true;
    }
}
