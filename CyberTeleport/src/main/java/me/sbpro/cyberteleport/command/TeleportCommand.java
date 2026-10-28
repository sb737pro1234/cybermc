package me.sbpro.cyberteleport.command;

import me.sbpro.cyberteleport.CyberTeleport;
import me.sbpro.cyberteleport.message.Messages;
import me.sbpro.cyberteleport.permission.Permissions;
import me.sbpro.cyberteleport.teleport.TeleportManager;
import me.sbpro.cyberteleport.util.CoordinateParser;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class TeleportCommand implements CommandExecutor {

    private final TeleportManager teleportManager;

    public TeleportCommand(CyberTeleport plugin) {
        this.teleportManager = plugin.getTeleportManager();
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Messages.PLAYER_ONLY);
            return true;
        }

        if (!player.hasPermission(Permissions.TP)) {
            player.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("@a")) {
                return teleportAll(player);
            }

            return teleportSelfToPlayer(player, args[0]);
        }

        if (args.length == 2) {
            return teleportPlayerToPlayer(
                    player,
                    args[0],
                    args[1]
            );
        }

        if (args.length == 3) {
            return teleportCoordinates(
                    player,
                    args[0],
                    args[1],
                    args[2]
            );
        }

        if (args.length == 4) {
            return teleportToCoordinates(
                    player,
                    args[0],
                    args[1],
                    args[2],
                    args[3]
            );
        }

        player.sendMessage(Messages.invalidUsage("/tp <player> [player] or /tp <x> <y> <z>"));

        return true;
    }

    private boolean teleportSelfToPlayer(
            Player sender,
            String targetInput
    ) {
        Player target = resolveSinglePlayer(sender, targetInput);

        if (target == null) {
            return true;
        }

        if (target.equals(sender)) {
            sender.sendMessage(Messages.selfTargeting());
            return true;
        }

        teleportManager.requestAdminTeleport(
                sender,
                sender,
                target.getLocation(),
                "teleport you to " + target.getName()
        );

        return true;
    }

    private boolean teleportPlayerToPlayer(
            Player sender,
            String playerInput,
            String targetInput
    ) {
        if (!sender.hasPermission(Permissions.TP_OTHERS)) {
            sender.sendMessage(Messages.noPermissionTeleportOthers());
            return true;
        }

        Player player = resolveSinglePlayer(sender, playerInput);

        if (player == null) {
            return true;
        }

        Player target = resolveSinglePlayer(sender, targetInput);

        if (target == null) {
            return true;
        }

        if (player.equals(target)) {
            sender.sendMessage(Messages.cannotTeleportPlayerToThemselves());
            return true;
        }

        teleportManager.requestAdminTeleport(
                sender,
                player,
                target.getLocation(),
                "teleport " + player.getName()
                        + " to " + target.getName()
        );

        return true;
    }

    private boolean teleportCoordinates(
            Player sender,
            String xInput,
            String yInput,
            String zInput
    ) {
        if (!sender.hasPermission(Permissions.TP_COORDINATES)) {
            sender.sendMessage(Messages.noPermissionCoordinates());
            return true;
        }

        Location destination;

        try {
            destination = CoordinateParser.parse(
                    sender.getLocation(),
                    xInput,
                    yInput,
                    zInput
            );
        } catch (IllegalArgumentException exception) {
            sender.sendMessage(Messages.invalidCoordinates());
            return true;
        }

        String formattedLocation = formatCoordinates(destination);

        teleportManager.requestAdminTeleport(
                sender,
                sender,
                destination,
                "teleport you to " + formattedLocation
        );

        return true;
    }

    private boolean teleportToCoordinates(
            Player sender,
            String targetInput,
            String xInput,
            String yInput,
            String zInput
    ) {
        if (!sender.hasPermission(Permissions.TP_COORDINATES)) {
            sender.sendMessage(Messages.noPermissionCoordinates());
            return true;
        }

        Player target = resolveSinglePlayer(sender, targetInput);

        if (target == null) {
            return true;
        }

        Location destination;

        try {
            destination = CoordinateParser.parse(
                    sender.getLocation(),
                    xInput,
                    yInput,
                    zInput
            );
        } catch (IllegalArgumentException exception) {
            sender.sendMessage(Messages.invalidCoordinates());
            return true;
        }

        String formattedLocation = formatCoordinates(
                destination
        );

        teleportManager.requestAdminTeleport(
                sender,
                target,
                destination,
                "teleport "
                        + target.getName()
                        + " to "
                        + formattedLocation
        );

        return true;
    }

    public boolean executeTpall(Player sender) {
        return teleportAll(sender);
    }
    private boolean teleportAll(Player sender) {
        if (!sender.hasPermission(Permissions.TPALL)) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        List<Player> players = new ArrayList<>(
                Bukkit.getOnlinePlayers().stream()
                        .filter(player -> !player.equals(sender))
                        .filter(player -> !player.hasPermission(
                                Permissions.TPALL_BYPASS
                        ))
                        .toList()
        );

        if (players.isEmpty()) {
            sender.sendMessage(Messages.noPlayersToTeleport());
            return true;
        }

        teleportManager.requestAdminTeleportMultiple(
                sender,
                players,
                sender.getLocation(),
                "teleport all players to you"
        );

        return true;
    }

    private Player resolveSinglePlayer(
            CommandSender sender,
            String input
    ) {
        Player exactPlayer = Bukkit.getPlayerExact(input);

        if (exactPlayer != null) {
            return exactPlayer;
        }

        if (!input.startsWith("@")) {
            sender.sendMessage(Messages.playerNotFound());
            return null;
        }

        try {
            List<Entity> entities = Bukkit.selectEntities(
                    sender,
                    input
            );

            List<Player> players = entities.stream()
                    .filter(Player.class::isInstance)
                    .map(Player.class::cast)
                    .toList();

            if (players.size() != 1) {
                sender.sendMessage(Messages.selectorMustSelectOnePlayer());
                return null;
            }

            return players.getFirst();

        } catch (IllegalArgumentException exception) {
            sender.sendMessage(Messages.invalidSelector());
            return null;
        }
    }

    private String formatCoordinates(Location location) {
        return formatNumber(location.getX())
                + ", "
                + formatNumber(location.getY())
                + ", "
                + formatNumber(location.getZ());
    }

    private String formatNumber(double value) {
        if (value == Math.rint(value)) {
            return Long.toString((long) value);
        }

        return Double.toString(value);
    }
}