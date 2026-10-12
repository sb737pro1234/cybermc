package me.sbpro.grassgg.commands.rules;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class RulesCommand implements CommandExecutor {
    
    public static final String CUSTOM = "§x§2§9§7§9§F§F";
    public static final String WHITE = "§f";
    public static final String POINT = CUSTOM +"◆ ";
    public static final String TITLE = CUSTOM + "Rules";


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        Inventory inventory = player.getServer().createInventory(null, 27, TITLE);

        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta meta = book.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(CUSTOM + "Rules");

            meta.setLore(Arrays.asList(
                    POINT + WHITE + "No chat spam, symbols or caps abuse",
                    POINT + WHITE + "English only, no swearing or inappropriate topics",
                    POINT + WHITE + "No disrespect, harassment or toxicity",
                    POINT + WHITE + "No scamming, fake messages or impersonation",
                    POINT + WHITE + "No advertising or hackusating",
                    POINT + WHITE + "No disallowed mods, hacking, ESP or Xray",
                    POINT + WHITE + "No glitch/exploit abuse or duping",
                    POINT + WHITE + "No lag machines, bad builds or casinos",
                    POINT + WHITE + "No alt abuse, autoclicking or macros",
                    POINT + WHITE + "No teleport trapping, command or report abuse",
                    POINT + WHITE + "No racism, threats, IRL scamming or leaking info",
                    "",
                    CUSTOM + "For more details on the rules. Click here."
            ));

            book.setItemMeta(meta);
        }

        inventory.setItem(13, book);

        player.openInventory(inventory);

        return true;
    }
}