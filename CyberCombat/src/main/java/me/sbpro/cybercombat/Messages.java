package me.sbpro.cybercombat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public final class Messages {

    public static final TextColor CUSTOM = TextColor.color(0xE91E63);

    public static final Component PREFIX = Component.text()
            .append(Component.text("COMBAT", CUSTOM, TextDecoration.BOLD))
            .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
            .build();

    public static final Component COMMAND_BLOCKED = PREFIX
            .append(Component.text("You cannot use commands while in combat!", NamedTextColor.RED));

    public static final Component LEFT_COMBAT = PREFIX
            .append(Component.text("You are no longer in combat.", NamedTextColor.WHITE));

    public static final Component YOU_DIED_TITLE =
            Component.text("☠ YOU DIED ☠", NamedTextColor.WHITE);

    public static final Component LOGGED_OUT_SUBTITLE =
            Component.text("You logged out during combat.", NamedTextColor.WHITE);

    private Messages() {
    }

    public static Component enteredCombat(String opponentName, int seconds) {
        return PREFIX
                .append(Component.text("You are now in combat with ", NamedTextColor.WHITE))
                .append(Component.text(opponentName, CUSTOM))
                .append(Component.text("! You cannot log out or use commands for ", NamedTextColor.WHITE))
                .append(Component.text(seconds, CUSTOM))
                .append(Component.text("s.", NamedTextColor.WHITE));
    }

    public static Component combatLogout(String playerName) {
        return Component.text("[", NamedTextColor.DARK_GRAY)
                .append(Component.text("☠", NamedTextColor.DARK_RED))
                .append(Component.text("] ", NamedTextColor.DARK_GRAY))
                .append(Component.text(playerName, NamedTextColor.DARK_RED))
                .append(Component.text(" logged out during combat.", NamedTextColor.WHITE));
    }

    public static Component combatActionBar(int seconds) {
        return Component.text("Combat: ", NamedTextColor.WHITE)
                .append(Component.text(seconds, CUSTOM))
                .append(Component.text("s", NamedTextColor.WHITE));
    }
}
