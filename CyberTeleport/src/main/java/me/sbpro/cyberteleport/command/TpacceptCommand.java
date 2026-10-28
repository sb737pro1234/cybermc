package me.sbpro.cyberteleport.command;

import me.sbpro.cyberteleport.CyberTeleport;
import me.sbpro.cyberteleport.message.Messages;
import me.sbpro.cyberteleport.permission.Permissions;
import me.sbpro.cyberteleport.request.TeleportRequestManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class TpacceptCommand implements CommandExecutor {

    private final TeleportRequestManager requestManager;

    public TpacceptCommand(CyberTeleport plugin) {
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

        if (!player.hasPermission(Permissions.TPACCEPT)) {
            player.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length == 0) {
            requestManager.acceptMostRecent(player);
            return true;
        }

        if (args.length == 1) {
            Player requester = Bukkit.getPlayerExact(args[0]);

            if (requester == null) {
                player.sendMessage(Messages.playerNotFound());
                return true;
            }

            requestManager.acceptFrom(
                    player,
                    requester
            );

            return true;
        }

        player.sendMessage(Messages.invalidUsage("/tpaccept [player]"));

        return true;
    }
}