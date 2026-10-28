package me.sbpro.cyber.commands.feed;

import me.sbpro.cyber.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class FeedCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // /feed
        if (args.length == 0) {

            if (!(sender instanceof Player player)) {
                sender.sendMessage(Messages.SPECIFY_PLAYER);
                return true;
            }

            if (!player.hasPermission("cyber.feed")) {
                player.sendMessage(Messages.NO_PERMISSION);
                return true;
            }

            openConfirmMenu(player, player);
            return true;
        }

        // /feed <player>
        if (args.length == 1) {

            if (!sender.hasPermission("cyber.feed.others")) {
                sender.sendMessage(Messages.NO_PERMISSION_FEED_OTHERS);
                return true;
            }
// 
            Player target = Bukkit.getPlayerExact(args[0]);

            if (target == null) {
                sender.sendMessage(Messages.PLAYER_NOT_ONLINE);
                return true;
            }

            // Console (or any non-player sender) can't open an inventory, so
            // skip the confirm menu and feed the target directly.
            if (!(sender instanceof Player player)) {
                feedDirectly(sender, target);
                return true;
            }

            openConfirmMenu(player, target);
            return true;
        }

        sender.sendMessage(Messages.USAGE_FEED);
        return true;
    }

    private void feedDirectly(CommandSender sender, Player target) {

        target.setFoodLevel(20);
        target.setSaturation(20);

        sender.sendMessage(
                Messages.hungerRestoredOther(target.getName())
        );

        target.sendMessage(
                Messages.hungerRestoredByStaff(String.valueOf(sender.getName()))
        );
    }

    private void openConfirmMenu(Player player, Player target) {

        Inventory feed = Bukkit.createInventory(
                new FeedConfirmHolder(target),
                27,
                Messages.FEED_CONFIRM_TITLE
        );

        ItemStack cancel = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemStack food = new ItemStack(Material.COOKED_BEEF);
        ItemStack confirm = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);

        // Confirm
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.displayName(Messages.CONFIRM);

        if (target.equals(player)) {
            confirmMeta.lore(List.of(
                    Messages.FEED_SELF_ITEM_LORE
            ));
        } else {
            confirmMeta.lore(List.of(
                    Messages.feedOtherItemLore(target.getName())
            ));
        }

        confirm.setItemMeta(confirmMeta);

        // Food
        ItemMeta foodMeta = food.getItemMeta();

        if (target.equals(player)) {
            foodMeta.displayName(Messages.FEED_SELF_INFO_LORE);
        } else {
            foodMeta.displayName(
                    Messages.feedOtherInfoLore(target.getName())
            );
        }

        food.setItemMeta(foodMeta);

        // Cancel
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.displayName(Messages.CANCEL);
        cancel.setItemMeta(cancelMeta);

        feed.setItem(10, cancel);
        feed.setItem(13, food);
        feed.setItem(16, confirm);

        player.openInventory(feed);
    }
}