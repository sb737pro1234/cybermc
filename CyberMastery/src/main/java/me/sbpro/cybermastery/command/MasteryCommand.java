package me.sbpro.cybermastery.command;

import me.sbpro.cybermastery.CyberMastery;
import me.sbpro.cybermastery.Messages;
import me.sbpro.cybermastery.data.PlayerData;
import me.sbpro.cybermastery.gui.MenuFactory;
import me.sbpro.cybermastery.model.LevelCurve;
import me.sbpro.cybermastery.model.MasteryType;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * /mastery (alias /m)
 * /mastery top  and  /masterytop          cyber.mastery.use
 * /mastery reload                        cyber.mastery.reload
 * /mastery give <player> <type> <levels>  cyber.mastery.admin
 * /mastery set <player> <type> <level>    cyber.mastery.admin
 * /mastery reset <player> [type]          cyber.mastery.admin
 * /mastery check                          cyber.mastery.admin
 */
public final class MasteryCommand implements TabExecutor {

    private static final String RELOAD_PERMISSION = "cyber.mastery.reload";
    private static final String ADMIN_PERMISSION = "cyber.mastery.admin";

    private static final int CHECK_RANGE = 6;

    private final CyberMastery plugin;

    public MasteryCommand(CyberMastery plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // /masterytop
        if (command.getName().equalsIgnoreCase("masterytop")) {
            return openTop(sender);
        }

        // /mastery
        if (args.length == 0) {

            if (!(sender instanceof Player player)) {
                sender.sendMessage(Messages.PLAYER_ONLY);
                return true;
            }

            if (!player.hasPermission("cyber.mastery.use")) {
                player.sendMessage(Messages.NO_PERMISSION);
                return true;
            }

            player.openInventory(MenuFactory.createMainMenu(plugin, player));
            return true;
        }

        String sub = args[0].toLowerCase(Locale.ROOT);

        // /mastery top
        if (sub.equals("top")) {
            return openTop(sender);
        }

        // /mastery reload
        if (sub.equals("reload")) {

            if (!sender.hasPermission(RELOAD_PERMISSION)) {
                sender.sendMessage(Messages.NO_PERMISSION);
                return true;
            }

            if (plugin.reloadMastery()) {
                sender.sendMessage(Messages.reloadSuccess(plugin.getRegistry().allTypes().size()));
            } else {
                sender.sendMessage(Messages.reloadFailed());
            }
            return true;
        }

        boolean admin = sender.hasPermission(ADMIN_PERMISSION);

        if (!admin) {
            sender.sendMessage(sub.equals("give") || sub.equals("set") || sub.equals("reset") || sub.equals("check")
                    ? Messages.NO_PERMISSION
                    : Messages.usage(false));
            return true;
        }

        switch (sub) {
            case "give" -> give(sender, args);
            case "set" -> set(sender, args);
            case "reset" -> reset(sender, args);
            case "check" -> check(sender);
            default -> sender.sendMessage(Messages.usage(true));
        }

        return true;
    }

    // =============================================================
    // /mastery top and /masterytop
    // =============================================================

