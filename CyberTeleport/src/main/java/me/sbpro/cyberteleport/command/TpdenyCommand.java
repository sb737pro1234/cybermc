package me.sbpro.cyberteleport.command;

import me.sbpro.cyberteleport.CyberTeleport;
import me.sbpro.cyberteleport.message.Messages;
import me.sbpro.cyberteleport.permission.Permissions;
import me.sbpro.cyberteleport.request.TeleportRequestManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class TpdenyCommand implements CommandExecutor {

    private final TeleportRequestManager requestManager;

    public TpdenyCommand(CyberTeleport plugin) {
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

        if (!player.hasPermission(Permissions.TPDENY)) {
            player.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length != 0) {
            player.sendMessage(Messages.invalidUsage("/tpdeny"));
            return true;
        }

        requestManager.denyMostRecent(player);

        return true;
    }
}