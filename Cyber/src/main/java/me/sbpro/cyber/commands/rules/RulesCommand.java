package me.sbpro.cyber.commands.rules;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import me.sbpro.cyber.Messages;
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

        for (RulesCategory category : RulesCategory.values()) {
            inventory.setItem(category.slot(), createItem(category));
        }

        player.openInventory(inventory);

        return true;
    }

    private static ItemStack createItem(RulesCategory category) {
        ItemStack item = new ItemStack(category.material());
        ItemMeta meta = item.getItemMeta();
        meta.displayName(category.displayName());
        meta.lore(category.lore());
        item.setItemMeta(meta);

        // Hide the shulker box's "Empty" contents line
        item.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay()
                .addHiddenComponents(DataComponentTypes.CONTAINER)
                .build());

        return item;
    }
}
