package me.sbpro.cybersupertools.commands;

import me.sbpro.cybersupertools.CyberSuperTools;
import me.sbpro.cybersupertools.Messages;
import me.sbpro.cybersupertools.tools.SuperToolManager;
import me.sbpro.cybersupertools.tools.SuperToolType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SuperToolsCommand implements CommandExecutor, TabCompleter {

    private final CyberSuperTools plugin;
    private final SuperToolManager toolManager;

    public SuperToolsCommand(CyberSuperTools plugin) {
        this.plugin = plugin;
        this.toolManager = new SuperToolManager(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("cyber.supertools.admin")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length != 3 || !args[0].equalsIgnoreCase("give")) {
            sender.sendMessage(Messages.USAGE);
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            sender.sendMessage(Messages.PLAYER_NOT_FOUND);
            return true;
        }

        SuperToolType type = parseTool(args[2]);
        if (type == null) {
            sender.sendMessage(Messages.UNKNOWN_TOOL);
            return true;
        }

        ItemStack item = toolManager.create(type);
        target.getInventory().addItem(item);

        sender.sendMessage(Messages.playerGiven(target.getName(), "Super " + type.getShortName()));
        return true;
    }

    private SuperToolType parseTool(String input) {
        for (SuperToolType type : SuperToolType.values()) {
            if (type.getId().equalsIgnoreCase(input)) {
                return type;
            }
        }
        return null;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("cyber.supertools.admin")) {
            return List.of();
        }

        if (args.length == 1) {
            return partial(args[0], List.of("give"));
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            List<String> players = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                players.add(player.getName());
            }
            return partial(args[1], players);
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            return partial(args[2], Arrays.stream(SuperToolType.values())
                    .map(SuperToolType::getId)
                    .toList());
        }

        return List.of();
    }

    private List<String> partial(String input, List<String> values) {
        return values.stream()
                .filter(value -> value.toLowerCase().startsWith(input.toLowerCase()))
                .sorted()
                .toList();
    }
}
