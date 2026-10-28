package me.sbpro.cyberstaff.sus;

import me.sbpro.cyberstaff.Messages;
import org.bukkit.Bukkit;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.TooltipDisplay;

import java.util.ArrayList;
import java.util.List;

public final class SusMenu {

    private SusMenu() {
    }

    public static void open(
            Player player,
            SusManager manager,
            int requestedPage
    ) {

        List<SusPlayer> players =
                manager.getPlayers();

        int totalPages =
                Math.max(
                        1,
                        (int) Math.ceil(
                                players.size() / 45.0
                        )
                );

        int page =
                Math.max(
                        0,
                        Math.min(
                                requestedPage,
                                totalPages - 1
                        )
                );

        SusMenuHolder holder =
                new SusMenuHolder(
                        page,
                        players
                );

        Inventory inventory =
                Bukkit.createInventory(
                        holder,
                        54,
                        Messages.susPlayersTitle((page + 1))
                );

        int start =
                page * 45;

        int end =
                Math.min(
                        start + 45,
                        players.size()
                );

        for (int index = start; index < end; index++) {

            SusPlayer susPlayer =
                    players.get(index);

            int slot =
                    index - start;

            ItemStack skull =
                    new ItemStack(Material.PLAYER_HEAD);

            SkullMeta meta =
                    (SkullMeta) skull.getItemMeta();

            if (meta == null) {
                continue;
            }

            meta.setOwningPlayer(
                    Bukkit.getOfflinePlayer(
                            susPlayer.uuid()
                    )
            );

            skull.setData(
                    DataComponentTypes.TOOLTIP_DISPLAY,
                    TooltipDisplay.tooltipDisplay()
                            .addHiddenComponents(
                                    DataComponentTypes.PROFILE
                            )
                            .build()
            );

            meta.displayName(
                    Messages.susPlayerName(susPlayer.name())
            );

            List<Component> lore =
                    new ArrayList<>();

            /*
             * SUS reasons.
             */
            for (SusNote note :
                    susPlayer.notes()) {

                lore.add(
                        Messages.susNoteReason(note.reason())
                );
            }

            lore.add(Component.empty());

            /*
             * Controls.
             */
            lore.add(
                    Messages.SUS_LORE_SPECTATE
            );

            lore.add(
                    Messages.SUS_LORE_ADD
            );

            lore.add(
                    Messages.SUS_LORE_DELETE
            );

            meta.lore(lore);

            skull.setItemMeta(meta);

            inventory.setItem(
                    slot,
                    skull
            );
        }

        /*
         * Previous page.
         */
        if (page > 0) {

            inventory.setItem(
                    45,
                    item(
                            Material.ARROW,
                            Messages.SUS_PREVIOUS_PAGE
                    )
            );
        }

        /*
         * Page indicator.
         */
        inventory.setItem(
                49,
                item(
                        Material.BOOK,
                        Messages.susPage((page + 1), totalPages)
                )
        );

        /*
         * Next page.
         */
        if (page < totalPages - 1) {

            inventory.setItem(
                    53,
                    item(
                            Material.ARROW,
                            Messages.SUS_NEXT_PAGE
                    )
            );
        }

        player.openInventory(inventory);
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