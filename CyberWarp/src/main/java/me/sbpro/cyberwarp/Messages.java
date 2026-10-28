package me.sbpro.cyberwarp;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public final class Messages {

    public static final TextColor CUSTOM = TextColor.color(0x02D8E9);

    public static final Component PREFIX = Component.text()
            .append(Component.text("WARP", CUSTOM, TextDecoration.BOLD))
            .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
            .build();

    public static final Component PLAYER_ONLY = Component.text(
            "You must be a player to use this command.",
            NamedTextColor.RED
    );

    public static Component warpLocationNotSet(String warpName) {
        return PREFIX
            .append(Component.text("No ", NamedTextColor.RED))
            .append(Component.text(warpName, NamedTextColor.RED))
            .append(Component.text(" location has been set. Please create a ticket in the discord immediately.", NamedTextColor.RED));
    }

    public static Component teleportingToWarp(String warpName) {
        return PREFIX
            .append(Component.text("Teleporting to ", NamedTextColor.WHITE))
            .append(Component.text(warpName, CUSTOM))
            .append(Component.text(" in ", NamedTextColor.WHITE))
            .append(Component.text("5", CUSTOM))
            .append(Component.text(" seconds.", NamedTextColor.WHITE));
    }

    public static final Component TELEPORT_CANCELLED_MOVED = PREFIX
            .append(Component.text("Teleportation cancelled — you moved!", NamedTextColor.RED));

    public static Component teleportedToWarp(String warpName) {
        return PREFIX
            .append(Component.text("Teleported to ", NamedTextColor.WHITE))
            .append(Component.text(warpName, CUSTOM))
            .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static final Component CRATES_SET = PREFIX
            .append(Component.text("Crates location set!", NamedTextColor.GREEN));

    public static final Component SPAWN_SET = PREFIX
            .append(Component.text("Spawn location set!", NamedTextColor.GREEN));

    public static final Component WARZONE_SET = PREFIX
            .append(Component.text("Warzone location set!", NamedTextColor.GREEN));

    public static Component teleportCountdown(int seconds) {
        return PREFIX
                .append(Component.text("Teleporting in ", NamedTextColor.WHITE))
                .append(Component.text(seconds, CUSTOM))
                .append(Component.text("s", NamedTextColor.WHITE));
    }

    private Messages() {
    }
}
