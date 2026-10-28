package me.sbpro.cyberteleport.command;

import me.sbpro.cyberteleport.message.Messages;
import me.sbpro.cyberteleport.CyberTeleport;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class TpallCommand implements CommandExecutor {

    private final TeleportCommand teleportCommand;

    public TpallCommand(CyberTeleport plugin) {
        this.teleportCommand = new TeleportCommand(plugin);
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

        return teleportCommand.executeTpall(player);
    }
}