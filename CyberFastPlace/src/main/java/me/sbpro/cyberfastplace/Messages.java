package me.sbpro.cyberfastplace;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public final class Messages {

    public static final TextColor CUSTOM = TextColor.color(0xA7F3D0);

    public static final Component PREFIX = Component.text()
            .append(Component.text("FASTPLACE", CUSTOM, TextDecoration.BOLD))
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

    public static final Component FASTPLACE_DISABLED = PREFIX
            .append(Component.text("Fastplace disabled.", NamedTextColor.RED));

    public static final Component USAGE = PREFIX
            .append(Component.text("Usage: /fp [1-16|1-32|1-64|1-128]", NamedTextColor.RED));

    public static Component usageMax(int maxAllowed) {
        return PREFIX
            .append(Component.text("Usage: /fp [1-", NamedTextColor.RED))
            .append(Component.text(maxAllowed, NamedTextColor.RED))
            .append(Component.text("]", NamedTextColor.RED));
    }

    public static Component fastplaceEnabled(int amount) {
        return PREFIX
            .append(Component.text("Fastplace enabled with block range ", NamedTextColor.WHITE))
            .append(Component.text(amount, CUSTOM))
            .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static final Component CANNOT_PLACE_SPAWNERS = PREFIX
            .append(Component.text("You cannot place spawners with fastplace!", NamedTextColor.RED));

    public static final Component STOPPED_AT_BORDER = PREFIX
            .append(Component.text("(!) ", NamedTextColor.RED, TextDecoration.BOLD))
            .append(Component.text("Fastplace stopped at your island border.", NamedTextColor.RED));

    private Messages() {
    }
}
