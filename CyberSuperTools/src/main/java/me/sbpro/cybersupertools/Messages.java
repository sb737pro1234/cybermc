package me.sbpro.cybersupertools;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.List;

public final class Messages {

    public static final TextColor CUSTOM = TextColor.color(0x56E0FD);

    /** Colour of the super tool items themselves (name and description). */
    public static final TextColor TOOL_COLOR = TextColor.color(0xA020F0);

    public static final Component PREFIX = Component.text()
            .append(Component.text("SUPERTOOLS", CUSTOM, TextDecoration.BOLD))
            .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
            .build();

    public static final Component NO_PERMISSION = Component.text(
            "You do not have permission to use this command.",
            NamedTextColor.RED
    );

    public static final Component USAGE = PREFIX
            .append(Component.text("Usage: ", NamedTextColor.WHITE))
            .append(Component.text("/supertools give [player] [tool]", CUSTOM));

    public static final Component UNKNOWN_TOOL = PREFIX
            .append(Component.text("Unknown tool.", NamedTextColor.RED));

    public static final Component PLAYER_NOT_FOUND = PREFIX
            .append(Component.text("That player could not be found.", NamedTextColor.RED));

    private Messages() {
    }

    public static Component playerGiven(String playerName, String toolName) {
        return PREFIX
                .append(Component.text("You gave ", NamedTextColor.WHITE))
                .append(Component.text(playerName, CUSTOM))
                .append(Component.text(" ", NamedTextColor.WHITE))
                .append(Component.text(toolName, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    /** Item name of a super tool. */
    public static Component toolName(String shortName) {
        return Component.text("Super " + shortName, TOOL_COLOR)
                .decoration(TextDecoration.ITALIC, false);
    }

    /** Item lore of a super tool. */
    public static List<Component> toolLore(String information) {
        return List.of(
                Component.text("", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
                Component.text(information, TOOL_COLOR).decoration(TextDecoration.ITALIC, false)
        );
    }
}
