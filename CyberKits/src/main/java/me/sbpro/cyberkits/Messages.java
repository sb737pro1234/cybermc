package me.sbpro.cyberkits;

import java.util.ArrayList;
import java.util.List;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import org.bukkit.Material;

import me.sbpro.cyberkits.kit.Kit;

public final class Messages {

    private Messages() {}

    public static final TextColor CUSTOM = TextColor.color(0x43A047);

    public static final Component PREFIX = Component.text()
            .append(Component.text("KITS", CUSTOM, TextDecoration.BOLD))
            .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
            .build();

    // Sounds
    public static final Sound SOUND_CLAIM = Sound.sound(Key.key("entity.player.levelup"), Sound.Source.MASTER, 1F, 1.2F);
    public static final Sound SOUND_ERROR = Sound.sound(Key.key("entity.villager.no"), Sound.Source.MASTER, 1F, 1F);
    public static final Sound SOUND_NOTE_OPEN = Sound.sound(Key.key("item.book.page_turn"), Sound.Source.MASTER, 1F, 1F);

    // Standard errors (unprefixed)
    public static final Component PLAYER_ONLY = Component.text(
            "You must be a player to use this command.",
            NamedTextColor.RED
    );

    public static final Component NO_PERMISSION = Component.text(
            "You do not have permission to use this command.",
            NamedTextColor.RED
    );

    // Command feedback
    public static final Component INVALID_PLAYER = error("That player could not be found.");
    public static final Component INVALID_KIT = error("That kit could not be found.");
    public static final Component USAGE = error("Usage: /kits [editor [kit] | reload | cooldown <reset|lock> <player> [kit]]");
    public static final Component USAGE_COOLDOWN = error("Usage: /kits cooldown <reset|lock> <player> [kit]");

    public static final Component RELOAD_SUCCESS = PREFIX
            .append(Component.text("Reloaded ", NamedTextColor.WHITE))
            .append(Component.text("kititems.yml", CUSTOM))
            .append(Component.text(" and ", NamedTextColor.WHITE))
            .append(Component.text("cooldowns.yml", CUSTOM))
            .append(Component.text(".", NamedTextColor.WHITE));

