package me.sbpro.cybermastery.gui;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import me.sbpro.cybermastery.CyberMastery;
import me.sbpro.cybermastery.Messages;
import me.sbpro.cybermastery.data.PlayerData;
import me.sbpro.cybermastery.data.TopMasteryEntry;
import me.sbpro.cybermastery.model.ActionMath;
import me.sbpro.cybermastery.model.Category;
import me.sbpro.cybermastery.model.Group;
import me.sbpro.cybermastery.model.LevelCurve;
import me.sbpro.cybermastery.model.MasteryType;
import me.sbpro.cybermastery.service.MasteryService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Builds every CyberMastery menu. All text comes from Messages. */
public final class MenuFactory {

    // /mastery (45 slots: info on top, categories in row 3, an empty row, then Top Mastery, Total Mastery and Claim All)
    public static final int MAIN_SIZE = 45;
    public static final int MAIN_INFO_SLOT = 4;
    public static final int MAIN_TOP_SLOT = 36;
    public static final int MAIN_TOTAL_SLOT = 40;
    public static final int MAIN_CLAIM_ALL_SLOT = 44;

    // /mastery top (same layout as /coinstop)
    public static final int TOP_SIZE = 54;
    public static final int TOP_ENTRIES = 45;
    public static final int TOP_SELF_SLOT = 49;

    // Category, group and type menus
    public static final int LIST_SIZE = 54;
    public static final int PAGE_SIZE = 45;
    public static final int PREVIOUS_SLOT = 45;
    public static final int BACK_SLOT = 48;
    public static final int INFO_SLOT = 49;
    public static final int NEXT_SLOT = 53;

    // Type menu bottom row
    public static final int TYPE_SUMMARY_SLOT = 49;
    public static final int TYPE_CLAIM_ALL_SLOT = 50;

    private MenuFactory() {
    }

    // =============================================================
    // /mastery
    // =============================================================

    public static Inventory createMainMenu(CyberMastery plugin, Player player) {
        MainMenuHolder holder = new MainMenuHolder();
        Inventory inventory = Bukkit.createInventory(holder, MAIN_SIZE, Messages.mainMenuTitle());
        holder.setInventory(inventory);

        fillMainMenu(plugin, player, inventory);

        return inventory;
    }

    public static void fillMainMenu(CyberMastery plugin, Player player, Inventory inventory) {
        PlayerData data = data(plugin, player);
        MasteryService service = plugin.getService();

        // Total Mastery: every type level added up, out of the most possible.
        Map<Category, Long> levels = new EnumMap<>(Category.class);
        Map<Category, Long> maxLevels = new EnumMap<>(Category.class);
        long total = 0;
        long totalMax = 0;

        for (Category category : Category.values()) {
            long sum = service.levelSum(data, category);
            long max = (long) plugin.getRegistry().types(category).size() * LevelCurve.MAX_LEVEL;
            levels.put(category, sum);
            maxLevels.put(category, max);
            total += sum;
            totalMax += max;
        }

        inventory.setItem(MAIN_TOTAL_SLOT, createItem(
                Material.BOOK,
                Messages.totalItemName(),
                Messages.totalItemLore(total, totalMax, levels, maxLevels)
        ));

        for (Category category : Category.values()) {
            List<MasteryType> types = plugin.getRegistry().types(category);

            inventory.setItem(category.menuSlot(), createItem(
                    category.icon(),
                    Messages.categoryItemName(category),
                    Messages.categoryItemLore(
                            category,
                            levels.get(category),
                            (long) types.size() * LevelCurve.MAX_LEVEL,
                            types.size(),
                            service.ready(data, types)
                    )
            ));
        }

        inventory.setItem(MAIN_INFO_SLOT, createItem(
                Material.KNOWLEDGE_BOOK,
                Messages.howMasteryWorksName(),
                Messages.howMasteryWorksLore()
        ));

        inventory.setItem(MAIN_TOP_SLOT, createItem(
                Material.PLAYER_HEAD,
                Messages.topButtonName(),
                Messages.topButtonLore()
        ));

        inventory.setItem(MAIN_CLAIM_ALL_SLOT, claimAllItem(service.readyAll(data), true));
    }

