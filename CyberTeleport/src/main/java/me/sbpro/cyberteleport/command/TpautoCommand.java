package me.sbpro.cyberteleport.command;

import me.sbpro.cyberteleport.CyberTeleport;
import me.sbpro.cyberteleport.message.Messages;
import me.sbpro.cyberteleport.permission.Permissions;
import me.sbpro.cyberteleport.settings.TeleportPreferencesManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class TpautoCommand implements CommandExecutor {

    private final TeleportPreferencesManager preferencesManager;

    public TpautoCommand(CyberTeleport plugin) {
        this.preferencesManager =
                plugin.getTeleportPreferencesManager();
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

        if (!player.hasPermission(Permissions.TPAUTO)) {
            player.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length != 0) {
            player.sendMessage(Messages.invalidUsage("/tpauto"));
            return true;
        }

        boolean enabled =
                preferencesManager.toggleAutoAcceptTpa(player);

        if (enabled) {
            player.sendMessage(
                    Messages.tpautoEnabled()
            );
        } else {
            player.sendMessage(
                    Messages.tpautoDisabled()
            );
        }

        return true;
    }
}