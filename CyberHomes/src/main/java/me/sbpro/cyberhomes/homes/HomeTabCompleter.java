package me.sbpro.cyberhomes.homes;

import me.sbpro.cyberhomes.CyberHomes;
import me.sbpro.cyberhomes.model.Home;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeTabCompleter implements TabCompleter {
    private final CyberHomes plugin;
    public HomeTabCompleter(CyberHomes plugin) { this.plugin = plugin; }
    @Override public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player player)) return Collections.emptyList();
        List<String> results = new ArrayList<>();
        if (args.length == 1) {
            for (Home home : plugin.getHomesManager().getHomes(player.getUniqueId()).values()) if (home.getName() != null) results.add(home.getName());
            if (player.hasPermission("cyber.homes.staff")) results.add("view");
            String input = args[0].toLowerCase(); results.removeIf(s -> !s.toLowerCase().startsWith(input));
        } else if (args.length == 2 && args[0].equalsIgnoreCase("view") && player.hasPermission("cyber.homes.staff")) {
            for (Player online : Bukkit.getOnlinePlayers()) results.add(online.getName());
            for (OfflinePlayer offline : Bukkit.getOfflinePlayers()) if (offline.getName() != null && offline.hasPlayedBefore()) results.add(offline.getName());
            String input = args[1].toLowerCase(); results.removeIf(s -> !s.toLowerCase().startsWith(input));
        } else return Collections.emptyList();
        results = new ArrayList<>(new java.util.LinkedHashSet<>(results));
        Collections.sort(results, String.CASE_INSENSITIVE_ORDER); return results;
    }
}
