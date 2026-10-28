package me.sbpro.cyberhomes;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.List;

public final class Messages {

    public static final TextColor CUSTOM = TextColor.color(0x00AA00);

    public static final Component PREFIX = Component.text()
            .append(Component.text("HOMES", CUSTOM, TextDecoration.BOLD))
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

    // Chat messages
    public static final Component NO_PERMISSION_SLOT = error("No permission.");
    public static final Component HOMES_RELOADED = info("Homes reloaded.");
    public static final Component TELEPORT_IN_FIVE = PREFIX
            .append(Component.text("Teleporting in ", NamedTextColor.WHITE))
            .append(Component.text("5", CUSTOM))
            .append(Component.text(" seconds.", NamedTextColor.WHITE));
    public static final Component TELEPORT_CANCELLED_MOVED = error("Teleport cancelled because you moved.");
    public static final Component TELEPORT_CANCELLED = error("Teleport cancelled.");
    public static final Component TELEPORTED = info("You have been teleported.");
    public static final Component HOME_DELETED = info("Home deleted.");
    public static final Component SET_HOME_FIRST = info("Set your home first.");
    public static final Component CANNOT_MODIFY_HOMES = info("You cannot modify this player's homes.");
    public static final Component CANNOT_SET_HOME_HERE = info("You cannot set a home here.");
    public static final Component HOME_CREATED = info("Home created.");
    public static final Component HOME_NOT_EXIST = info("That home does not exist.");
    public static final Component PROVIDE_HOME_NAME = info("Please provide a home name.");
    public static final Component NO_HOME_SLOTS = info("You have no available home slots.");

    // Menu titles and buttons
    public static final Component HOMES_TITLE = Component.text("ʜᴏᴍᴇѕ", CUSTOM);
    public static final Component RENAME_TITLE = Component.text("Rename Home");
    public static final Component CANCEL_BUTTON = item("Cancel", NamedTextColor.RED);
    public static final Component CONFIRM_BUTTON = item("Confirm", NamedTextColor.GREEN);
    public static final Component NO_PERMISSION_BUTTON = item("No Permission", NamedTextColor.RED);

    // Menu lore
    public static final List<Component> NO_ACCESS_LORE = List.of(
            Component.empty(),
            lore("You do not have access", NamedTextColor.RED),
            lore("to this home slot.", NamedTextColor.RED)
    );
    public static final List<Component> TELEPORT_LORE = List.of(
            Component.empty(),
            lore("Click to teleport.", NamedTextColor.WHITE),
            Component.empty(),
            lore("Teleport takes 5 seconds.", CUSTOM)
    );
    public static final List<Component> NOT_SET_LORE = List.of(
            Component.empty(),
            lore("This home has not been set.", NamedTextColor.RED),
            Component.empty(),
            lore("Use the button below to create it.", NamedTextColor.WHITE)
    );
    public static final List<Component> SLOT_LOCKED_LORE = List.of(
            Component.empty(),
            lore("You cannot use this slot.", NamedTextColor.RED)
    );
    public static final List<Component> MANAGE_LORE = List.of(
            Component.empty(),
            lore("Left click to delete.", NamedTextColor.WHITE),
            lore("Right click to rename.", NamedTextColor.WHITE)
    );
    public static final List<Component> CREATE_LORE = List.of(
            Component.empty(),
            lore("Click to create this home.", NamedTextColor.WHITE)
    );
    public static final List<Component> CANCEL_LORE = List.of(
            Component.empty(),
            lore("Click to cancel.", NamedTextColor.WHITE)
    );
    public static final List<Component> DELETE_PREVIEW_LORE = List.of(
            Component.empty(),
            lore("This home will be deleted.", NamedTextColor.WHITE)
    );
    public static final List<Component> CONFIRM_LORE = List.of(
            Component.empty(),
            lore("Click to delete this home.", NamedTextColor.WHITE)
    );

    private Messages() {
    }

    private static Component info(String text) {
        return PREFIX.append(Component.text(text, NamedTextColor.WHITE));
    }

    private static Component error(String text) {
        return PREFIX.append(Component.text(text, NamedTextColor.RED));
    }

    private static Component item(String text, TextColor color) {
        return Component.text(text, color);
    }

    private static Component lore(String text, TextColor color) {
        return Component.text(text, color);
    }

    // Chat messages with values

    public static Component usage(String usage) {
        return PREFIX
                .append(Component.text("Usage: ", NamedTextColor.RED))
                .append(Component.text(usage, NamedTextColor.WHITE));
    }

    public static Component homeMissing(String name) {
        return PREFIX
                .append(Component.text("Home ", NamedTextColor.WHITE))
                .append(Component.text(name, CUSTOM))
                .append(Component.text(" does not exist.", NamedTextColor.WHITE));
    }

    public static Component playerMissing(String name) {
        return PREFIX
                .append(Component.text("Player ", NamedTextColor.WHITE))
                .append(Component.text(name, CUSTOM))
                .append(Component.text(" has never played or cannot be found.", NamedTextColor.WHITE));
    }

    public static Component teleportingTo(String homeName) {
        return PREFIX
                .append(Component.text("Teleporting to ", NamedTextColor.WHITE))
                .append(Component.text(homeName, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component homeRenamed(String name) {
        return PREFIX
                .append(Component.text("Home renamed to ", NamedTextColor.WHITE))
                .append(Component.text(name, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component nameTooLong(int maxLength) {
        return PREFIX
                .append(Component.text("Home names cannot be longer than ", NamedTextColor.WHITE))
                .append(Component.text(maxLength, CUSTOM))
                .append(Component.text(" characters.", NamedTextColor.WHITE));
    }

    public static Component alreadyHaveHome(String name) {
        return PREFIX
                .append(Component.text("You already have a home named ", NamedTextColor.WHITE))
                .append(Component.text(name, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component homeCreatedNamed(String name) {
        return PREFIX
                .append(Component.text("Home ", NamedTextColor.WHITE))
                .append(Component.text(name, CUSTOM))
                .append(Component.text(" created.", NamedTextColor.WHITE));
    }

    public static Component teleportCountdown(int seconds) {
        return PREFIX
                .append(Component.text("Teleporting in ", NamedTextColor.WHITE))
                .append(Component.text(seconds, CUSTOM))
                .append(Component.text("s", NamedTextColor.WHITE));
    }

    // Menu text with values

    public static Component homesTitleOther(String targetName) {
        return Component.text("ʜᴏᴍᴇѕ", CUSTOM)
                .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
                .append(Component.text(targetName, NamedTextColor.WHITE));
    }

    public static Component viewTitle(String targetName) {
        return Component.text("Homes View", CUSTOM)
                .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
                .append(Component.text(targetName, NamedTextColor.WHITE));
    }

    public static Component deleteTitle(int homeNumber) {
        return Component.text("Delete Home", CUSTOM)
                .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
                .append(Component.text("Home" + homeNumber, NamedTextColor.WHITE));
    }

    public static Component noAccessName(int number) {
        return item("Home " + number, NamedTextColor.RED);
    }

    public static Component homeName(String name) {
        return item(name, CUSTOM);
    }

    public static Component emptyHomeName(int number) {
        return item("Home " + number, NamedTextColor.GRAY);
    }

    public static Component manageName(String name) {
        return item("Manage " + name, CUSTOM);
    }

    public static Component setHomeName(int number) {
        return item("Set Home " + number, NamedTextColor.GRAY);
    }
}
