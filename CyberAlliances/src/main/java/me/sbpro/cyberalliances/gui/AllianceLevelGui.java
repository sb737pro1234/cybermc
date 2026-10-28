package me.sbpro.cyberalliances.gui;

import me.sbpro.cyberalliances.CyberAlliances;
import me.sbpro.cyberalliances.Messages;
import me.sbpro.cyberalliances.model.Alliance;
import me.sbpro.cyberalliances.service.AllianceXpService;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class AllianceLevelGui {

    private static final int PAGE_SIZE = 45;

    private AllianceLevelGui() {
    }

    public static void openMain(
            CyberAlliances plugin,
            Player player,
            Alliance alliance
    ) {
        Inventory inventory = Bukkit.createInventory(
                new AllianceLevelGuiHolder(alliance.getName()),
                27,
                Messages.LEVEL_TITLE
        );

        inventory.setItem(
                13,
                createAllianceInfoItem(plugin, alliance)
        );

        inventory.setItem(
                22,
                createSimpleItem(
                        Material.ARROW,
                        Messages.GO_BACK,
                        List.of(Messages.RETURN_TO_INFO_LORE)
                )
        );

        // Alliance XP Sources button
        inventory.setItem(
                26,
                createSimpleItem(
                        Material.CHEST,
                        Messages.XP_VALUES_BUTTON,
                        List.of(Messages.VIEW_ALL_XP_SOURCES_LORE)
                )
        );

        player.openInventory(inventory);
    }

    public static void openXpValues(
            CyberAlliances plugin,
            Player player,
            Alliance alliance,
            int page
    ) {
        Inventory inventory = Bukkit.createInventory(
                new AllianceXpValuesGuiHolder(alliance.getName(), page),
                54,
                Messages.XP_VALUES_TITLE
        );

        List<AllianceXpService.XpEntry> entries =
                plugin.getXpService().getDisplayEntries();

        int start = page * PAGE_SIZE;
        int end = Math.min(entries.size(), start + PAGE_SIZE);

        /*
         * XP entries start on row 2.
         * This leaves the top row for navigation.
         */
        int slot = 9;

        for (int index = start; index < end; index++) {
            inventory.setItem(
                    slot++,
                    createXpEntryItem(entries.get(index))
            );
        }

        // Previous page
        inventory.setItem(
                0,
                createSimpleItem(
                        Material.ARROW,
                        Messages.PREVIOUS_PAGE,
                        List.of(Messages.OPEN_PREVIOUS_PAGE_LORE)
                )
        );

        // Back
        inventory.setItem(
                4,
                createSimpleItem(
                        Material.BARRIER,
                        Messages.GO_BACK,
                        List.of(Messages.RETURN_TO_LEVEL_LORE)
                )
        );

        // Next page
        inventory.setItem(
                8,
                createSimpleItem(
                        Material.ARROW,
                        Messages.NEXT_PAGE,
                        List.of(Messages.OPEN_NEXT_PAGE_LORE)
                )
        );

        player.openInventory(inventory);
    }

    public static int getMaxPage(CyberAlliances plugin) {
        int size = plugin.getXpService().getDisplayEntries().size();

        return Math.max(
                0,
                (size - 1) / PAGE_SIZE
        );
    }

    private static ItemStack createAllianceInfoItem(
            CyberAlliances plugin,
            Alliance alliance
    ) {
        AllianceXpService xpService = plugin.getXpService();

        List<Component> lore = Messages.levelOverviewLore(
                alliance.getName(),
                alliance.getLevel(),
                Math.round(alliance.getXp()),
                xpService.getProgressBar(alliance)
        );

        return createSimpleItem(
                Material.GRASS_BLOCK,
                Messages.LEVEL_GUI_TITLE,
                lore
        );
    }

    private static ItemStack createXpEntryItem(
            AllianceXpService.XpEntry entry
    ) {
        return createSimpleItem(
                entry.iconMaterial(),
                entry.displayName(),
                Messages.xpEntryLore(entry.prettyCategory(), Math.round(entry.xp()))
        );
    }

    private static ItemStack createSimpleItem(
            Material material,
            Component name,
            List<Component> lore
    ) {
        return AllianceItems.simple(material, name, lore);
    }
}
