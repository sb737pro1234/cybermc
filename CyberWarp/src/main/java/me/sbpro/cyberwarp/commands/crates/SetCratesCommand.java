package me.sbpro.cyberwarp.commands.crates;

import me.sbpro.cyberwarp.Messages;
import me.sbpro.cyberwarp.warp.WarpHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetCratesCommand implements CommandExecutor {

    private final WarpHelper warpHelper;

    public SetCratesCommand(WarpHelper warpHelper) {
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

        warpHelper.setWarp("crates", player.getLocation());

        player.sendMessage(
                Messages.CRATES_SET
        );

        return true;
    }
}