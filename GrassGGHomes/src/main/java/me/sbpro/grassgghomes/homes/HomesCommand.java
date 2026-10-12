package me.sbpro.grassgghomes.homes;

import me.sbpro.grassgghomes.GrassGGHomes;
import me.sbpro.grassgghomes.Home;
import me.sbpro.grassgghomes.HomesMenu;
import me.sbpro.grassgghomes.Messages;
import me.sbpro.grassgghomes.TeleportManager;
import me.sbpro.grassgghomes.ViewHomesMenu;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomesCommand implements CommandExecutor {
    private final GrassGGHomes plugin;
    public HomesCommand(GrassGGHomes plugin) { this.plugin = plugin; }

    @Override public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) { sender.sendMessage(Messages.PLAYER_ONLY); return true; }
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!player.hasPermission("grassgg.admin")) { player.sendMessage(Messages.NO_PERMISSION); return true; }
            plugin.getHomesManager().reloadHomes(); player.sendMessage(Messages.PREFIX + "Homes reloaded."); return true;
        }
        if (args.length == 0) { new HomesMenu(plugin, player, player).open(); return true; }
        if (args[0].equalsIgnoreCase("view")) {
            if (!player.hasPermission("grassgg.homes.staff")) { player.sendMessage(Messages.NO_PERMISSION); return true; }
            if (args.length != 2) { player.sendMessage(Messages.usage("/homes view <player>")); return true; }
            String name = args[1];
            Player online = Bukkit.getPlayerExact(name);
            OfflinePlayer target = online != null ? online : Bukkit.getOfflinePlayerIfCached(name);
            if (target == null || (!target.hasPlayedBefore() && !target.isOnline())) { player.sendMessage(Messages.playerMissing(name)); return true; }
            String targetName = target.getName() == null ? name : target.getName();
            new ViewHomesMenu(plugin, target.getUniqueId(), targetName).open(player);
            return true;
        }
        // Any other argument is exclusively treated as one of the sender's own home names.
        String homeName = String.join(" ", args).trim();
        Home home = plugin.getHomesManager().getHomeByName(player.getUniqueId(), homeName);
        if (home == null) { player.sendMessage(Messages.homeMissing(homeName)); return true; }
        player.closeInventory(); player.sendMessage(Messages.PREFIX + "Teleporting to §2" + homeName + "§f.");
        new TeleportManager(plugin, player, home.getLocation()).start();
        return true;
    }
}
