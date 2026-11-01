package me.sbpro.cyberstatistics.stats;

import me.sbpro.cyberstatistics.Messages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class StatisticsMenu {

    private static final int MAIN_SIZE = 45;
    private static final int CATEGORY_SIZE = 27;

    private static final int SUMMARY_SLOT = 4;
    private static final int MAIN_CLOSE_SLOT = 40;
    private static final int CATEGORY_BACK_SLOT = 22;

    /** Slots used for 0-3 statistics, centred on the middle row of a category menu. */
    private static final int[][] STAT_SLOTS = {{}, {13}, {12, 14}, {11, 13, 15}};

    private static final LegacyComponentSerializer LEGACY_PREFIX = LegacyComponentSerializer.builder()
            .character('&')
            .hexColors()
            .build();

    private enum Category {
        COMBAT("Combat", "Player kills, mob kills and deaths", Material.DIAMOND_SWORD, 19),
        MINING("Mining", "Mining and block-breaking statistics", Material.DIAMOND_PICKAXE, 20),
        BUILDING("Building", "Building and crafting statistics", Material.BRICKS, 21),
        EXPLORATION("Exploration", "Travel and exploration statistics", Material.COMPASS, 22),
        FARMING("Farming", "Farming and animal statistics", Material.WHEAT, 23),
        FISHING("Fishing", "Fishing statistics", Material.FISHING_ROD, 24),
        GENERAL("General", "General player statistics", Material.BOOK, 25);

        private final String title;
        private final String description;
        private final Material icon;
        private final int slot;

        Category(String title, String description, Material icon, int slot) {
            this.title = title;
            this.description = description;
            this.icon = icon;
            this.slot = slot;
        }
    }

    private record Stat(Material icon, String label, String value) {
    }

    public static void open(Player viewer, Player target) {
        openMain(viewer, target);
    }

    // ------------------------------------------------------------------
    // Menus
    // ------------------------------------------------------------------

    private static void openMain(Player viewer, Player target) {

        StatisticsHolder holder = new StatisticsHolder();
        Inventory inventory = Bukkit.createInventory(
                holder,
                MAIN_SIZE,
                Messages.statisticsTitle(viewer.equals(target), target.getName())
        );
        holder.setInventory(inventory);

        inventory.setItem(SUMMARY_SLOT, createSummaryItem(target));

        for (Category category : Category.values()) {
            inventory.setItem(category.slot, createCategoryItem(category));
            holder.onClick(category.slot, player -> openCategory(player, target, category));
        }

        inventory.setItem(MAIN_CLOSE_SLOT, createItem(
                Material.BARRIER,
                Messages.CLOSE_BUTTON,
                List.of(Messages.CLOSE_BUTTON_HINT)
        ));
        holder.onClick(MAIN_CLOSE_SLOT, Player::closeInventory);

        viewer.openInventory(inventory);
    }

    private static void openCategory(Player viewer, Player target, Category category) {

        StatisticsHolder holder = new StatisticsHolder();
        Inventory inventory = Bukkit.createInventory(
                holder,
                CATEGORY_SIZE,
                Messages.categoryTitle(category.title, viewer.equals(target), target.getName())
        );
        holder.setInventory(inventory);

        List<Stat> stats = getStatistics(category, target);
        int[] slots = STAT_SLOTS[Math.min(stats.size(), STAT_SLOTS.length - 1)];

        for (int i = 0; i < slots.length; i++) {
            Stat stat = stats.get(i);
            inventory.setItem(slots[i], createItem(
                    stat.icon(),
                    Messages.statName(stat.label()),
                    List.of(Messages.statValue(stat.value()))
            ));
        }

        inventory.setItem(CATEGORY_BACK_SLOT, createItem(
                Material.ARROW,
                Messages.BACK_BUTTON,
                List.of(Messages.BACK_BUTTON_HINT)
        ));
        holder.onClick(CATEGORY_BACK_SLOT, player -> openMain(player, target));

        viewer.openInventory(inventory);
    }

    // ------------------------------------------------------------------
    // Statistics (same values and formatting as the previous dialog menu)
    // ------------------------------------------------------------------

    private static List<Stat> getStatistics(Category category, Player target) {
        return switch (category) {
            case COMBAT -> List.of(
                    new Stat(Material.DIAMOND_SWORD, "Player Kills", formatNumber(target.getStatistic(Statistic.PLAYER_KILLS))),
                    new Stat(Material.ZOMBIE_HEAD, "Mob Kills", formatNumber(target.getStatistic(Statistic.MOB_KILLS))),
                    new Stat(Material.SKELETON_SKULL, "Deaths", formatNumber(target.getStatistic(Statistic.DEATHS)))
            );
            case MINING -> List.of(
                    new Stat(Material.IRON_PICKAXE, "Blocks Broken", formatNumber(StatisticsFormatter.getBlocksBroken(target))),
                    new Stat(Material.DIAMOND_ORE, "Ore Blocks Mined", formatNumber(StatisticsFormatter.getOreBlocksMined(target))),
                    new Stat(Material.ANVIL, "Tools Broken", formatNumber(StatisticsFormatter.getToolsBroken(target)))
            );
            case BUILDING -> List.of(
                    new Stat(Material.BRICKS, "Blocks Placed", formatNumber(StatisticsFormatter.getBlocksPlaced(target))),
                    new Stat(Material.CRAFTING_TABLE, "Items Crafted", formatNumber(StatisticsFormatter.getItemsCrafted(target)))
            );
            case EXPLORATION -> List.of(
                    new Stat(Material.LEATHER_BOOTS, "Distance Travelled", StatisticsFormatter.formatDistance(StatisticsFormatter.getTotalDistance(target))),
                    new Stat(Material.RABBIT_FOOT, "Jumps", formatNumber(target.getStatistic(Statistic.JUMP)))
            );
            case FARMING -> List.of(
                    new Stat(Material.WHEAT, "Crops Harvested", formatNumber(StatisticsFormatter.getCropsHarvested(target))),
                    new Stat(Material.EGG, "Animals Bred", formatNumber(target.getStatistic(Statistic.ANIMALS_BRED)))
            );
            case FISHING -> List.of(
                    new Stat(Material.COD, "Fish Caught", formatNumber(target.getStatistic(Statistic.FISH_CAUGHT)))
            );
            case GENERAL -> List.of(
                    new Stat(Material.CLOCK, "Playtime", StatisticsFormatter.formatPlaytime(target.getStatistic(Statistic.PLAY_ONE_MINUTE))),
                    new Stat(Material.RED_BED, "Times Slept", formatNumber(target.getStatistic(Statistic.SLEEP_IN_BED))),
                    new Stat(Material.RABBIT_FOOT, "Jumps", formatNumber(target.getStatistic(Statistic.JUMP)))
            );
        };
    }

    // ------------------------------------------------------------------
    // Items
    // ------------------------------------------------------------------

    private static ItemStack createCategoryItem(Category category) {
        return createItem(
                category.icon,
                Messages.categoryButton(category.title),
                List.of(
                        Messages.categoryButtonHint(category.description),
                        Component.empty(),
                        Messages.CLICK_TO_VIEW
                )
        );
    }

    private static ItemStack createSummaryItem(Player target) {

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);

        if (item.getItemMeta() instanceof SkullMeta meta) {
            meta.setOwningPlayer(target);
            item.setItemMeta(meta);
        }

        List<Component> lore = List.of(
                Messages.summaryLine("Username", Messages.summaryValue(target.getName())),
                Messages.summaryLine("Rank", getPrefixComponent(target)),
                Messages.summaryLine("Balance", Messages.summaryValue(getBalance(target))),
                Messages.summaryLine("Playtime", Messages.summaryValue(
                        StatisticsFormatter.formatPlaytime(target.getStatistic(Statistic.PLAY_ONE_MINUTE)))),
                Messages.summaryLine("First Join", Messages.summaryValue(
                        StatisticsFormatter.formatDate(target.getFirstPlayed())))
        );

        return applyDisplay(item, Messages.SUMMARY_NAME, lore);
    }

    private static ItemStack createItem(Material material, Component name, List<Component> lore) {
        return applyDisplay(new ItemStack(material), name, lore);
    }

    private static ItemStack applyDisplay(ItemStack item, Component name, List<Component> lore) {

        ItemMeta meta = item.getItemMeta();

        // Item text is italic by default; only turn it off where a component doesn't set it.
        meta.displayName(noItalic(name));

        List<Component> lines = new ArrayList<>();
        for (Component line : lore) {
            lines.add(noItalic(line));
        }
        meta.lore(lines);

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(meta);
        return item;
    }

    private static Component noItalic(Component component) {
        return component.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------

    private static String formatNumber(long number) {
        return StatisticsFormatter.formatNumber(number);
    }

    private static String getBalance(Player player) {
        try {
            var registration = Bukkit.getServicesManager().getRegistration(Economy.class);
            if (registration == null) return "Unknown";

            return "$" + StatisticsFormatter.formatNumber(
                    Math.round(registration.getProvider().getBalance((OfflinePlayer) player))
            );
        } catch (Exception ex) {
            return "Unknown";
        }
    }

    private static Component getPrefixComponent(Player player) {

        try {

            var registration = Bukkit.getServicesManager()
                    .getRegistration(LuckPerms.class);

            if (registration == null) {
                return Messages.UNKNOWN;
            }

            LuckPerms api = registration.getProvider();

            User user = api.getUserManager()
                    .getUser(player.getUniqueId());

            if (user == null) {
                return Messages.UNKNOWN;
            }

            CachedMetaData meta = user.getCachedData().getMetaData();

            String prefix = meta.getPrefix();

            if (prefix == null || prefix.isEmpty()) {
                return Messages.UNKNOWN;
            }

            return parsePrefix(prefix);

        } catch (Exception ex) {

            return Messages.UNKNOWN;
        }
    }

    private static Component parsePrefix(String prefix) {

        // MiniMessage prefixes, including gradients
        if (prefix.contains("<") && prefix.contains(">")) {

            try {

                return MiniMessage.miniMessage().deserialize(prefix);

            } catch (Exception ignored) {
                // Fall through to legacy parsing
            }
        }

        // LuckPerms stores prefixes as legacy "&" text (including &#RRGGBB hex); deserialise at the boundary.
        return LEGACY_PREFIX.deserialize(prefix);
    }
}
