package me.sbpro.cyberteleport.gui;

import me.sbpro.cyberteleport.message.Messages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ConfirmationMenu {

    public static final int CANCEL_SLOT = 10;
    public static final int DISPLAY_SLOT = 13;
    public static final int CONFIRM_SLOT = 16;

    private static final Map<UUID, ConfirmationAction> actions = new HashMap<>();

    private ConfirmationMenu() {
    }

    public static void open(
            Player player,
            String action,
            ConfirmationAction confirmationAction
    ) {
        ConfirmationMenuHolder holder = new ConfirmationMenuHolder();
        Inventory inventory = Bukkit.createInventory(
                holder,
                27,
                Messages.CONFIRMATION_TITLE
        );
        holder.setInventory(inventory);

        inventory.setItem(
                CANCEL_SLOT,
                createItem(
                        Material.RED_STAINED_GLASS_PANE,
                        Messages.CONFIRM_CANCEL
                )
        );

        inventory.setItem(
                DISPLAY_SLOT,
                createItem(
                        Material.ENDER_PEARL,
                        Messages.confirmationAction(action)
                )
        );

        inventory.setItem(
                CONFIRM_SLOT,
                createItem(
                        Material.GREEN_STAINED_GLASS_PANE,
                        Messages.CONFIRM_ACCEPT
                )
        );

        actions.put(player.getUniqueId(), confirmationAction);

        player.openInventory(inventory);
    }

    public static ConfirmationAction getAction(Player player) {
        return actions.get(player.getUniqueId());
    }

    public static ConfirmationAction removeAction(Player player) {
        return actions.remove(player.getUniqueId());
    }

    private static ItemStack createItem(
            Material material,
            Component name
    ) {
        ItemStack item = new ItemStack(material);

        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.displayName(name.decoration(TextDecoration.ITALIC, false));
            item.setItemMeta(meta);
        }

        return item;
    }
}