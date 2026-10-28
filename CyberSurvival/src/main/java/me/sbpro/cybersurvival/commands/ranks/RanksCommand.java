package me.sbpro.cybersurvival.commands.ranks;

import me.sbpro.cybersurvival.Messages;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class RanksCommand implements CommandExecutor {

    private static final TextColor PLAYER_COLOR = NamedTextColor.WHITE;
    private static final TextColor JESTER_COLOR = TextColor.color(0x6EE7B7);
    private static final TextColor SQUIRE_COLOR = TextColor.color(0x60A5FA);
    private static final TextColor BISHOP_COLOR = TextColor.color(0xA78BFA);
    private static final TextColor KNIGHT_COLOR = TextColor.color(0xF472B6);
    private static final TextColor LORD_COLOR = TextColor.color(0xFB923C);
    private static final TextColor KING_COLOR = TextColor.color(0xFDE047);
    private static final TextColor GRASS_COLOR = TextColor.color(0x338650);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Messages.PLAYER_ONLY);
            return true;
        }

        RanksMenuHolder holder = new RanksMenuHolder();
        Inventory ranks = Bukkit.createInventory(holder, 45, Messages.RANKS_TITLE);
        holder.setInventory(ranks);

        ranks.setItem(10, rank(PLAYER_COLOR, "Player",
                "Can access to 2 homes.",
                "Can select a cosmetic tag.",
                "Can rename items in an anvil using color codes.",
                "Can change nickname."));
        ranks.setItem(12, rank(JESTER_COLOR, "Jester",
                "Can use chat colors.",
                "Can clear own inventory.",
                "Can use the /craft command."));
        ranks.setItem(14, rank(SQUIRE_COLOR, "Squire",
                "Can hideplayers from view.",
                "Can use the /nightvision command.",
                "Can use the /smithingtable command.",
                "Can use the /stonecutter command.",
                "Can use the /cartographytable command.",
                "Can use the /loom command.",
                "Can use the /rename command.",
                "Can use the /anvil command."));
        ranks.setItem(16, rank(BISHOP_COLOR, "Bishop",
                "Can set up to 3 homes.",
                "Can use fastplace with a range of 16 blocks."));
        ranks.setItem(28, rank(KNIGHT_COLOR, "Knight",
                "Join and leave messages visible by all players.",
                "Can set up to 4 homes.",
                "Can use fastplace with a range of 32 blocks."));
        ranks.setItem(30, rank(LORD_COLOR, "Lord",
                "Can set up to 5 homes.",
                "Can use up to 4 enderchest rows.",
                "Can use fastplace with a range of 64 blocks."));
        ranks.setItem(32, rank(KING_COLOR, "King",
                "Can set up to 6 homes.",
                "Can use up to 5 enderchest rows.",
                "Can use fastplace with a range of 128 blocks."));
        ranks.setItem(34, rank(GRASS_COLOR, "Grass",
                "Can set up to 7 homes.",
                "Can use up to 6 enderchest rows."));

        player.openInventory(ranks);
        return true;
    }

    private ItemStack rank(TextColor color, String name, String... perks) {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Messages.rankName(color, name));
        meta.lore(Messages.rankPerks(color, perks));
        item.setItemMeta(meta);
        return item;
    }
}
