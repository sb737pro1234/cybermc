package me.sbpro.cyberstaff.commands.clearchat;

import me.sbpro.cyberstaff.Messages;
import net.kyori.adventure.text.Component;
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

public final class ClearChatCommand
        implements CommandExecutor {

    private static final int CLEAR_LINES = 500;

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!sender.hasPermission(
                "cyber.staff.clearchat"
        )) {

            sender.sendMessage(
                    Messages.NO_PERMISSION
            );

            return true;
        }

        /*
         * Console does not need confirmation.
         */
        if (!(sender instanceof Player player)) {

            clearChat("Console");

            return true;
        }

        /*
         * Players must confirm.
         */
        openConfirmMenu(player);

        return true;
    }

    private void openConfirmMenu(
            Player player
    ) {

        ClearChatConfirmHolder holder =
                new ClearChatConfirmHolder();

        Inventory inventory =
                Bukkit.createInventory(
                        holder,
                        27,
                        Messages.CLEAR_CHAT_TITLE
                );

        holder.setInventory(inventory);

        /*
         * Slot 10 = CANCEL
         */
        inventory.setItem(
                10,
                item(
                        Material.RED_STAINED_GLASS_PANE,
                        Messages.CANCEL
                )
        );

        /*
         * Slot 13 = DISPLAY ITEM
         */
        ItemStack display =
                new ItemStack(
                        Material.PAPER
                );

        ItemMeta displayMeta =
                display.getItemMeta();

        if (displayMeta != null) {

            displayMeta.displayName(
                    Messages.CLEAR_CHAT_ITEM_NAME
            );

            displayMeta.lore(
                    List.of(
                            Messages.CLEAR_CHAT_LORE_1,
                            Messages.CLEAR_CHAT_LORE_2,
                            Component.empty(),
                            Messages.clearChatLoreLines(CLEAR_LINES)
                    )
            );

            display.setItemMeta(
                    displayMeta
            );
        }

        inventory.setItem(
                13,
                display
        );

        /*
         * Slot 16 = CONFIRM
         */
        inventory.setItem(
                16,
                item(
                        Material.GREEN_STAINED_GLASS_PANE,
                        Messages.CONFIRM
                )
        );

        player.openInventory(inventory);
    }

    public static void clearChat(
            String clearer
    ) {

        /*
         * Send the blank lines first.
         */
        for (int i = 0; i < CLEAR_LINES; i++) {

            Bukkit.broadcast(Component.empty());
        }

        /*
         * Then show who cleared it.
         */
        Bukkit.broadcast(
                Messages.chatClearedBy(clearer)
        );
    }

    private static ItemStack item(
            Material material,
            Component name
    ) {

        ItemStack item =
                new ItemStack(material);

        ItemMeta meta =
                item.getItemMeta();

        if (meta != null) {

            meta.displayName(name);

            item.setItemMeta(meta);
        }

        return item;
    }
}