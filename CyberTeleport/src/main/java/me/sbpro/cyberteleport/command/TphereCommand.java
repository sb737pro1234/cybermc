package me.sbpro.cyberteleport.command;

import me.sbpro.cyberteleport.CyberTeleport;
import me.sbpro.cyberteleport.message.Messages;
import me.sbpro.cyberteleport.permission.Permissions;
import me.sbpro.cyberteleport.teleport.TeleportManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class TphereCommand implements CommandExecutor {

    private final TeleportManager teleportManager;

    public TphereCommand(CyberTeleport plugin) {
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

        if (!player.hasPermission(Permissions.TPHERE)) {
            player.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(Messages.invalidUsage("/tphere <player>"));
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);

        if (target == null) {
            player.sendMessage(Messages.playerNotFound());
            return true;
        }

        if (target.equals(player)) {
            player.sendMessage(Messages.cannotTeleportPlayerToThemselves());
            return true;
        }

        teleportManager.requestAdminTeleport(
                player,
                target,
                player.getLocation(),
                "teleport " + target.getName() + " to you"
        );

        return true;
    }
}