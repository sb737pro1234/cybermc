package me.sbpro.cyberwarp.commands.crates;

import me.sbpro.cyberwarp.Messages;
import me.sbpro.cyberwarp.warp.WarpHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CratesCommand implements CommandExecutor {

    private final WarpHelper warpHelper;

    public CratesCommand(WarpHelper warpHelper) {
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

        warpHelper.teleport(player, "crates");

        return true;
    }
}