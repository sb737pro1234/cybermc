package me.sbpro.cyberquests.commands;

import me.sbpro.cyberquests.CyberQuests;
import me.sbpro.cyberquests.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/** /tutorial — opens the Tutorial quests menu directly. */
public class TutorialCommand implements CommandExecutor {
    private final CyberQuests plugin;

    public TutorialCommand(CyberQuests plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // /tutorial
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Messages.PLAYER_ONLY);
            return true;
        }
        plugin.getMenus().openTutorial(player);
        return true;
    }
}