    // =============================================================
    // /mastery top
    // =============================================================

    public static Inventory createTopMasteryMenu(CyberMastery plugin, Player player) {
        TopMasteryMenuHolder holder = new TopMasteryMenuHolder();
        Inventory inventory = Bukkit.createInventory(holder, TOP_SIZE, Messages.topMasteryMenuTitle());
        holder.setInventory(inventory);

        // Make sure the viewer's own totals are current before ranking.
        plugin.getService().refreshTotals(data(plugin, player));

        List<TopMasteryEntry> topPlayers = plugin.getTotals().getTop(TOP_ENTRIES);

        for (int slot = 0; slot < topPlayers.size() && slot < TOP_ENTRIES; slot++) {
            TopMasteryEntry entry = topPlayers.get(slot);
            int position = slot + 1;

            inventory.setItem(
                    slot,
                    createPlayerLevelsHead(
                            entry.uuid(),
                            entry.playerName(),
                            entry.levels(),
                            position
                    )
            );
        }

        // Centre of the bottom row: the player's own levels and full leaderboard position.
        long playerLevels = plugin.getService().totalLevels(data(plugin, player));
        int playerPosition = plugin.getTotals().getPosition(player.getUniqueId());

        inventory.setItem(
                TOP_SELF_SLOT,
                createPlayerLevelsHead(
                        player.getUniqueId(),
                        player.getName(),
                        playerLevels,
                        playerPosition
                )
        );

        return inventory;
    }

    private static ItemStack createPlayerLevelsHead(
            UUID uuid,
            String playerName,
            long levels,
            int position
    ) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta baseMeta = head.getItemMeta();

        if (!(baseMeta instanceof SkullMeta meta)) {
            return head;
        }

