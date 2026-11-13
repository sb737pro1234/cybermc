package me.sbpro.cyberkits.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.sbpro.cyberkits.CyberKits;
import me.sbpro.cyberkits.Kits;
import me.sbpro.cyberkits.Messages;
import me.sbpro.cyberkits.kit.Kit;

public final class KitsCommand implements CommandExecutor, TabCompleter {

    private static final String ADMIN_PERMISSION = "cyber.kits.admin";

    private final CyberKits plugin;

    public KitsCommand(CyberKits plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // /kits
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Messages.PLAYER_ONLY);
                return true;
            }
            plugin.openMenu(player);
            return true;
        }

        switch (args[0].toLowerCase(Locale.ROOT)) {
            // /kits editor [kit]
            case "editor" -> {
                if (!isAdmin(sender)) return true;
                if (!(sender instanceof Player player)) {
                    sender.sendMessage(Messages.PLAYER_ONLY);
                    return true;
                }

                if (args.length == 1) {
                    plugin.openEditorSelect(player);
                    return true;
                }

                Kit kit = Kits.byId(args[1]);
                if (kit == null) {
                    sender.sendMessage(Messages.INVALID_KIT);
                    return true;
                }
                plugin.openEditor(player, kit);
            }

            // /kits reload
            case "reload" -> {
                if (!isAdmin(sender)) return true;
                plugin.reload();
                sender.sendMessage(Messages.RELOAD_SUCCESS);
            }

            // /kits cooldown <reset|lock> <player> [kit]
            case "cooldown" -> {
                if (!isAdmin(sender)) return true;
                if (args.length < 3 || args.length > 4) {
                    sender.sendMessage(Messages.USAGE_COOLDOWN);
                    return true;
                }

                String action = args[1].toLowerCase(Locale.ROOT);
                if (!action.equals("reset") && !action.equals("lock")) {
                    sender.sendMessage(Messages.USAGE_COOLDOWN);
                    return true;
                }

                OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[2]);
                if (target == null) {
                    sender.sendMessage(Messages.INVALID_PLAYER);
                    return true;
                }
                String name = target.getName() != null ? target.getName() : args[2];

                // No kit given = every kit
                Kit kit = null;
                if (args.length == 4) {
                    kit = Kits.byId(args[3]);
                    if (kit == null) {
                        sender.sendMessage(Messages.INVALID_KIT);
                        return true;
                    }
                }

                if (action.equals("reset")) {
                    plugin.cooldowns().reset(target.getUniqueId(), kit);
                    sender.sendMessage(Messages.cooldownReset(name, kit));
                } else {
                    plugin.cooldowns().start(target.getUniqueId(), kit, Kits.KITS);
                    sender.sendMessage(Messages.cooldownLocked(name, kit));
                }
            }

            default -> sender.sendMessage(Messages.USAGE);
        }
        return true;
    }

    private boolean isAdmin(CommandSender sender) {
        if (!sender.hasPermission(ADMIN_PERMISSION)) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission(ADMIN_PERMISSION)) return Collections.emptyList();

        if (args.length == 1) return partial(args[0], List.of("editor", "reload", "cooldown"));

        String sub = args[0].toLowerCase(Locale.ROOT);
        if (args.length == 2 && sub.equals("editor")) return partial(args[1], kitIds());
        if (args.length == 2 && sub.equals("cooldown")) return partial(args[1], List.of("reset", "lock"));
        if (args.length == 3 && sub.equals("cooldown")) return partial(args[2], onlinePlayers());
        if (args.length == 4 && sub.equals("cooldown")) return partial(args[3], kitIds());
        return Collections.emptyList();
    }

    private static List<String> kitIds() {
        List<String> ids = new ArrayList<>();
        for (Kit kit : Kits.KITS) ids.add(kit.id());
        return ids;
    }

    private static List<String> onlinePlayers() {
        List<String> names = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) names.add(player.getName());
        return names;
    }

    private static List<String> partial(String input, List<String> options) {
        List<String> result = new ArrayList<>();
        String lower = input.toLowerCase(Locale.ROOT);
        for (String option : options) {
            if (option.toLowerCase(Locale.ROOT).startsWith(lower)) result.add(option);
        }
        return result;
    }
}
