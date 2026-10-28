package me.sbpro.cyberteleport.command;

import me.sbpro.cyberteleport.CyberTeleport;
import me.sbpro.cyberteleport.message.Messages;
import me.sbpro.cyberteleport.permission.Permissions;
import me.sbpro.cyberteleport.settings.TeleportPreferencesManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class TpaheretoggleCommand implements CommandExecutor {

    private final TeleportPreferencesManager preferencesManager;

    public TpaheretoggleCommand(CyberTeleport plugin) {
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

        if (!player.hasPermission(Permissions.TPAHERETOGGLE)) {
            player.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length != 0) {
            player.sendMessage(Messages.invalidUsage("/tpaheretoggle"));
            return true;
        }

        boolean disabled = preferencesManager.toggleTpaHere(player);

        player.sendMessage(Messages.tpaHereToggled(disabled));

        return true;
    }
}