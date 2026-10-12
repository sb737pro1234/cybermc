package me.sbpro.grassgghomes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class ViewHomesMenu {

    private static final Map<UUID, UUID> OPEN_VIEWS = new HashMap<>();

    private final GrassGGHomes plugin;
    private final UUID targetId;
    private final String targetName;
    private final Inventory inventory;

    public ViewHomesMenu(
            GrassGGHomes plugin,
            UUID targetId,
            String targetName
    ) {
        this.plugin = plugin;
        this.targetId = targetId;
        this.targetName = targetName;

        this.inventory = Bukkit.createInventory(
                null,
                27,
                "§2Homes View §8» §f" + targetName
        );

        int[] slots = {10, 11, 12, 13, 14, 15, 16};

        for (int number = 1; number <= 7; number++) {

            Home home = plugin.getHomesManager().getHome(
                    targetId,
                    number
            );

            ItemStack item = new ItemStack(
                    home == null
                            ? Material.GRAY_BED
                            : Material.GREEN_BED
            );

            ItemMeta meta = item.getItemMeta();

            if (meta != null) {

                if (home == null) {

                    meta.setDisplayName("§7Home " + number);

                    meta.setLore(List.of(
                            "§7",
                            "§cThis home has not been set.",
                            "§7",
                            "§fUse the button below to create it."
                    ));

                } else {

                    meta.setDisplayName("§2" + home.getName());

                    meta.setLore(List.of(
                            "§7",
                            "§fClick to teleport.",
                            "§7",
                            "§2Teleport takes 5 seconds."
                    ));
                }

                item.setItemMeta(meta);
            }

            inventory.setItem(slots[number - 1], item);
        }
    }

    public void open(Player viewer) {

        OPEN_VIEWS.put(viewer.getUniqueId(), targetId);

        viewer.openInventory(inventory);
    }

    public static UUID getTarget(UUID viewerId) {

        return OPEN_VIEWS.get(viewerId);
    }

    public static void close(Player viewer) {

        OPEN_VIEWS.remove(viewer.getUniqueId());
    }
}