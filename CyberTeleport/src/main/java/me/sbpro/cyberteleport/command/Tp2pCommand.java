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

public final class Tp2pCommand implements CommandExecutor {

    private final TeleportManager teleportManager;

    public Tp2pCommand(CyberTeleport plugin) {
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

        if (args.length == 0) {
            player.sendMessage(Messages.invalidUsage("/tp2p <player>"));
            return true;
        }

        if (args.length > 1) {
            player.sendMessage(Messages.invalidUsage("/tp2p <player>"));
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);

        if (target == null) {
            player.sendMessage(Messages.playerNotFound());
            return true;
        }

        if (target.equals(player)) {
            player.sendMessage(Messages.selfTargeting());
            return true;
        }

        teleportManager.requestAdminTeleport(
                player,
                player,
                target.getLocation(),
                "teleport you to " + target.getName()
        );

        return true;
    }
}