        meta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));

        meta.displayName(noItalic(
                position > 0
                        ? Messages.topMasteryPlayerName(playerName, position)
                        : Messages.topMasteryPlayerNameUnranked(playerName)
        ));

        meta.lore(noItalic(Messages.topMasteryPlayerLore(levels)));

        head.setItemMeta(meta);

        // Hide the player-head profile tooltip entry while keeping the skin.
        head.setData(
                DataComponentTypes.TOOLTIP_DISPLAY,
                TooltipDisplay.tooltipDisplay()
                        .addHiddenComponents(DataComponentTypes.PROFILE)
                        .build()
        );

        return head;
    }

    // =============================================================
    // Category menu
    // =============================================================

    /** Number of entries in a category menu: groups for Blocks, types otherwise. */
    public static int categoryEntries(CyberMastery plugin, Category category) {
        return category == Category.BLOCKS
                ? plugin.getRegistry().groups().size()
                : plugin.getRegistry().types(category).size();
    }

    public static Inventory createCategoryMenu(CyberMastery plugin, Player player, Category category, int page) {
        int entries = categoryEntries(plugin, category);
        int pages = pages(entries);
        page = clampPage(page, pages);

        CategoryMenuHolder holder = new CategoryMenuHolder(category, page);
        Inventory inventory = Bukkit.createInventory(holder, LIST_SIZE, Messages.categoryMenuTitle(category, page, pages));
        holder.setInventory(inventory);

        PlayerData data = data(plugin, player);
        MasteryService service = plugin.getService();

        int start = page * PAGE_SIZE;
        int end = Math.min(entries, start + PAGE_SIZE);

        for (int index = start; index < end; index++) {
            int slot = index - start;

            if (category == Category.BLOCKS) {
                Group group = plugin.getRegistry().groups().get(index);

                inventory.setItem(slot, createItem(
                        group.icon(),
                        Messages.groupItemName(group),
                        Messages.groupItemLore(
                                group,
                                service.levelSum(data, group),
                                (long) group.types().size() * LevelCurve.MAX_LEVEL,
                                service.ready(data, group.types())
                        )
                ));
            } else {
                inventory.setItem(slot, typeItem(plugin, data, plugin.getRegistry().types(category).get(index)));
            }
        }

        addPageButtons(inventory, page, pages);

        inventory.setItem(BACK_SLOT, createItem(
                Material.ARROW,
                Messages.backName(),
                Messages.backLore(Messages.mainMenuName())
        ));

        inventory.setItem(INFO_SLOT, howCategoryWorksItem(plugin, category));

        return inventory;
    }

    // =============================================================
    // Group menu (Blocks)
    // =============================================================

    public static Inventory createGroupMenu(CyberMastery plugin, Player player, Group group, int page) {
        List<MasteryType> types = group.types();
        int pages = pages(types.size());
        page = clampPage(page, pages);

        GroupMenuHolder holder = new GroupMenuHolder(group, page);
        Inventory inventory = Bukkit.createInventory(holder, LIST_SIZE, Messages.groupMenuTitle(group, page, pages));
        holder.setInventory(inventory);

        PlayerData data = data(plugin, player);

        int start = page * PAGE_SIZE;
        int end = Math.min(types.size(), start + PAGE_SIZE);

        for (int index = start; index < end; index++) {
            inventory.setItem(index - start, typeItem(plugin, data, types.get(index)));
        }

        addPageButtons(inventory, page, pages);

        inventory.setItem(BACK_SLOT, createItem(
                Material.ARROW,
                Messages.backName(),
                Messages.backLore(Messages.categoryName(Category.BLOCKS))
        ));

        inventory.setItem(INFO_SLOT, howCategoryWorksItem(plugin, Category.BLOCKS));

        return inventory;
    }

    // =============================================================
    // Type menu (all 45 levels)
    // =============================================================

    public static Inventory createTypeMenu(CyberMastery plugin, Player player, MasteryType type, int backPage) {
        TypeMenuHolder holder = new TypeMenuHolder(type, backPage);
        Inventory inventory = Bukkit.createInventory(holder, LIST_SIZE, Messages.typeMenuTitle(type));
        holder.setInventory(inventory);

        fillTypeMenu(plugin, player, inventory, type);

        return inventory;
    }

    /** Also used to refresh the menu straight after a claim. */
    public static void fillTypeMenu(CyberMastery plugin, Player player, Inventory inventory, MasteryType type) {
        PlayerData data = data(plugin, player);
        MasteryService service = plugin.getService();
        LevelCurve curve = plugin.getRegistry().curve(type.category());

        long units = service.units(data, type);
        int level = curve.levelForUnits(units);
        long done = ActionMath.actionsDone(units, type.points());

        for (int n = 1; n <= LevelCurve.MAX_LEVEL; n++) {
            // Total actions needed to reach this level, e.g. "Mine Stone: 260/420".
            long need = ActionMath.actionsFor(curve, type.points(), n);
            Component reward = plugin.getRewardHook().describe(type, n);

            ItemStack item;

            if (n <= level && !data.isClaimed(type.id(), n)) {
                item = createItem(Material.CHEST_MINECART, Messages.levelItemName(n),
                        Messages.levelReadyLore(type, n, need, need, reward), true);
            } else if (n <= level) {
                item = createItem(Material.MINECART, Messages.levelItemName(n),
                        Messages.levelClaimedLore(type, n, need, need, reward));
            } else {
                item = createItem(Material.BARRIER, Messages.levelItemName(n),
                        Messages.levelLockedLore(type, n, done, need, reward));
            }

            item.setAmount(n);
            inventory.setItem(n - 1, item);
        }

        String backTo = type.group() != null
                ? type.group().name()
                : Messages.categoryName(type.category());

        inventory.setItem(BACK_SLOT, createItem(
                Material.ARROW,
                Messages.backName(),
                Messages.backLore(backTo)
        ));

        inventory.setItem(TYPE_SUMMARY_SLOT, createItem(
                type.icon(),
                Messages.typeItemName(type),
                Messages.typeSummaryLore(
                        type,
                        level,
                        ActionMath.current(curve, type.points(), units),
                        done,
                        service.placementPercent(type)
                )
        ));

        inventory.setItem(TYPE_CLAIM_ALL_SLOT, claimAllItem(service.ready(data, type), false));
    }

    // =============================================================
    // Shared items
    // =============================================================

    private static ItemStack typeItem(CyberMastery plugin, PlayerData data, MasteryType type) {
        MasteryService service = plugin.getService();
        LevelCurve curve = plugin.getRegistry().curve(type.category());
        long units = service.units(data, type);

        int level = curve.levelForUnits(units);

        // Maxed types glow.
        return createItem(
                type.icon(),
                Messages.typeItemName(type),
                Messages.typeItemLore(
                        type,
                        level,
                        ActionMath.current(curve, type.points(), units),
                        service.placementPercent(type),
                        service.ready(data, type)
                ),
                level >= LevelCurve.MAX_LEVEL
        );
    }

    private static ItemStack howCategoryWorksItem(CyberMastery plugin, Category category) {
        return createItem(
                Material.BOOK,
                Messages.howCategoryWorksName(category),
                Messages.howCategoryWorksLore(category, plugin.getSettings())
        );
    }

    private static ItemStack claimAllItem(int ready, boolean everything) {
        return createItem(
                ready > 0 ? Material.CHEST_MINECART : Material.MINECART,
                Messages.claimAllName(),
                Messages.claimAllLore(ready, everything),
                ready > 0
        );
    }

    private static void addPageButtons(Inventory inventory, int page, int pages) {
        if (page > 0) {
            inventory.setItem(PREVIOUS_SLOT, createItem(
                    Material.ARROW,
                    Messages.previousPageName(),
                    Messages.previousPageLore(page)
            ));
        }

        if (page < pages - 1) {
            inventory.setItem(NEXT_SLOT, createItem(
                    Material.ARROW,
                    Messages.nextPageName(),
                    Messages.nextPageLore(page + 2)
            ));
        }
    }

    public static int pages(int entries) {
        return Math.max(1, (entries + PAGE_SIZE - 1) / PAGE_SIZE);
    }

    private static int clampPage(int page, int pages) {
        return Math.max(0, Math.min(page, pages - 1));
    }

    private static PlayerData data(CyberMastery plugin, Player player) {
        return plugin.getDataManager().loadNow(player.getUniqueId(), player.getName());
    }

    private static ItemStack createItem(Material material, Component name, List<Component> lore) {
        return createItem(material, name, lore, false);
    }

    private static ItemStack createItem(Material material, Component name, List<Component> lore, boolean glint) {
        ItemStack stack = new ItemStack(material);

        ItemMeta meta = stack.getItemMeta();

        if (meta == null) {
            return stack;
        }

        meta.displayName(noItalic(name));
        meta.lore(noItalic(lore));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        if (glint) {
            meta.setEnchantmentGlintOverride(true);
        }

        stack.setItemMeta(meta);

        // Hide tool damage / attack speed lines on the sword, hoe and axe icons.
        stack.setData(
                DataComponentTypes.TOOLTIP_DISPLAY,
                TooltipDisplay.tooltipDisplay()
                        .addHiddenComponents(DataComponentTypes.ATTRIBUTE_MODIFIERS)
                        .build()
        );

        return stack;
    }

    /** Item names and lore are italic by default; Messages text should render as written. */
    private static Component noItalic(Component component) {
        return component.decoration(TextDecoration.ITALIC, false);
    }

    private static List<Component> noItalic(List<Component> components) {
        return components.stream().map(MenuFactory::noItalic).toList();
    }
}
