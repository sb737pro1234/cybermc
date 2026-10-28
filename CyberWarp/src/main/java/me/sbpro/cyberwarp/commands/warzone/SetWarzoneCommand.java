package me.sbpro.cyberwarp.commands.warzone;

import me.sbpro.cyberwarp.Messages;
import me.sbpro.cyberwarp.warp.WarpHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetWarzoneCommand implements CommandExecutor {

    private final WarpHelper warpHelper;

    public SetWarzoneCommand(WarpHelper warpHelper) {
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

        warpHelper.setWarp("warzone", player.getLocation());

        player.sendMessage(
                Messages.WARZONE_SET
        );

        return true;
    }
}