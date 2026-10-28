package me.sbpro.cyberalliances.gui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import me.sbpro.cyberalliances.CyberAlliances;
import me.sbpro.cyberalliances.Messages;
import me.sbpro.cyberalliances.model.Alliance;
import me.sbpro.cyberalliances.service.AllianceXpService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public final class AllianceInfoGui {
    private AllianceInfoGui() {
    }

    public static void open(CyberAlliances plugin, Player player, Alliance alliance) {
        Inventory inventory = Bukkit.createInventory(
                (InventoryHolder) new AllianceInfoGuiHolder(alliance.getName()),
                45,
                Messages.INFO_GUI_TITLE
        );

        inventory.setItem(4, createOverviewItem(plugin, alliance));

        // Leave the rest of the menu empty so the GUI has a clean look.

        // Three central action buttons.
        inventory.setItem(20, createSimpleItem(Material.PLAYER_HEAD,
                Messages.INFO_GUI_MEMBERS,
                List.of(Messages.VIEW_MEMBERS_LORE)));
        inventory.setItem(22, createSimpleItem(Material.EXPERIENCE_BOTTLE,
                Messages.INFO_GUI_LEVEL,
                Messages.levelButtonLore(alliance.getLevel(), Math.round(alliance.getXp()),
                        plugin.getXpService().getProgressBar(alliance))));
        inventory.setItem(24, createSimpleItem(Material.CHEST,
                Messages.INFO_GUI_XP_VALUES,
                List.of(Messages.VIEW_XP_VALUES_LORE)));
        inventory.setItem(40, createSimpleItem(Material.BARRIER,
                Messages.GO_BACK,
                List.of(Messages.CLOSE_MENU_LORE)));

        player.openInventory(inventory);
    }

    public static void openMembers(CyberAlliances plugin, Player player, Alliance alliance) {
        Inventory inventory = Bukkit.createInventory(
                (InventoryHolder) new AllianceMembersGuiHolder(alliance.getName()),
                27,
                Messages.MEMBERS_GUI_TITLE
        );

        List<UUID> members = new ArrayList<>(alliance.getMembers());
        members.sort(Comparator.comparing(uuid -> {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            String name = offlinePlayer.getName();
            if (uuid.equals(alliance.getOwner())) {
                return "0-" + (name == null ? uuid.toString() : name);
            }
            return "1-" + (name == null ? uuid.toString() : name);
        }, String.CASE_INSENSITIVE_ORDER));

        int slot = 0;
        for (UUID memberId : members) {
            if (slot >= 18) {
                break;
            }

            OfflinePlayer member = Bukkit.getOfflinePlayer(memberId);
            String name = member.getName() == null ? memberId.toString().substring(0, 8) : member.getName();
            List<Component> lore = Messages.memberLore(memberId.equals(alliance.getOwner()), member.isOnline());
            inventory.setItem(slot++, createPlayerHead(member, name, lore));
        }

        inventory.setItem(22, createSimpleItem(Material.ARROW,
                Messages.GO_BACK,
                List.of(Messages.RETURN_TO_INFO_LORE)));

        player.openInventory(inventory);
    }

    private static ItemStack createOverviewItem(CyberAlliances plugin, Alliance alliance) {
        AllianceXpService xpService = plugin.getXpService();
        OfflinePlayer owner = Bukkit.getOfflinePlayer(alliance.getOwner());
        String ownerName = owner.getName() == null ? alliance.getOwner().toString() : owner.getName();

        List<Component> lore = Messages.allianceOverviewLore(ownerName, alliance.getMembers().size(),
                alliance.getLevel(), Math.round(alliance.getXp()), xpService.getProgressBar(alliance));

        return createSimpleItem(Material.GRASS_BLOCK,
                Messages.allianceItemName(alliance.getName()),
                lore);
    }

    private static ItemStack createPlayerHead(OfflinePlayer player, String name, List<Component> lore) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(player);
        meta.displayName(Messages.memberHeadName(name));
        meta.lore(lore.stream().map(line -> line.decoration(TextDecoration.ITALIC, false)).toList());
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack createSimpleItem(Material material, Component name, List<Component> lore) {
        return AllianceItems.simple(material, name, lore);
    }
}
