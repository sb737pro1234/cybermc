package me.sbpro.cyberteleport.command;

import me.sbpro.cyberteleport.CyberTeleport;
import me.sbpro.cyberteleport.message.Messages;
import me.sbpro.cyberteleport.permission.Permissions;
import me.sbpro.cyberteleport.request.TeleportRequestManager;
import me.sbpro.cyberteleport.request.TeleportRequestType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class TpahereCommand implements CommandExecutor {

    private final TeleportRequestManager requestManager;

    public TpahereCommand(CyberTeleport plugin) {
        this.requestManager = plugin.getTeleportRequestManager();
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

        if (!player.hasPermission(Permissions.TPAHERE)) {
            player.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(Messages.invalidUsage("/tpahere <player>"));
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);

        if (target == null) {
            player.sendMessage(Messages.playerNotFound());
            return true;
        }

        if (target.equals(player)) {
            player.sendMessage(Messages.cannotRequestSelf());
            return true;
        }

        requestManager.sendRequest(
                player,
                target,
                TeleportRequestType.TPAHERE
        );

        return true;
    }
}