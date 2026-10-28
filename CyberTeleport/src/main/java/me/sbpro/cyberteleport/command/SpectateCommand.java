package me.sbpro.cyberteleport.command;

import me.sbpro.cyberteleport.CyberTeleport;
import me.sbpro.cyberteleport.gui.ConfirmationMenu;
import me.sbpro.cyberteleport.message.Messages;
import me.sbpro.cyberteleport.permission.Permissions;
import me.sbpro.cyberteleport.spectate.SpectateAction;
import me.sbpro.cyberteleport.spectate.SpectateManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class SpectateCommand implements CommandExecutor {

    private final SpectateManager spectateManager;

    public SpectateCommand(CyberTeleport plugin) {
        this.spectateManager =
                plugin.getSpectateManager();
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

        if (!player.hasPermission(Permissions.SPECTATE)) {
            player.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length == 0) {
            if (!spectateManager.stopSpectating(player)) {
                player.sendMessage(Messages.notSpectating());
            }

            return true;
        }

        if (args.length != 1) {
            player.sendMessage(Messages.invalidUsage("/spectate [player]"));
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);

        if (target == null) {
            player.sendMessage(Messages.playerNotFound());
            return true;
        }

        if (target.equals(player)) {
            player.sendMessage(Messages.cannotSpectateSelf());
            return true;
        }

        ConfirmationMenu.open(
                player,
                "spectate " + target.getName(),
                new SpectateAction(
                        spectateManager,
                        player,
                        target
                )
        );

        return true;
    }
}