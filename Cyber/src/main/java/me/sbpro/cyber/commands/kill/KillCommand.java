package me.sbpro.cyber.commands.kill;

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

public class KillCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("cyber.kill.self")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        // /kill
        if (args.length == 0) {

            if (!(sender instanceof Player player)) {
                sender.sendMessage(Messages.SPECIFY_PLAYER);
                return true;
            }

            openConfirmMenu(player, player);
            return true;
        }

        // /kill <player>
        if (args.length == 1) {

            if (!sender.hasPermission("cyber.kill.others")) {
                sender.sendMessage(Messages.NO_PERMISSION_KILL_OTHERS);
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);

            if (target == null) {
                sender.sendMessage(Messages.PLAYER_NOT_ONLINE);
                return true;
            }

            // Console (or any non-player sender) can't open an inventory, so
            // skip the confirm menu and kill the target directly.
            if (!(sender instanceof Player player)) {
                killDirectly(sender, target);
                return true;
            }

            openConfirmMenu(player, target);
            return true;
        }

        sender.sendMessage(Messages.USAGE_KILL);
        return true;
    }

    private void killDirectly(CommandSender sender, Player target) {

        target.setHealth(0);

        sender.sendMessage(
                Messages.killedOther(target.getName())
        );

        target.sendMessage(
                Messages.killedByStaff(String.valueOf(sender.getName()))
        );
    }

    private void openConfirmMenu(Player player, Player target) {

        Inventory death = Bukkit.createInventory(
                new KillConfirmHolder(target),
                27,
                Messages.KILL_CONFIRM_TITLE
        );

        ItemStack cancel = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemStack skull = new ItemStack(Material.SKELETON_SKULL);
        ItemStack confirm = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);

        // Confirm
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.displayName(Messages.CONFIRM);

        if (target.equals(player)) {
            confirmMeta.lore(List.of(
                    Messages.KILL_SELF_ITEM_LORE,
                    Messages.KILL_NO_REFUND_LORE

            ));
        } else {
            confirmMeta.lore(List.of(
                    Messages.killOtherItemLore(target.getName())
            ));
        }

        confirm.setItemMeta(confirmMeta);

        // Skull
        ItemMeta skullMeta = skull.getItemMeta();

        if (target.equals(player)) {
            skullMeta.displayName(Messages.KILL_SELF_INFO_LORE);
        } else {
            skullMeta.displayName(
                    Messages.killOtherInfoLore(target.getName())
            );
        }

        skull.setItemMeta(skullMeta);

        // Cancel
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.displayName(Messages.CANCEL);
        cancel.setItemMeta(cancelMeta);

        death.setItem(10, cancel);
        death.setItem(13, skull);
        death.setItem(16, confirm);

        player.openInventory(death);
    }
}