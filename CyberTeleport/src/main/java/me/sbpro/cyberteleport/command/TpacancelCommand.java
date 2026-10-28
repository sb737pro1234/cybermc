package me.sbpro.cyberteleport.command;

import me.sbpro.cyberteleport.CyberTeleport;
import me.sbpro.cyberteleport.message.Messages;
import me.sbpro.cyberteleport.permission.Permissions;
import me.sbpro.cyberteleport.request.TeleportRequestManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class TpacancelCommand implements CommandExecutor {

    private final TeleportRequestManager requestManager;

    public TpacancelCommand(CyberTeleport plugin) {
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

        if (!player.hasPermission(Permissions.TPACANCEL)) {
            player.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length != 0) {
            player.sendMessage(Messages.invalidUsage("/tpacancel"));
            return true;
        }

        requestManager.cancelOutgoing(player);

        return true;
    }
}