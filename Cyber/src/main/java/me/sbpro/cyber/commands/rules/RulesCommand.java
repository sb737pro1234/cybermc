package me.sbpro.cyber.commands.rules;

import me.sbpro.cyber.Messages;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class RulesCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Messages.PLAYER_ONLY);
            return true;
        }

        RulesMenuHolder holder = new RulesMenuHolder();
        Inventory inventory = player.getServer().createInventory(holder, 27, Messages.RULES_TITLE);
        holder.setInventory(inventory);

        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.displayName(Messages.RULES_BOOK_NAME);
        meta.lore(Messages.RULES_LORE);
        book.setItemMeta(meta);

        inventory.setItem(13, book);

        player.openInventory(inventory);

        return true;
    }
}
