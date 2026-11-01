package me.sbpro.cyberstatistics;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public final class Messages {

    public static final TextColor CUSTOM = TextColor.color(0x00AAFF);

    public static final Component PREFIX = Component.text()
            .append(Component.text("Cyber", CUSTOM, TextDecoration.BOLD))
            .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
            .build();

    public static final Component PLAYER_ONLY = Component.text(
            "You must be a player to use this command.",
            NamedTextColor.RED
    );

    public static final Component NO_PERMISSION = Component.text(
            "You do not have permission to use this command.",
            NamedTextColor.RED
    );

    public static final Component PLAYER_NOT_ONLINE = PREFIX
            .append(Component.text("That player is not online.", NamedTextColor.RED));

    public static final Component BACK_BUTTON = Component.text("Back", CUSTOM);
    public static final Component BACK_BUTTON_HINT = Component.text("Return to Statistics", NamedTextColor.WHITE);
    public static final Component CLOSE_BUTTON = Component.text("Close", CUSTOM);
    public static final Component CLOSE_BUTTON_HINT = Component.text("Close statistics", NamedTextColor.WHITE);

    public static final Component SUMMARY_NAME = Component.text("Player Summary", CUSTOM);
    public static final Component CLICK_TO_VIEW = Component.text("Click to view", NamedTextColor.DARK_GRAY);

    public static final Component UNKNOWN = Component.text("Unknown", NamedTextColor.WHITE);

    private Messages() {
    }

    /** Title of the main statistics inventory. */
    public static Component statisticsTitle(boolean ownStatistics, String targetName) {
        if (ownStatistics) {
            return Component.text("Statistics", CUSTOM);
        }
        return Component.text("Statistics for ", NamedTextColor.WHITE)
                .append(Component.text(targetName, CUSTOM));
    }

    /** Title of a category inventory. */
    public static Component categoryTitle(String category, boolean ownStatistics, String targetName) {
        Component title = Component.text(category, CUSTOM);
        if (ownStatistics) {
            return title;
        }
        return title
                .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
                .append(Component.text(targetName, NamedTextColor.WHITE));
    }

    /** Display name of a category button. */
    public static Component categoryButton(String title) {
        return Component.text(title, CUSTOM);
    }

    /** Description line on a category button. */
    public static Component categoryButtonHint(String description) {
        return Component.text(description, NamedTextColor.WHITE);
    }

    /** Display name of a single statistic item. */
    public static Component statName(String label) {
        return Component.text(label, CUSTOM);
    }

    /** The value line of a single statistic item. */
    public static Component statValue(String value) {
        return Component.text(value, NamedTextColor.WHITE);
    }

    /** A single "Label: value" line in the player summary. */
    public static Component summaryLine(String label, Component value) {
        return Component.text(label + ": ", NamedTextColor.WHITE).append(value);
    }

    public static Component summaryValue(String value) {
        return Component.text(value, CUSTOM);
    }
}
