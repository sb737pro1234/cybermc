package me.sbpro.cyber.commands.heal;

import me.sbpro.cyber.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import java.util.List;

public class HealCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // /heal
        if (args.length == 0) {

            if (!(sender instanceof Player player)) {
                sender.sendMessage(Messages.SPECIFY_PLAYER);
                return true;
            }

            if (!player.hasPermission("cyber.heal")) {
                player.sendMessage(Messages.NO_PERMISSION);
                return true;
            }

            openConfirmMenu(player, player);
            return true;
        }

        // /heal <player>
        if (args.length == 1) {

            if (!sender.hasPermission("cyber.heal.others")) {
                sender.sendMessage(Messages.NO_PERMISSION_HEAL_OTHERS);
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);

            if (target == null) {
                sender.sendMessage(Messages.PLAYER_NOT_ONLINE);
                return true;
            }

            // Console (or any non-player sender) can't open an inventory, so
            // skip the confirm menu and heal the target directly.
            if (!(sender instanceof Player player)) {
                healDirectly(sender, target);
                return true;
            }

            openConfirmMenu(player, target);
            return true;
        }

        sender.sendMessage(Messages.USAGE_HEAL);
        return true;
    }

    private void healDirectly(CommandSender sender, Player target) {

        target.setHealth(target.getAttribute(Attribute.MAX_HEALTH).getValue());
        target.setFoodLevel(20);
        target.setSaturation(20);
        target.setFireTicks(0);

        sender.sendMessage(
                Messages.healthRestoredOther(target.getName())
        );

        target.sendMessage(
                Messages.healthRestoredByStaff(String.valueOf(sender.getName()))
        );
    }

    private void openConfirmMenu(Player player, Player target) {

        Inventory heal = Bukkit.createInventory(
                new HealConfirmHolder(target),
                27,
                Messages.HEAL_CONFIRM_TITLE
        );

        ItemStack cancel = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemStack potion = new ItemStack(Material.POTION);
        ItemStack confirm = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);

        // Confirm
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.displayName(Messages.CONFIRM);

        if (target.equals(player)) {
            confirmMeta.lore(List.of(
                    Messages.HEAL_SELF_ITEM_LORE
            ));
        } else {
            confirmMeta.lore(List.of(
                    Messages.healOtherItemLore(target.getName())
            ));
        }

        confirm.setItemMeta(confirmMeta);

        // Potion
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();

        if (target.equals(player)) {
            potionMeta.displayName(Messages.HEAL_SELF_INFO_LORE);
        } else {
            potionMeta.displayName(
                    Messages.healOtherInfoLore(target.getName())
            );
        }

        potionMeta.setBasePotionType(PotionType.HEALING);
        potion.setItemMeta(potionMeta);

        // Cancel
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.displayName(Messages.CANCEL);
        cancel.setItemMeta(cancelMeta);

        heal.setItem(10, cancel);
        heal.setItem(13, potion);
        heal.setItem(16, confirm);

        player.openInventory(heal);
    }
}