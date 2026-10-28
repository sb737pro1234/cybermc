package me.sbpro.cyberteleport.command;

import me.sbpro.cyberteleport.CyberTeleport;
import me.sbpro.cyberteleport.message.Messages;
import me.sbpro.cyberteleport.permission.Permissions;
import me.sbpro.cyberteleport.settings.TeleportPreferencesManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class TpatoggleCommand implements CommandExecutor {

    private final TeleportPreferencesManager preferencesManager;

    public TpatoggleCommand(CyberTeleport plugin) {
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

        if (!player.hasPermission(Permissions.TPTOGGLE)) {
            player.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length != 0) {
            player.sendMessage(Messages.invalidUsage("/tpatoggle"));
            return true;
        }

        boolean disabled = preferencesManager.toggleTpa(player);

        player.sendMessage(Messages.tpaToggled(disabled));

        return true;
    }
}