package me.sbpro.cyberprotect;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public final class Messages {

    public static final TextColor CUSTOM = TextColor.color(0xFFD600);

    public static final Component PREFIX = Component.text()
            .append(Component.text("WORLD", CUSTOM, TextDecoration.BOLD))
            .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
            .build();

    /*
     * ==========================================
     * COMMAND MESSAGES
     * ==========================================
     */

    public static final Component NO_PERMISSION = Component.text(
            "You do not have permission to use this command.",
            NamedTextColor.RED
    );

    public static final Component RELOADED = PREFIX
            .append(Component.text("Configuration reloaded successfully.", NamedTextColor.WHITE));

    public static final Component INVALID_ARGUMENT = PREFIX
            .append(Component.text("Usage: ", NamedTextColor.WHITE))
            .append(Component.text("/cyberprotect reload", NamedTextColor.YELLOW));

    /*
     * ==========================================
     * PROTECTION MESSAGES
     * ==========================================
     */

    public static final Component BLOCK_BREAK = PREFIX
            .append(Component.text("You cannot break blocks here.", NamedTextColor.WHITE));

    public static final Component BLOCK_PLACE = PREFIX
            .append(Component.text("You cannot place blocks here.", NamedTextColor.WHITE));

    public static final Component BLOCK_INTERACT = PREFIX
            .append(Component.text("You cannot interact with blocks here.", NamedTextColor.WHITE));

    public static final Component DOORS = PREFIX
            .append(Component.text("You cannot use doors here.", NamedTextColor.WHITE));

    public static final Component TRAPDOORS = PREFIX
            .append(Component.text("You cannot use trapdoors here.", NamedTextColor.WHITE));

    public static final Component ENTITY_INTERACT = PREFIX
            .append(Component.text("You cannot interact with entities here.", NamedTextColor.WHITE));

    public static final Component ITEM_INTERACT = PREFIX
            .append(Component.text("You cannot use items here.", NamedTextColor.WHITE));

    public static final Component WINDCHARGES = PREFIX
            .append(Component.text("You cannot use wind charges here.", NamedTextColor.WHITE));

    public static final Component EAT = PREFIX
            .append(Component.text("You cannot eat here.", NamedTextColor.WHITE));

    public static final Component PVP = PREFIX
            .append(Component.text("You cannot PvP here.", NamedTextColor.WHITE));

    public static final Component ITEM_DROP = PREFIX
            .append(Component.text("You cannot drop items here.", NamedTextColor.WHITE));

    public static final Component ITEM_PICKUP = PREFIX
            .append(Component.text("You cannot pick up items here.", NamedTextColor.WHITE));

    private Messages() {
    }
}
