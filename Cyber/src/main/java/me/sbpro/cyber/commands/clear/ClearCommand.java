package me.sbpro.cyber.commands.clear;

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

public class ClearCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // /clear
        if (args.length == 0) {

            if (!(sender instanceof Player player)) {
                sender.sendMessage(Messages.SPECIFY_PLAYER);
                return true;
            }

            if (!player.hasPermission("cyber.clear.self")) {
                player.sendMessage(Messages.NO_PERMISSION);
                return true;
            }

            openConfirmMenu(player, player);
            return true;
        }

        // /clear <player>
        if (args.length == 1) {

            if (!sender.hasPermission("cyber.clear.others")) {
                sender.sendMessage(Messages.NO_PERMISSION_CLEAR_OTHERS);
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);

            if (target == null) {
                sender.sendMessage(Messages.PLAYER_NOT_ONLINE);
                return true;
            }

            // Console (or any non-player sender) can't open an inventory, so
            // skip the confirm menu and clear the target's inventory directly.
            if (!(sender instanceof Player player)) {
                clearDirectly(sender, target);
                return true;
            }

            openConfirmMenu(player, target);
            return true;
        }

        sender.sendMessage(Messages.USAGE_CLEAR);
        return true;
    }

    private void clearDirectly(CommandSender sender, Player target) {

        target.getInventory().clear();

        sender.sendMessage(
                Messages.inventoryClearedOther(target.getName())
        );

        target.sendMessage(
                Messages.inventoryClearedByStaff(String.valueOf(sender.getName()))
        );
    }

    private void openConfirmMenu(Player player, Player target) {

        Inventory clear = Bukkit.createInventory(
                new ClearConfirmHolder(target),
                27,
                Messages.CLEAR_CONFIRM_TITLE
        );

        ItemStack cancel = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemStack chest = new ItemStack(Material.CHEST);
        ItemStack confirm = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);

        // Confirm
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.displayName(Messages.CONFIRM);

        if (target.equals(player)) {
            confirmMeta.lore(List.of(
                    Messages.CLEAR_SELF_ITEM_LORE
            ));
        } else {
            confirmMeta.lore(List.of(
                    Messages.clearOtherItemLore(target.getName())
            ));
        }

        confirm.setItemMeta(confirmMeta);

        // Chest
        ItemMeta chestMeta = chest.getItemMeta();

        if (target.equals(player)) {
            chestMeta.displayName(Messages.CLEAR_SELF_INFO_LORE);
        } else {
            chestMeta.displayName(
                    Messages.clearOtherInfoLore(target.getName())
            );
        }

        chest.setItemMeta(chestMeta);

        // Cancel
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.displayName(Messages.CANCEL);
        cancel.setItemMeta(cancelMeta);

        clear.setItem(10, cancel);
        clear.setItem(13, chest);
        clear.setItem(16, confirm);

        player.openInventory(clear);
    }
}