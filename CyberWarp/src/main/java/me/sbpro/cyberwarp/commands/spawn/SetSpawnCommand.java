package me.sbpro.cyberwarp.commands.spawn;

import me.sbpro.cyberwarp.Messages;
import me.sbpro.cyberwarp.warp.WarpHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetSpawnCommand implements CommandExecutor {

    private final WarpHelper warpHelper;

    public SetSpawnCommand(WarpHelper warpHelper) {
        this.warpHelper = warpHelper;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(
                    Messages.PLAYER_ONLY
            );
            return true;
        }

        warpHelper.setWarp("spawn", player.getLocation());

        player.sendMessage(
                Messages.SPAWN_SET
        );

        return true;
    }
}