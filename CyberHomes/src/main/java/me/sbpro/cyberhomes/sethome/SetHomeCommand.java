package me.sbpro.cyberhomes.sethome;

import me.sbpro.cyberhomes.CyberHomes;
import me.sbpro.cyberhomes.Messages;
import me.sbpro.cyberhomes.model.Home;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SetHomeCommand implements CommandExecutor {

    private final CyberHomes plugin;

    public SetHomeCommand(CyberHomes plugin) {
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

            player.sendMessage(Messages.usage("/sethome <name>"));

            return true;
        }

        String name = String.join(" ", args).trim();

        if (name.isEmpty()) {

            player.sendMessage(Messages.PROVIDE_HOME_NAME);

            return true;
        }

        if (name.length() > 24) {

            player.sendMessage(Messages.nameTooLong(24));

            return true;
        }

        if (plugin.getHomesManager().getHomeByName(
                player.getUniqueId(),
                name
        ) != null) {

            player.sendMessage(Messages.alreadyHaveHome(name));

            return true;
        }

        int maxHomes = getHomeLimit(player);

        if (maxHomes == 0) {

            player.sendMessage(Messages.NO_PERMISSION);

            return true;
        }

        int nextHome =
                plugin.getHomesManager().getNextAvailableHome(
                        player.getUniqueId(),
                        maxHomes
                );

        if (nextHome == -1) {

            player.sendMessage(Messages.NO_HOME_SLOTS);

            return true;
        }

        Location location = player.getLocation();

        if (!isAllowedWorld(location.getWorld())) {

            player.sendMessage(Messages.CANNOT_SET_HOME_HERE);

            return true;
        }

        plugin.getHomesManager().setHome(
                player.getUniqueId(),
                nextHome,
                location
        );

        Home home =
                plugin.getHomesManager().getHome(
                        player.getUniqueId(),
                        nextHome
                );

        if (home != null) {
            home.setName(name);
            plugin.getHomesManager().saveHomes();
        }

        player.sendMessage(Messages.homeCreatedNamed(name));

        return true;
    }

    private int getHomeLimit(Player player) {

        for (int i = 7; i >= 1; i--) {

            if (player.hasPermission(
                    "cyber.homes." + i
            )) {

                return i;
            }
        }

        return 0;
    }

    private boolean isAllowedWorld(World world) {

        return world != null
                && List.of(
                "world",
                "world_nether",
                "world_the_end"
        ).contains(world.getName());
    }
}