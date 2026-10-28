package me.sbpro.cyberenchants;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.List;

public final class Messages {

    // Cyber Enchants colour: Cyan (#00D9FF)
    public static final TextColor CUSTOM = TextColor.color(0x00D9FF);

    public static final Component PREFIX = Component.text()
            .append(Component.text("ENCHANTS", CUSTOM, TextDecoration.BOLD))
            .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
            .build();

    // Standard errors (unprefixed)
    public static final Component NO_PERMISSION = Component.text(
            "You do not have permission to use this command.",
            NamedTextColor.RED
    );

    // Custom item
    public static final Component ENCHANT_UPGRADE_NAME = name("Enchant Upgrade", CUSTOM);
    public static final List<Component> ENCHANT_UPGRADE_LORE = List.of(
            lore("Right-click this to open the Enchant Upgrade menu."),
            lore("Select an item with a max-level enchantment"),
            lore("and select which enchant to upgrade.")
    );

    // GUI titles
    public static final Component UPGRADE_MENU_TITLE = Component.text("Enchant Upgrade", CUSTOM);
    public static final Component ENCHANT_SELECT_TITLE = Component.text("Select Enchant", CUSTOM);
    public static final Component CONFIRM_TITLE = Component.text("Confirm Upgrade", CUSTOM);

    // GUI items
    public static final Component CONFIRM_NAME = name("Confirm", NamedTextColor.GREEN);
    public static final Component CANCEL_NAME = name("Cancel", NamedTextColor.RED);
    public static final Component CONFIRM_LORE = lore("Upgrade this enchant.");
    public static final Component CANCEL_LORE = lore("Cancel the upgrade.");

    public static final Component MENU_INFO_NAME = name("Enchant Upgrade", CUSTOM);
    public static final Component MENU_INFO_LORE = lore("Place your item below.");
    public static final Component PLACE_ITEM_NAME = name("Place Item", CUSTOM);
    public static final Component PLACE_ITEM_LORE = lore("Click here while holding the item");
    public static final Component PLACE_ITEM_LORE_EMPTY = lore("Place the item you want to upgrade.");
    public static final Component PLACE_UPGRADE_NAME = name("Place Upgrade", CUSTOM);
    public static final Component PLACE_UPGRADE_LORE = lore("Click here while holding an Enchant Upgrade");
    public static final Component PLACE_UPGRADE_LORE_EMPTY = lore("Place an Enchant Upgrade here.");
    public static final Component SELECT_LORE = lore("Click to select.");

    // Messages
    public static final Component USAGE = PREFIX
            .append(Component.text("Usage: ", NamedTextColor.WHITE))
            .append(Component.text("/enchantitem give <player> <item>", CUSTOM));
    public static final Component PLAYER_NOT_FOUND = PREFIX
            .append(Component.text("That player could not be found.", NamedTextColor.RED));
    public static final Component INVALID_ITEM = PREFIX
            .append(Component.text("Unknown enchant item. Available: ", NamedTextColor.RED))
            .append(Component.text("enchant_upgrade", NamedTextColor.WHITE));
    public static final Component NO_ENCHANTMENTS = PREFIX
            .append(Component.text("This item has no enchantments that can be upgraded.", NamedTextColor.RED));
    public static final Component NOT_ENCHANTED_ITEM = PREFIX
            .append(Component.text("You must right-click an item that has an eligible enchantment.", NamedTextColor.RED));

    private Messages() {
    }

    public static Component given(String itemName, String playerName) {
        return PREFIX
                .append(Component.text("Given ", NamedTextColor.WHITE))
                .append(Component.text(itemName, CUSTOM))
                .append(Component.text(" to ", NamedTextColor.WHITE))
                .append(Component.text(playerName, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component upgraded(String enchantName, int current, int next) {
        return PREFIX
                .append(Component.text("Upgraded ", NamedTextColor.WHITE))
                .append(Component.text(enchantName + " " + current, CUSTOM))
                .append(Component.text(" to ", NamedTextColor.WHITE))
                .append(Component.text(enchantName + " " + next, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component enchantOption(String enchantName, int current) {
        return name(enchantName + " " + current, CUSTOM);
    }

    public static List<Component> enchantOptionLore(String enchantName, int next) {
        return List.of(
                lore("Upgrade to ")
                        .append(Component.text(enchantName + " " + next, NamedTextColor.WHITE)),
                SELECT_LORE
        );
    }

    public static Component upgradeDisplayName(String enchantName, int current, int next) {
        return name(enchantName + " " + current + " → " + next, CUSTOM);
    }

    public static Component upgradeDisplayLore(String enchantName, int current, int next) {
        return lore("Upgrade: ")
                .append(Component.text(enchantName + " " + current + " ", NamedTextColor.WHITE))
                .append(Component.text("→ ", NamedTextColor.DARK_GRAY))
                .append(Component.text(enchantName + " " + next, NamedTextColor.WHITE));
    }

    /** Item name without the default italics. */
    private static Component name(String text, TextColor color) {
        return Component.text(text, color).decoration(TextDecoration.ITALIC, false);
    }

    /** Item lore line (gray, no default italics). */
    private static Component lore(String text) {
        return Component.text(text, NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false);
    }
}
