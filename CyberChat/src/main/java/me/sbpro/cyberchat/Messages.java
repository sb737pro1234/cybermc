package me.sbpro.cyberchat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public final class Messages {

    public static final TextColor CUSTOM = TextColor.color(0xE056FD);

    /** Colour of the tick shown next to the selected option. */
    private static final TextColor SELECTED_TICK = TextColor.color(0x55FF55);
    private static final TextColor LOCKED = TextColor.color(0x777777);
    private static final TextColor BOLD_OPTION = TextColor.color(0xFFAA00);

    public static final Component PREFIX = Component.text()
            .append(Component.text("CHATCOLOR", CUSTOM, TextDecoration.BOLD))
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

    public static final Component NO_PERMISSION_COLOR = PREFIX
            .append(Component.text("You don't have permission to use this chat color.", NamedTextColor.RED));

    public static final Component NO_PERMISSION_BOLD = PREFIX
            .append(Component.text("You don't have permission to use bold.", NamedTextColor.RED));

    public static final Component DIALOG_TITLE = Component.text("Chat Color", NamedTextColor.WHITE);

    public static final Component NO_PERMISSION_HINT = Component.text("You don't have permission.", NamedTextColor.WHITE);

    public static final Component TOGGLE_BOLD_HINT = Component.text("Toggle bold chat", NamedTextColor.WHITE);

    private Messages() {
    }

    public static Component chatColorUpdated(String colorName, TextColor color) {
        return PREFIX
                .append(Component.text("Your chat color has been updated to ", NamedTextColor.WHITE))
                .append(Component.text(colorName, color))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component boldToggled(boolean enabled) {
        return PREFIX
                .append(Component.text("Bold has been ", NamedTextColor.WHITE))
                .append(Component.text(enabled ? "enabled." : "disabled.", NamedTextColor.WHITE));
    }

    public static Component selectColorHint(String colorName) {
        return Component.text("Select " + colorName, NamedTextColor.WHITE);
    }

    /** Dialog button label of a chat colour (greyed out when locked, ticked when selected). */
    public static Component colorButton(String colorName, TextColor color, boolean unlocked, boolean selected) {
        if (!unlocked) {
            return Component.text(colorName, LOCKED);
        }
        Component name = Component.text(colorName, color);
        return selected ? name.append(Component.text(" ✓", SELECTED_TICK)) : name;
    }

    /** Dialog button label of the bold toggle. */
    public static Component boldButton(boolean unlocked, boolean enabled) {
        if (!unlocked) {
            return Component.text("Bold", LOCKED);
        }
        Component name = Component.text("Bold", BOLD_OPTION);
        return enabled ? name.append(Component.text(" ✓", SELECTED_TICK)) : name;
    }
}
