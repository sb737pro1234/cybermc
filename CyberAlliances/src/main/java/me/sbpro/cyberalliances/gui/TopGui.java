package me.sbpro.cyberalliances.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.sbpro.cyberalliances.CyberAlliances;
import me.sbpro.cyberalliances.Messages;
import me.sbpro.cyberalliances.model.Alliance;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * Paginated Alliance Top menu.
 *
 * <pre>
 *  Slots 0-44   alliances for this page (45 per page, ranked by XP)
 *  Slot  45     Previous Page
 *  Slot  49     Your Alliance (click to jump to your page)
 *  Slot  53     Next Page
 * </pre>
 */
public final class TopGui {

    public static final int PAGE_SIZE = 45;
    public static final int SLOT_PREVIOUS = 45;
    public static final int SLOT_YOU = 49;
    public static final int SLOT_NEXT = 53;

    private TopGui() {
    }

    public static void open(CyberAlliances plugin, Player player, int page) {
        List<Alliance> top = plugin.getXpService().getTopAlliances();
        int totalPages = totalPages(top.size());
        int safePage = Math.max(0, Math.min(page, totalPages - 1));

        Inventory inventory = Bukkit.createInventory(
                (InventoryHolder) new TopGuiHolder(safePage),
                54,
                Messages.topTitle(safePage, totalPages)
        );

        Optional<Alliance> own = plugin.getAllianceStorage().getAllianceByPlayer(player.getUniqueId());
        String ownName = own.map(Alliance::getName).orElse(null);

        // Alliances: slots 0-44.
        int start = safePage * PAGE_SIZE;
        int end = Math.min(top.size(), start + PAGE_SIZE);
        for (int index = start; index < end; index++) {
            Alliance alliance = top.get(index);
            boolean yours = ownName != null && ownName.equalsIgnoreCase(alliance.getName());
            inventory.setItem(index - start, createAllianceItem(plugin, alliance, index + 1, yours));
        }

        // Bottom row: Previous / You / Next.
        inventory.setItem(SLOT_PREVIOUS, AllianceItems.simple(
                Material.ARROW,
                Messages.PREVIOUS_PAGE,
                List.of(Messages.OPEN_PREVIOUS_PAGE_LORE)));
        inventory.setItem(SLOT_YOU, createYouItem(player, own.orElse(null), top));
        inventory.setItem(SLOT_NEXT, AllianceItems.simple(
                Material.ARROW,
                Messages.NEXT_PAGE,
                List.of(Messages.OPEN_NEXT_PAGE_LORE)));

        player.openInventory(inventory);
    }

    /** Opens the page that contains the player's own alliance. Returns false if they have none. */
    public static boolean openOwnPage(CyberAlliances plugin, Player player) {
        Optional<Alliance> own = plugin.getAllianceStorage().getAllianceByPlayer(player.getUniqueId());
        if (own.isEmpty()) {
            return false;
        }
        int rank = rankOf(plugin.getXpService().getTopAlliances(), own.get());
        if (rank < 1) {
            return false;
        }
        open(plugin, player, (rank - 1) / PAGE_SIZE);
        return true;
    }

    /** Highest valid page index (0-based) right now. */
    public static int getMaxPage(CyberAlliances plugin) {
        return totalPages(plugin.getXpService().getTopAlliances().size()) - 1;
    }

    private static int totalPages(int alliances) {
        return Math.max(1, (alliances + PAGE_SIZE - 1) / PAGE_SIZE);
    }

    /** 1-based rank of the alliance, or -1 if it is not in the list. */
    private static int rankOf(List<Alliance> top, Alliance alliance) {
        for (int index = 0; index < top.size(); index++) {
            if (top.get(index).getName().equalsIgnoreCase(alliance.getName())) {
                return index + 1;
            }
        }
        return -1;
    }

    private static String nameOf(UUID id) {
        String name = Bukkit.getOfflinePlayer(id).getName();
        return name == null ? id.toString().substring(0, 8) : name;
    }

    /** Member names with the owner first, then everyone else alphabetically. */
    private static List<String> memberNames(Alliance alliance) {
        List<String> names = new ArrayList<>();
        names.add(nameOf(alliance.getOwner()));
        alliance.getMembers().stream()
                .filter(id -> !id.equals(alliance.getOwner()))
                .map(TopGui::nameOf)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .forEach(names::add);
        return names;
    }

    private static ItemStack createAllianceItem(CyberAlliances plugin, Alliance alliance, int rank, boolean yours) {
        List<Component> lore = Messages.topEntryLore(
                nameOf(alliance.getOwner()),
                memberNames(alliance),
                alliance.getLevel(),
                Math.round(alliance.getXp()),
                plugin.getXpService().getProgressBar(alliance),
                yours);

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(alliance.getOwner()));
        meta.displayName(Messages.topEntryName(rank, alliance.getName()));
        meta.lore(lore.stream().map(line -> line.decoration(TextDecoration.ITALIC, false)).toList());
        if (yours) {
            meta.setEnchantmentGlintOverride(true);
        }
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack createYouItem(Player player, Alliance own, List<Alliance> top) {
        List<Component> lore;
        if (own == null) {
            lore = List.of(Messages.TOP_NOT_IN_ALLIANCE_LORE);
        } else {
            lore = Messages.topYouLore(
                    rankOf(top, own),
                    top.size(),
                    own.getLevel(),
                    Math.round(own.getXp()));
        }

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(player);
        meta.displayName(Messages.TOP_YOU_BUTTON);
        meta.lore(lore.stream().map(line -> line.decoration(TextDecoration.ITALIC, false)).toList());
        item.setItemMeta(meta);
        return item;
    }
}