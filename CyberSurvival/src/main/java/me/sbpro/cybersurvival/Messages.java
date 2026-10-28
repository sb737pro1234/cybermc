package me.sbpro.cybersurvival;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.List;

public final class Messages {

    public static final TextColor CUSTOM = TextColor.color(0x00AAFF);

    public static final Component PREFIX = Component.text()
            .append(Component.text("Cyber", CUSTOM, TextDecoration.BOLD))
            .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
            .build();

    // Standard errors (unprefixed)
    public static final Component PLAYER_ONLY = Component.text(
            "You must be a player to use this command.",
            NamedTextColor.RED
    );

    public static final Component NO_PERMISSION = Component.text(
            "You do not have permission to use this command.",
            NamedTextColor.RED
    );

    // Commands
    public static final Component ECONOMY_UNAVAILABLE = error("The economy system is currently unavailable.");
    public static final Component BALTOP_RELOADED = info("Balance Top messages have been reloaded.");
    public static final Component PLAYTOP_RELOADED = info("Playtime Top messages have been reloaded.");
    public static final Component USAGE_CUSTOM_ENCHANT = error("Usage: /customenchant give <player> <enchant>");
    public static final Component PLAYER_NOT_FOUND = error("Player not found.");
    public static final Component NOT_HOLDING_ITEM = error("That player is not holding an item.");
    public static final Component AUTOSMELT_PICKAXES_ONLY = error("AutoSmelt can only be applied to pickaxes.");
    public static final Component ALREADY_HAS_AUTOSMELT = error("That item already has AutoSmelt.");
    public static final Component UNKNOWN_ENCHANT = error("Unknown enchant.");
    public static final Component PICKAXE_ENCHANTED = Component.text("Your pickaxe has been enchanted with ", NamedTextColor.WHITE)
            .append(Component.text("AutoSmelt", NamedTextColor.GOLD))
            .append(Component.text(".", NamedTextColor.WHITE));

    public static final Component PLAYERS_SHOWN = PREFIX
            .append(Component.text("✔ Players are now ", NamedTextColor.WHITE))
            .append(Component.text("shown", NamedTextColor.GREEN, TextDecoration.BOLD))
            .append(Component.text(".", NamedTextColor.WHITE));
    public static final Component PLAYERS_HIDDEN = PREFIX
            .append(Component.text("✔ Players are now ", NamedTextColor.WHITE))
            .append(Component.text("hidden", NamedTextColor.RED, TextDecoration.BOLD))
            .append(Component.text(".", NamedTextColor.WHITE));

    // Menu titles
    public static final Component BALTOP_TITLE = Component.text("Balance Top", CUSTOM);
    public static final Component PLAYTOP_TITLE = Component.text("Playtime Top", CUSTOM);
    public static final Component RANKS_TITLE = Component.text("Ranks", CUSTOM, TextDecoration.BOLD);

    // Menu items
    public static final Component YOUR_BALANCE_NAME = item("Your Balance", CUSTOM);
    public static final Component YOUR_PLAYTIME_NAME = item("Your Playtime", CUSTOM);
    public static final Component UNRANKED_LINE = loreValue("Position", Component.text("Unranked", CUSTOM));

    // Item names
    public static final Component AUTOSMELT_LORE = Component.text("AutoSmelt", NamedTextColor.GOLD)
            .decoration(TextDecoration.ITALIC, false);
    public static final Component GRASSY_HELMET = item("Grassy Helmet", NamedTextColor.DARK_GREEN);
    public static final Component GRASSY_CHESTPLATE = item("Grassy Chestplate", NamedTextColor.DARK_GREEN);
    public static final Component GRASSY_LEGGINGS = item("Grassy Leggings", NamedTextColor.DARK_GREEN);
    public static final Component GRASSY_BOOTS = item("Grassy Boots", NamedTextColor.DARK_GREEN);

    private Messages() {
    }

    private static Component info(String text) {
        return PREFIX.append(Component.text(text, NamedTextColor.WHITE));
    }

    private static Component error(String text) {
        return Component.text(text, NamedTextColor.RED);
    }

    private static Component item(String text, TextColor color) {
        return Component.text(text, color).decoration(TextDecoration.ITALIC, false);
    }

    private static Component loreLine(String text) {
        return Component.text(text, NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false);
    }

    private static Component loreValue(String label, Component value) {
        return Component.text(label + ": ", NamedTextColor.WHITE)
                .append(value)
                .decoration(TextDecoration.ITALIC, false);
    }

    /** Blank lore line. */
    public static Component blankLore() {
        return Component.empty();
    }

    // Toggle commands

    public static Component toggled(String subject, boolean enabled) {
        return PREFIX
                .append(Component.text(subject + " has been ", NamedTextColor.WHITE))
                .append(Component.text(enabled ? "enabled" : "disabled", CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component glowToggled(boolean enabled) {
        return toggled("Your glow", enabled);
    }

    public static Component bossBarToggled(boolean enabled) {
        return toggled("Your bossbar", enabled);
    }

    public static Component chatVisibilityToggled(boolean enabled) {
        return toggled("Chat visibility", enabled);
    }

    public static Component playtime(String playtime) {
        return PREFIX
                .append(Component.text("Your playtime is ", NamedTextColor.WHITE))
                .append(Component.text(playtime, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component enchantApplied(String playerName) {
        return Component.text("Applied ", NamedTextColor.GREEN)
                .append(Component.text("AutoSmelt", NamedTextColor.GOLD))
                .append(Component.text(" to " + playerName + "'s pickaxe.", NamedTextColor.GREEN));
    }

    // Leaderboard menus

    public static Component topPlayerName(int position, String playerName) {
        return Component.text(playerName + " (", NamedTextColor.WHITE)
                .append(Component.text("#" + position, CUSTOM))
                .append(Component.text(")", NamedTextColor.WHITE))
                .decoration(TextDecoration.ITALIC, false);
    }

    public static Component balanceLine(String balance) {
        return loreValue("Balance", Component.text("$" + balance, CUSTOM));
    }

    public static Component playtimeLine(String playtime) {
        return loreValue("Playtime", Component.text(playtime, CUSTOM));
    }

    public static Component positionLine(int position) {
        return position > 0
                ? loreValue("Position", Component.text("#" + position, CUSTOM))
                : UNRANKED_LINE;
    }

    // Ranks menu

    public static Component rankName(TextColor color, String name) {
        return item(name, color);
    }

    public static Component rankPerk(TextColor color, String perk) {
        return Component.text("◆ ", color)
                .append(Component.text(perk, NamedTextColor.WHITE))
                .decoration(TextDecoration.ITALIC, false);
    }

    public static List<Component> rankPerks(TextColor color, String... perks) {
        return java.util.Arrays.stream(perks).map(perk -> rankPerk(color, perk)).toList();
    }
}
