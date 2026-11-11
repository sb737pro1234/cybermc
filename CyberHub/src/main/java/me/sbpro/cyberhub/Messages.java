package me.sbpro.cyberhub;

import java.util.List;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public final class Messages {

    private Messages() {}

    public static final TextColor CYBER_BLUE = TextColor.color(0x00AAFF);
    public static final TextColor ENDER_PURPLE = TextColor.color(0xB84DFF);

    public static final Component PREFIX = Component.text()
            .append(Component.text("HUB", CYBER_BLUE).decorate(TextDecoration.BOLD))
            .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
            .build();

    // Bossbar
    public static final Component BOSSBAR = Component.text("Use the compass to switch servers!", NamedTextColor.WHITE);

    // Player visibility toggle
    // Custom blue + bold label, status not bold
    public static final Component VISIBILITY_ON_NAME = Component.text("Player Visibility: ", CYBER_BLUE, TextDecoration.BOLD)
            .append(Component.text("Enabled", NamedTextColor.GREEN).decoration(TextDecoration.BOLD, false))
            .decoration(TextDecoration.ITALIC, false);

    public static final Component VISIBILITY_OFF_NAME = Component.text("Player Visibility: ", CYBER_BLUE, TextDecoration.BOLD)
            .append(Component.text("Disabled", NamedTextColor.RED).decoration(TextDecoration.BOLD, false))
            .decoration(TextDecoration.ITALIC, false);

    public static final List<Component> VISIBILITY_LORE = List.of(
            Component.text("Right-click to toggle seeing other players.", NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, false)
    );

    public static final Component VISIBILITY_ENABLED = PREFIX
            .append(Component.text("Player visibility ", NamedTextColor.WHITE))
            .append(Component.text("enabled", NamedTextColor.GREEN))
            .append(Component.text(".", NamedTextColor.WHITE));

    public static final Component VISIBILITY_DISABLED = PREFIX
            .append(Component.text("Player visibility ", NamedTextColor.WHITE))
            .append(Component.text("disabled", NamedTextColor.RED))
            .append(Component.text(".", NamedTextColor.WHITE));

    public static Component visibilityCooldown(int ticksLeft) {
        String seconds = String.format("%.1f", ticksLeft / 20.0);
        return PREFIX
                .append(Component.text("Please wait ", NamedTextColor.RED))
                .append(Component.text(seconds + "s", CYBER_BLUE))
                .append(Component.text(" before toggling again.", NamedTextColor.RED));
    }

    // Server selector
    private static Component plain(String text, TextColor color) {
        return Component.text(text, color).decoration(TextDecoration.ITALIC, false);
    }

    private static Component bold(String text, TextColor color) {
        return plain(text, color).decorate(TextDecoration.BOLD);
    }

    public static final Component SELECTOR_TITLE = bold("Server Selector", CYBER_BLUE);

    public static final Component SELECTOR_ITEM_NAME = bold("Server Selector", CYBER_BLUE);
    public static final List<Component> SELECTOR_ITEM_LORE = List.of(
            plain("Right-click to open the server menu!", NamedTextColor.GRAY)
    );

    public static final Component SELECTOR_SURVIVAL_NAME = bold("Survival", CYBER_BLUE);
    public static final List<Component> SELECTOR_SURVIVAL_LORE = List.of(
            plain("Click here to join Cyber Survival.", NamedTextColor.WHITE)
    );

    public static final Component SELECTOR_HUB_NAME = bold("Hub", NamedTextColor.WHITE);
    public static final List<Component> SELECTOR_HUB_LORE = List.of(
            plain("You are already here!", NamedTextColor.RED)
    );

    public static final Component SELECTOR_UNAVAILABLE_NAME = bold("UNAVAILABLE", NamedTextColor.RED);

    public static final Component SELECTOR_ALREADY_HERE = PREFIX
            .append(Component.text("You are already on the ", NamedTextColor.RED))
            .append(Component.text("Hub", CYBER_BLUE))
            .append(Component.text("!", NamedTextColor.RED));

    public static Component selectorConnecting(String server) {
        return PREFIX
                .append(Component.text("Sending you to ", NamedTextColor.WHITE))
                .append(Component.text(server, CYBER_BLUE))
                .append(Component.text("...", NamedTextColor.WHITE));
    }

    // Ender Bow
    public static final Component ENDER_BOW_NAME = Component.text("Ender Bow", ENDER_PURPLE)
            .decorate(TextDecoration.BOLD)
            .decoration(TextDecoration.ITALIC, false);

    public static final List<Component> ENDER_BOW_LORE = List.of(
            Component.text("Shoots an ender pearl instead", NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, false),
            Component.text("of an arrow — you land where it hits.", NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, false),
            Component.text("No arrows needed. 3s cooldown.", NamedTextColor.DARK_GRAY)
                    .decoration(TextDecoration.ITALIC, false)
    );
}
