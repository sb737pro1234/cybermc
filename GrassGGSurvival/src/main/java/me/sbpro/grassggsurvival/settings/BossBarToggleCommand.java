package me.sbpro.grassggsurvival.settings;

import me.sbpro.grassggsurvival.GrassGGSurvival;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BossBarToggleCommand implements CommandExecutor {

    private final GrassGGSurvival plugin;

    public BossBarToggleCommand(GrassGGSurvival plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§2§lGRASS.GG §8» §cOnly players can use this command.");
            return true;
        }

        plugin.getBossBarManager().toggleBossBar(player);

        if (plugin.getBossBarManager().isBossBarEnabled(player.getUniqueId())) {
            player.sendMessage("§2§lGRASS.GG §8» §fYour bossbar has been §2enabled§f.");
        } else {
            player.sendMessage("§2§lGRASS.GG §8» §fYour bossbar has been §2disabled§f.");
        }

        return true;
    }
}
