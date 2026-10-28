package me.sbpro.cyberquests.commands;

import me.sbpro.cyberquests.CyberQuests;
import me.sbpro.cyberquests.Messages;
import me.sbpro.cyberquests.quests.QuestDefinition;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import java.util.*;

public class QuestsCommand implements CommandExecutor, TabCompleter {
    private final CyberQuests plugin;
    public QuestsCommand(CyberQuests plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) { sender.sendMessage(Messages.PLAYER_ONLY); return true; }
            plugin.getMenus().openMain(player);
            return true;
        }
        switch (args[0].toLowerCase(Locale.ROOT)) {
            case "reload" -> {
                if (!isAdmin(sender)) return true;
                plugin.reloadPluginConfig();
                sender.sendMessage(Messages.RELOAD_SUCCESS);
            }
            case "reset" -> {
                if (!isAdmin(sender)) return true;
                if (args.length != 2) { sender.sendMessage(Messages.USAGE_RESET); return true; }
                Player target = Bukkit.getPlayerExact(args[1]);
                if (target == null) { sender.sendMessage(Messages.INVALID_PLAYER); return true; }
                plugin.getQuestManager().reset(target);
                sender.sendMessage(Messages.resetSuccess(target.getName()));
            }
            case "give" -> {
                if (!isAdmin(sender)) return true;
                if (args.length != 3) { sender.sendMessage(Messages.USAGE_GIVE); return true; }
                Player target = Bukkit.getPlayerExact(args[1]);
                if (target == null) { sender.sendMessage(Messages.INVALID_PLAYER); return true; }
                QuestDefinition quest = plugin.getQuestManager().getDefinition(args[2]);
                if (quest == null) { sender.sendMessage(Messages.INVALID_QUEST); return true; }
                if (!plugin.getQuestManager().giveQuest(target, quest.getId(), true)) {
                    sender.sendMessage(Messages.ALREADY_ACTIVE);
                    return true;
                }
                sender.sendMessage(Messages.giveSuccess(quest.getTitle(), target.getName()));
            }
            default -> sender.sendMessage(Messages.USAGE);
        }
        return true;
    }

    private boolean isAdmin(CommandSender sender) {
        if (!sender.hasPermission("cyber.quests.admin")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) return partial(args[0], List.of("reload", "reset", "give"));
        if (args.length == 2 && Set.of("reset", "give").contains(args[0].toLowerCase(Locale.ROOT))) return onlinePlayers(args[1]);
        if (args.length == 3 && args[0].equalsIgnoreCase("give")) return partial(args[2], new ArrayList<>(me.sbpro.cyberquests.Quests.QUESTS.keySet()));
        return Collections.emptyList();
    }

    private List<String> onlinePlayers(String input) {
        List<String> names = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) names.add(p.getName());
        return partial(input, names);
    }

    private List<String> partial(String input, List<String> options) {
        List<String> result = new ArrayList<>();
        for (String option : options) if (option.toLowerCase(Locale.ROOT).startsWith(input.toLowerCase(Locale.ROOT))) result.add(option);
        return result;
    }
}
