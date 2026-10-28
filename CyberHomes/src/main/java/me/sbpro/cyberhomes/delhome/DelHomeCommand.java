package me.sbpro.cyberhomes.delhome;

import me.sbpro.cyberhomes.CyberHomes;
import me.sbpro.cyberhomes.Messages;
import me.sbpro.cyberhomes.menu.DeleteHomeMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelHomeCommand implements CommandExecutor {

    private final CyberHomes plugin;

    public DelHomeCommand(CyberHomes plugin) {
        this.plugin = plugin;
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

        if (args.length == 0) {

            player.sendMessage(Messages.usage("/delhome <name>"));

            return true;
        }

        String homeName =
                String.join(" ", args).trim();

        int homeNumber =
                plugin.getHomesManager()
                        .getHomeNumber(
                                player.getUniqueId(),
                                homeName
                        );

        if (homeNumber == -1) {

            player.sendMessage(Messages.homeMissing(homeName));

            return true;
        }

        /*
         * Open the exact same confirmation menu
         * used by the Homes GUI.
         */
        new DeleteHomeMenu(
                plugin,
                player,
                homeNumber
        ).open();

        return true;
    }
}