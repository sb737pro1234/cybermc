package me.sbpro.cybersurvival.commands;

import me.sbpro.cybersurvival.Messages;

import me.sbpro.cybersurvival.enchants.AutoSmeltEnchant;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CustomEnchantCommand implements CommandExecutor {

    private final AutoSmeltEnchant autoSmelt;

    public CustomEnchantCommand(AutoSmeltEnchant autoSmelt) {
        this.autoSmelt = autoSmelt;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("cyber.admin")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length != 3 || !args[0].equalsIgnoreCase("give")) {
            sender.sendMessage(Messages.USAGE_CUSTOM_ENCHANT);
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            sender.sendMessage(Messages.PLAYER_NOT_FOUND);
            return true;
        }

        ItemStack item = target.getInventory().getItemInMainHand();

        if (item == null || item.getType() == Material.AIR) {
            sender.sendMessage(Messages.NOT_HOLDING_ITEM);
            return true;
        }

        switch (args[2].toLowerCase()) {

            case "autosmelt":

                if (!item.getType().name().endsWith("_PICKAXE")) {
                    sender.sendMessage(Messages.AUTOSMELT_PICKAXES_ONLY);
                    return true;
                }

                if (autoSmelt.hasEnchant(item)) {
                    sender.sendMessage(Messages.ALREADY_HAS_AUTOSMELT);
                    return true;
                }

                autoSmelt.apply(item);

                sender.sendMessage(Messages.enchantApplied(target.getName()));
                target.sendMessage(Messages.PICKAXE_ENCHANTED);

                break;

            default:
                sender.sendMessage(Messages.UNKNOWN_ENCHANT);
                break;
        }

        return true;
    }
}