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

    public static final Component BACK_BUTTON = Component.text("Back", NamedTextColor.WHITE);
    public static final Component BACK_BUTTON_HINT = Component.text("Return to Statistics", NamedTextColor.WHITE);
    public static final Component CLOSE_BUTTON = Component.text("Close", NamedTextColor.WHITE);
    public static final Component CLOSE_BUTTON_HINT = Component.text("Close statistics", NamedTextColor.WHITE);

    public static final Component UNKNOWN = Component.text("Unknown", NamedTextColor.WHITE);

    private Messages() {
    }

    /** Title of the main statistics dialog. */
    public static Component statisticsTitle(boolean ownStatistics, String targetName) {
        if (ownStatistics) {
            return Component.text("Statistics", CUSTOM);
        }
        return Component.text("Statistics for ", NamedTextColor.WHITE)
                .append(Component.text(targetName, CUSTOM));
    }

    /** Title of a category dialog. */
    public static Component categoryTitle(String category) {
        return Component.text(category, CUSTOM);
    }

    /** Short description shown at the top of a category dialog. */
    public static Component categoryDescription(String category, String targetName) {
        return Component.text(category + " statistics for ", NamedTextColor.WHITE)
                .append(Component.text(targetName, CUSTOM));
    }

    public static Component categoryButton(String title) {
        return Component.text(title, NamedTextColor.WHITE);
    }

    public static Component categoryButtonHint(String description) {
        return Component.text(description, NamedTextColor.WHITE);
    }

    /** Bold and underlined heading of the statistics body. */
    public static Component categoryHeading(String category) {
        return Component.text(category, CUSTOM, TextDecoration.BOLD, TextDecoration.UNDERLINED);
    }

    /** A single "Label » value" statistic line. */
    public static Component statLine(String label, String value) {
        return Component.text(label, NamedTextColor.WHITE)
                .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
                .append(Component.text(value, CUSTOM));
    }

    /** A single "Label: value" line in the player summary. */
    public static Component summaryLine(String label, Component value) {
        return Component.text(label + ": ", NamedTextColor.WHITE).append(value);
    }

    public static Component summaryValue(String value) {
        return Component.text(value, CUSTOM);
    }
}