    private boolean openTop(CommandSender sender) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Messages.PLAYER_ONLY);
            return true;
        }

        if (!player.hasPermission("cyber.mastery.use")) {
            player.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        player.openInventory(
                MenuFactory.createTopMasteryMenu(plugin, player)
        );

        return true;
    }

    // =============================================================
    // /mastery give <player> <type> <levels>
    // =============================================================

    private void give(CommandSender sender, String[] args) {
        Target target = target(sender, args, Messages.GIVE_USAGE, Messages.GIVE_EXAMPLE);
        if (target == null) {
            return;
        }

        Integer levels = parseInt(args[3]);
        if (levels == null || levels < 1 || levels > LevelCurve.MAX_LEVEL) {
            sender.sendMessage(Messages.invalidLevelAmount());
            return;
        }

        int before = plugin.getService().level(target.data(), target.type());

        // Goes through the normal path, so action bars, titles, sounds and broadcasts can be tested.
        if (!plugin.getService().giveLevels(target.player(), target.type(), levels)) {
            sender.sendMessage(Messages.alreadyMaxLevel(target.player().getName(), target.type()));
            return;
        }

        int after = plugin.getService().level(target.data(), target.type());
        sender.sendMessage(Messages.levelsGiven(target.player().getName(), target.type(), after - before, after));
    }

    // =============================================================
    // /mastery set <player> <type> <level>
    // =============================================================

    private void set(CommandSender sender, String[] args) {
        Target target = target(sender, args, Messages.SET_USAGE, Messages.SET_EXAMPLE);
        if (target == null) {
            return;
        }

        Integer level = parseInt(args[3]);
        if (level == null || level < 0 || level > LevelCurve.MAX_LEVEL) {
            sender.sendMessage(Messages.invalidLevel());
            return;
        }

        plugin.getService().setLevel(target.data(), target.type(), level);
        sender.sendMessage(Messages.levelSet(target.player().getName(), target.type(), level));
    }

    /** The parsed "<player> <type>" part of give and set. */
    private record Target(Player player, PlayerData data, MasteryType type) {
    }

    /** Parses args[1..2]; sends the right error and returns null if anything is wrong. */
    private Target target(CommandSender sender, String[] args, String usage, String example) {
        if (args.length != 4) {
            sender.sendMessage(Messages.subUsage(usage, example));
            return null;
        }

        Player player = Bukkit.getPlayerExact(args[1]);
        PlayerData data = player == null ? null : plugin.getDataManager().get(player.getUniqueId());

        if (data == null) {
            sender.sendMessage(Messages.playerNotOnline());
            return null;
        }

        MasteryType type = plugin.getRegistry().type(args[2]);
        if (type == null) {
            sender.sendMessage(Messages.unknownType(args[2]));
            return null;
        }

        return new Target(player, data, type);
    }

    // =============================================================
    // /mastery reset <player> [type]
    // =============================================================

    private void reset(CommandSender sender, String[] args) {
        if (args.length != 2 && args.length != 3) {
            sender.sendMessage(Messages.subUsage(Messages.RESET_USAGE, Messages.RESET_EXAMPLE));
            return;
        }

        Player target = Bukkit.getPlayerExact(args[1]);
        PlayerData data = target == null ? null : plugin.getDataManager().get(target.getUniqueId());

        if (data == null) {
            sender.sendMessage(Messages.playerNotOnline());
            return;
        }

        // /mastery reset <player>
        if (args.length == 2) {
            plugin.getService().reset(data, null);
            sender.sendMessage(Messages.allReset(target.getName()));
            return;
        }

        // /mastery reset <player> <type>
        MasteryType type = plugin.getRegistry().type(args[2]);
        if (type == null) {
            sender.sendMessage(Messages.unknownType(args[2]));
            return;
        }

        plugin.getService().reset(data, type);
        sender.sendMessage(Messages.typeReset(target.getName(), type));
    }

    // =============================================================
    // /mastery check
    // =============================================================

    private void check(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Messages.PLAYER_ONLY);
            return;
        }

        Block block = player.getTargetBlockExact(CHECK_RANGE);
        if (block == null || block.getType().isAir()) {
            player.sendMessage(Messages.checkNoBlock());
            return;
        }

        player.sendMessage(Messages.checkResult(
                block.getType().name().toLowerCase(Locale.ROOT),
                plugin.getRegistry().blockType(block.getType()),
                plugin.getTracker().isPlaced(block),
                plugin.getTracker().isCredited(block)
        ));
    }

    // =============================================================
    // Tab completion
    // =============================================================

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("masterytop")) {
            return List.of();
        }

        boolean admin = sender.hasPermission(ADMIN_PERMISSION);

        if (args.length == 1) {
            List<String> options = new ArrayList<>();
            if (sender.hasPermission("cyber.mastery.use")) {
                options.add("top");
            }
            if (sender.hasPermission(RELOAD_PERMISSION)) {
                options.add("reload");
            }
            if (admin) {
                options.addAll(List.of("give", "set", "reset", "check"));
            }
            return filter(options, args[0]);
        }

        if (!admin) {
            return List.of();
        }

        String sub = args[0].toLowerCase(Locale.ROOT);

        // /mastery reset <player> [type]
        if (sub.equals("reset")) {
            if (args.length == 2) {
                return filter(onlineNames(), args[1]);
            }
            if (args.length == 3) {
                return filter(typeIds(), args[2]);
            }
            return List.of();
        }

        if (!sub.equals("give") && !sub.equals("set")) {
            return List.of();
        }

        // /mastery <give|set> <player> <type> <levels>
        return switch (args.length) {
            case 2 -> filter(onlineNames(), args[1]);
            case 3 -> filter(typeIds(), args[2]);
            case 4 -> sub.equals("give")
                    ? filter(List.of("1", "5", "10", "45"), args[3])
                    : filter(List.of("0", "4", "5", "10", "44", "45"), args[3]);
            default -> List.of();
        };
    }

    private static List<String> onlineNames() {
        List<String> names = new ArrayList<>();
        for (Player online : Bukkit.getOnlinePlayers()) {
            names.add(online.getName());
        }
        return names;
    }

    private List<String> typeIds() {
        List<String> ids = new ArrayList<>();
        for (MasteryType type : plugin.getRegistry().allTypes()) {
            ids.add(type.id());
        }
        return ids;
    }

    private static List<String> filter(List<String> options, String typed) {
        String lower = typed.toLowerCase(Locale.ROOT);
        List<String> matches = new ArrayList<>();
        for (String option : options) {
            if (option.toLowerCase(Locale.ROOT).startsWith(lower)) {
                matches.add(option);
            }
        }
        return matches;
    }

    private static Integer parseInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}