    /** kit == null means all kits. */
    public static Component cooldownReset(String player, Kit kit) {
        return PREFIX
                .append(Component.text("Reset ", NamedTextColor.WHITE))
                .append(kitTarget(kit))
                .append(Component.text(" for ", NamedTextColor.WHITE))
                .append(Component.text(player, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    /** kit == null means all kits. */
    public static Component cooldownLocked(String player, Kit kit) {
        return PREFIX
                .append(Component.text("Restarted ", NamedTextColor.WHITE))
                .append(kitTarget(kit))
                .append(Component.text(" for ", NamedTextColor.WHITE))
                .append(Component.text(player, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    private static Component kitTarget(Kit kit) {
        return kit == null
                ? Component.text("all kit cooldowns", CUSTOM)
                : Component.text("the ", NamedTextColor.WHITE)
                        .append(kit.displayName())
                        .append(Component.text(" cooldown", NamedTextColor.WHITE));
    }

    // Claiming
    public static final Component KIT_EMPTY = error("This kit has no items yet.");

    public static Component kitClaimed(Kit kit) {
        return PREFIX
                .append(Component.text("You claimed the ", NamedTextColor.WHITE))
                .append(kit.displayName())
                .append(Component.text("! Right-click the ", NamedTextColor.WHITE))
                .append(Component.text("Kit Note", CUSTOM))
                .append(Component.text(" to open it.", NamedTextColor.WHITE));
    }

    public static Component kitLocked(Kit kit) {
        return PREFIX
                .append(Component.text("You need the ", NamedTextColor.RED))
                .append(kit.prefixComponent())
                .append(Component.text(" rank to claim this kit.", NamedTextColor.RED));
    }

    public static Component kitOnCooldown(Kit kit, long millis) {
        return PREFIX
                .append(Component.text("You can claim the ", NamedTextColor.RED))
                .append(kit.displayName())
                .append(Component.text(" again in ", NamedTextColor.RED))
                .append(Component.text(formatTime(millis), CUSTOM))
                .append(Component.text(".", NamedTextColor.RED));
    }

    public static Component firstJoinKit(Kit kit) {
        return PREFIX
                .append(Component.text("Welcome! You received a ", NamedTextColor.WHITE))
                .append(kit.displayName())
                .append(Component.text(" Note. Right-click it to open it, and use ", NamedTextColor.WHITE))
                .append(Component.text("/kits", CUSTOM))
                .append(Component.text(" to see all kits.", NamedTextColor.WHITE));
    }

    // Kits menu
    public static final Component MENU_TITLE = Component.text("Kits", CUSTOM, TextDecoration.BOLD);

    public static Component kitName(Kit kit) {
        return kit.displayName();
    }

    public static List<Component> kitLoreClaimable(Kit kit) {
        List<Component> lore = new ArrayList<>();
        lore.add(plain("Status: ", NamedTextColor.GRAY).append(plain("Claimable", NamedTextColor.GREEN)));
        lore.add(cooldownLine(kit));
        lore.add(Component.empty());
        lore.add(plain("Left-click to claim a Kit Note!", NamedTextColor.GREEN));
        lore.add(plain("Right-click to preview.", NamedTextColor.GRAY));
        return lore;
    }

    public static List<Component> kitLoreCooldown(Kit kit, long millis) {
        List<Component> lore = new ArrayList<>();
        lore.add(plain("Status: ", NamedTextColor.GRAY).append(plain("Claimed", NamedTextColor.GOLD)));
        lore.add(plain("Available in: ", NamedTextColor.GRAY).append(plain(formatTime(millis), CUSTOM)));
        lore.add(cooldownLine(kit));
        lore.add(Component.empty());
        lore.add(plain("Right-click to preview.", NamedTextColor.GRAY));
        return lore;
    }

    public static List<Component> kitLoreLocked(Kit kit) {
        List<Component> lore = new ArrayList<>();
        lore.add(plain("Status: ", NamedTextColor.GRAY).append(plain("Locked", NamedTextColor.RED)));
        lore.add(cooldownLine(kit));
        lore.add(Component.empty());
        lore.add(plain("You do not have permission", NamedTextColor.RED));
        lore.add(plain("to claim this kit. Requires the ", NamedTextColor.RED)
                .append(kit.prefixComponent())
                .append(plain(" rank.", NamedTextColor.RED)));
        lore.add(Component.empty());
        lore.add(plain("Right-click to preview.", NamedTextColor.GRAY));
        return lore;
    }

    private static Component cooldownLine(Kit kit) {
        return plain("Cooldown: ", NamedTextColor.GRAY).append(plain(formatTime(kit.cooldown().toMillis()), NamedTextColor.WHITE));
    }

    // Preview
    public static Component previewTitle(Kit kit) {
        return kit.displayName();
    }

    public static final Component PREVIEW_BACK_NAME = bold("Back", NamedTextColor.RED);
    public static final List<Component> PREVIEW_BACK_LORE = List.of(plain("Return to the kits menu.", NamedTextColor.GRAY));

    // Editor
    public static final Component EDITOR_SELECT_TITLE = Component.text("Kit Editor", CUSTOM, TextDecoration.BOLD);

    public static List<Component> editorSelectLore(int items) {
        return List.of(
                plain("Items: ", NamedTextColor.GRAY).append(plain(String.valueOf(items), CUSTOM)),
                Component.empty(),
                plain("Click to edit this kit's items.", NamedTextColor.GREEN)
        );
    }

    public static Component editorTitle(Kit kit) {
        return Component.text("Editing: ", CUSTOM, TextDecoration.BOLD)
                .append(kit.displayName());
    }

    public static Component editorOpened(Kit kit) {
        return PREFIX
                .append(Component.text("Editing the ", NamedTextColor.WHITE))
                .append(kit.displayName())
                .append(Component.text(". Place the items to give, then close the menu to save.", NamedTextColor.WHITE));
    }

    public static Component editorSaved(Kit kit, int items) {
        return PREFIX
                .append(Component.text("Saved ", NamedTextColor.WHITE))
                .append(Component.text(items + (items == 1 ? " item" : " items"), CUSTOM))
                .append(Component.text(" to the ", NamedTextColor.WHITE))
                .append(kit.displayName())
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    // Kit notes (given on claim; right-click to get the items, can be passed to other players)
    public static final Material KIT_NOTE_MATERIAL = Material.PAPER;

    public static final Component KIT_NOTE_NAME = bold("Kit Note", CUSTOM);

    public static List<Component> kitNoteLore(Kit kit, String claimedBy) {
        return List.of(
                plain("Kit: ", NamedTextColor.GRAY).append(kit.displayName()),
                plain("Claimed by: ", NamedTextColor.GRAY).append(plain(claimedBy, NamedTextColor.WHITE)),
                Component.empty(),
                plain("Right-click to open this kit.", NamedTextColor.GREEN),
                plain("Can be given to other players.", NamedTextColor.GRAY)
        );
    }

    public static final Component NOTE_INVENTORY_FULL = error("You need a free inventory slot for the Kit Note.");
    public static final Component NOTE_NO_SPACE = error("You do not have enough inventory space to open this kit.");
    public static final Component NOTE_ALREADY_USED = error("This Kit Note has already been used.");
    public static final Component NOTE_ERROR = error("This Kit Note could not be opened. No items were given.");
    public static final Component NOTE_UNKNOWN_KIT = error("This Kit Note is for a kit that no longer exists.");
    public static final Component NOTE_NO_CRAFT = error("Kit Notes cannot be used in crafting.");

    public static Component kitNoteOpened(Kit kit) {
        return PREFIX
                .append(Component.text("You opened the ", NamedTextColor.WHITE))
                .append(kit.displayName())
                .append(Component.text("!", NamedTextColor.WHITE));
    }

    // Helpers
    private static Component error(String text) {
        return PREFIX.append(Component.text(text, NamedTextColor.RED));
    }

    private static Component plain(String text, TextColor color) {
        return Component.text(text, color).decoration(TextDecoration.ITALIC, false);
    }

    private static Component bold(String text, TextColor color) {
        return plain(text, color).decorate(TextDecoration.BOLD);
    }

    /** e.g. 2d 5h 30m; seconds are only shown under an hour. */
    public static String formatTime(long millis) {
        long seconds = Math.max(0, (millis + 999) / 1000);
        long days = seconds / 86400;
        long hours = seconds % 86400 / 3600;
        long minutes = seconds % 3600 / 60;
        long secs = seconds % 60;

        StringBuilder builder = new StringBuilder();
        if (days > 0) builder.append(days).append("d ");
        if (hours > 0) builder.append(hours).append("h ");
        if (minutes > 0) builder.append(minutes).append("m ");
        if (seconds < 3600 && (secs > 0 || builder.isEmpty())) builder.append(secs).append("s ");
        return builder.toString().trim();
    }
}
