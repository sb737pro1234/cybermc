package me.sbpro.cyberalliances;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.List;

/**
 * Central place for all CyberAlliances messages and GUI text.
 *
 * Normal text is white; highlighted values use {@link #CUSTOM}.
 */
public final class Messages {

    public static final TextColor CUSTOM = TextColor.color(0x43A047);

    public static final Component PREFIX = Component.text()
            .append(Component.text("ALLIANCES", CUSTOM, TextDecoration.BOLD))
            .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
            .build();

    // Standard errors (unprefixed)
    public static final Component PLAYER_ONLY = Component.text(
            "You must be a player to use this command.",
            NamedTextColor.RED
    );

    // Usage
    public static final Component USAGE = PREFIX
            .append(Component.text("Usage: ", NamedTextColor.WHITE))
            .append(Component.text("/a <create|info|delete|chat|invite|accept|kick|leave|top|level>", CUSTOM));

    public static final Component CREATE_USAGE = PREFIX
            .append(Component.text("Usage: ", NamedTextColor.WHITE))
            .append(Component.text("/a create <name>", CUSTOM));

    public static final Component INVITE_USAGE = PREFIX
            .append(Component.text("Usage: ", NamedTextColor.WHITE))
            .append(Component.text("/a invite <player>", CUSTOM));

    public static final Component ACCEPT_USAGE = PREFIX
            .append(Component.text("Usage: ", NamedTextColor.WHITE))
            .append(Component.text("/a accept", CUSTOM));

    public static final Component KICK_USAGE = PREFIX
            .append(Component.text("Usage: ", NamedTextColor.WHITE))
            .append(Component.text("/a kick <player>", CUSTOM));

    // Notices
    public static final Component DELETE_OPENED = PREFIX
            .append(Component.text("Please confirm deletion in the GUI.", NamedTextColor.WHITE));

    public static final Component DELETE_CANCELLED = PREFIX
            .append(Component.text("Alliance deletion cancelled.", NamedTextColor.WHITE));

    public static final Component CHAT_ENABLED = PREFIX
            .append(Component.text("Alliance chat enabled.", NamedTextColor.WHITE));

    public static final Component CHAT_DISABLED = PREFIX
            .append(Component.text("Alliance chat disabled. You are back in global chat.", NamedTextColor.WHITE));

    public static final Component TOP_EMPTY = PREFIX
            .append(Component.text("No alliances have been created yet.", NamedTextColor.WHITE));

    public static final Component NO_NEXT_PAGE = PREFIX
            .append(Component.text("There is no next page.", NamedTextColor.WHITE));

    public static final Component NO_PREVIOUS_PAGE = PREFIX
            .append(Component.text("There is no previous page.", NamedTextColor.WHITE));

    // Errors
    public static final Component LEAVE_FAILED = PREFIX
            .append(Component.text("You could not leave the alliance. Please try again.", NamedTextColor.RED));

    public static final Component INVALID_NAME = PREFIX
            .append(Component.text("Alliance names must be plain text only. No hex colors, no & color codes.", NamedTextColor.RED));

    public static final Component NAME_LENGTH = PREFIX
            .append(Component.text("Alliance names must be between 3 and 24 characters.", NamedTextColor.RED));

    public static final Component ALREADY_IN_ALLIANCE = PREFIX
            .append(Component.text("You are already in an alliance.", NamedTextColor.RED));

    public static final Component ALLIANCE_EXISTS = PREFIX
            .append(Component.text("That alliance already exists.", NamedTextColor.RED));

    public static final Component ALLIANCE_FULL = PREFIX
            .append(Component.text("That alliance already has the maximum of 5 members.", NamedTextColor.RED));

    public static final Component OWNER_ONLY_INVITE = PREFIX
            .append(Component.text("Only the alliance owner can invite players.", NamedTextColor.RED));

    public static final Component OWNER_ONLY_DELETE = PREFIX
            .append(Component.text("Only the alliance owner can delete the alliance.", NamedTextColor.RED));

    public static final Component OWNER_ONLY_KICK = PREFIX
            .append(Component.text("Only the alliance owner can kick members.", NamedTextColor.RED));

    public static final Component NO_ALLIANCE = PREFIX
            .append(Component.text("You are not in an alliance.", NamedTextColor.RED));

    public static final Component NO_PENDING_INVITE = PREFIX
            .append(Component.text("You do not have a pending alliance invite.", NamedTextColor.RED));

    public static final Component TARGET_NOT_ONLINE = PREFIX
            .append(Component.text("That player must be online.", NamedTextColor.RED));

    public static final Component TARGET_ALREADY_IN_ALLIANCE = PREFIX
            .append(Component.text("That player is already in an alliance.", NamedTextColor.RED));

    public static final Component CANNOT_KICK_OWNER = PREFIX
            .append(Component.text("You cannot kick the alliance owner.", NamedTextColor.RED));

    public static final Component PLAYER_NOT_IN_YOUR_ALLIANCE = PREFIX
            .append(Component.text("That player is not in your alliance.", NamedTextColor.RED));

    public static final Component INVITE_EXPIRED = PREFIX
            .append(Component.text("Your pending alliance invite is no longer valid.", NamedTextColor.RED));

    public static final Component OWNER_ONLY_LEAVE = PREFIX
            .append(Component.text("You are the alliance owner. Use ", NamedTextColor.WHITE))
            .append(Component.text("/a delete", CUSTOM))
            .append(Component.text(" if you want to remove your alliance.", NamedTextColor.WHITE));

    public static final Component CANNOT_KICK_SELF = PREFIX
            .append(Component.text("Use ", NamedTextColor.WHITE))
            .append(Component.text("/a delete", CUSTOM))
            .append(Component.text(" if you want to remove your alliance.", NamedTextColor.WHITE));

    public static final List<Component> TOP_HEADER = List.of(
            Component.text("Alliance Top", CUSTOM),
            Component.text("Top alliances by alliance XP", NamedTextColor.WHITE)
    );

    // GUI titles and button names use the alliance colour, no italics/bold, and standard case.
    public static final Component INFO_GUI_TITLE = Component.text("Alliance Information", CUSTOM);
    public static final Component MEMBERS_GUI_TITLE = Component.text("Alliance Members", CUSTOM);
    public static final Component LEVEL_TITLE = Component.text("Alliance Level", CUSTOM);
    public static final Component XP_VALUES_TITLE = Component.text("Alliance XP", CUSTOM);
    public static final Component DELETE_TITLE = Component.text("Delete Alliance?", NamedTextColor.WHITE);

    public static final Component INFO_GUI_MEMBERS = itemName("Members");
    public static final Component INFO_GUI_LEVEL = itemName("Alliance Level");
    public static final Component INFO_GUI_XP_VALUES = itemName("Alliance XP");
    public static final Component LEVEL_GUI_TITLE = itemName("Alliance Information");
    public static final Component XP_VALUES_BUTTON = itemName("Alliance XP");
    public static final Component GO_BACK = itemName("Go Back");
    public static final Component NEXT_PAGE = itemName("Next Page");
    public static final Component PREVIOUS_PAGE = itemName("Previous Page");

    public static final Component DELETE_CANCEL_BUTTON = Component.text("Cancel", NamedTextColor.RED)
            .decoration(TextDecoration.ITALIC, false);
    public static final Component DELETE_CONFIRM_BUTTON = Component.text("Confirm", NamedTextColor.GREEN)
            .decoration(TextDecoration.ITALIC, false);

    // Button descriptions
    public static final Component VIEW_MEMBERS_LORE = lore("View everyone in your alliance.");
    public static final Component VIEW_XP_VALUES_LORE = lore("View how your alliance earns XP.");
    public static final Component CLOSE_MENU_LORE = lore("Close this menu");
    public static final Component RETURN_TO_INFO_LORE = lore("Return to alliance information");
    public static final Component RETURN_TO_LEVEL_LORE = lore("Return to alliance level");
    public static final Component VIEW_ALL_XP_SOURCES_LORE = lore("View all alliance XP sources");
    public static final Component OPEN_PREVIOUS_PAGE_LORE = lore("Open the previous page");
    public static final Component OPEN_NEXT_PAGE_LORE = lore("Open the next page");
    public static final Component OPEN_LEVEL_MENU_LORE = Component.text("Click to open the Alliance Level menu.", CUSTOM)
            .decoration(TextDecoration.ITALIC, false);
    public static final Component BLANK_LORE = Component.text(" ");
    public static final Component OWNER_ROLE = Component.text("Owner", NamedTextColor.WHITE)
            .decoration(TextDecoration.ITALIC, false);
    public static final Component MEMBER_ROLE = Component.text("Member", NamedTextColor.WHITE)
            .decoration(TextDecoration.ITALIC, false);
    public static final Component ONLINE_STATUS = Component.text("● Online", NamedTextColor.GREEN)
            .decoration(TextDecoration.ITALIC, false);
    public static final Component OFFLINE_STATUS = Component.text("● Offline", NamedTextColor.GRAY)
            .decoration(TextDecoration.ITALIC, false);

    private Messages() {
    }

    /** Item name without the default italics. */
    private static Component itemName(String text) {
        return Component.text(text, CUSTOM).decoration(TextDecoration.ITALIC, false);
    }

    /** White item lore line without the default italics. */
    private static Component lore(String text) {
        return Component.text(text, NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false);
    }

    /** "Label: value" lore line. */
    private static Component loreValue(String label, Object value) {
        return Component.text(label + ": ", NamedTextColor.WHITE)
                .append(Component.text(String.valueOf(value), CUSTOM))
                .decoration(TextDecoration.ITALIC, false);
    }

    // ---------------------------------------------------------------
    // Chat messages
    // ---------------------------------------------------------------

    public static Component notFound(String input) {
        return PREFIX
                .append(Component.text("No player or alliance matched '", NamedTextColor.WHITE))
                .append(Component.text(input, CUSTOM))
                .append(Component.text("'.", NamedTextColor.WHITE));
    }

    public static Component created(String allianceName) {
        return PREFIX
                .append(Component.text("Alliance ", NamedTextColor.WHITE))
                .append(Component.text("'" + allianceName + "'", CUSTOM))
                .append(Component.text(" created.", NamedTextColor.WHITE));
    }

    public static Component deleted(String allianceName) {
        return PREFIX
                .append(Component.text("Alliance ", NamedTextColor.WHITE))
                .append(Component.text("'" + allianceName + "'", CUSTOM))
                .append(Component.text(" deleted.", NamedTextColor.WHITE));
    }

    public static Component left(String allianceName) {
        return PREFIX
                .append(Component.text("You have left the alliance ", NamedTextColor.WHITE))
                .append(Component.text(allianceName, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component memberLeft(String playerName) {
        return PREFIX
                .append(Component.text(playerName, CUSTOM))
                .append(Component.text(" has left the alliance.", NamedTextColor.WHITE));
    }

    public static Component inviteSent(String playerName) {
        return PREFIX
                .append(Component.text("Invite sent to ", NamedTextColor.WHITE))
                .append(Component.text(playerName, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component inviteReceived(String allianceName, String ownerName) {
        return PREFIX
                .append(Component.text("You have been invited to join ", NamedTextColor.WHITE))
                .append(Component.text("'" + allianceName + "'", CUSTOM))
                .append(Component.text(" by ", NamedTextColor.WHITE))
                .append(Component.text(ownerName, CUSTOM))
                .append(Component.text(". Type ", NamedTextColor.WHITE))
                .append(Component.text("/a accept", CUSTOM))
                .append(Component.text(" to join.", NamedTextColor.WHITE));
    }

    public static Component joined(String allianceName) {
        return PREFIX
                .append(Component.text("You joined ", NamedTextColor.WHITE))
                .append(Component.text("'" + allianceName + "'", CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component memberJoined(String playerName) {
        return PREFIX
                .append(Component.text(playerName, CUSTOM))
                .append(Component.text(" has joined the alliance.", NamedTextColor.WHITE));
    }

    public static Component kickedTarget(String allianceName) {
        return PREFIX
                .append(Component.text("You were kicked from ", NamedTextColor.WHITE))
                .append(Component.text("'" + allianceName + "'", CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component kickedByOwner(String playerName) {
        return PREFIX
                .append(Component.text("You kicked ", NamedTextColor.WHITE))
                .append(Component.text(playerName, CUSTOM))
                .append(Component.text(" from the alliance.", NamedTextColor.WHITE));
    }

    public static Component kickedBroadcast(String playerName) {
        return PREFIX
                .append(Component.text(playerName, CUSTOM))
                .append(Component.text(" was kicked from the alliance.", NamedTextColor.WHITE));
    }

    public static Component levelUp(int level) {
        return PREFIX
                .append(Component.text("Your alliance reached level ", NamedTextColor.WHITE))
                .append(Component.text(level, CUSTOM))
                .append(Component.text("!", NamedTextColor.WHITE));
    }

    public static Component topEntry(int position, String allianceName, int level, long xp) {
        return PREFIX
                .append(Component.text("#" + position, CUSTOM))
                .append(Component.text(" " + allianceName, NamedTextColor.WHITE))
                .append(Component.text(" - ", NamedTextColor.DARK_GRAY))
                .append(Component.text("Level ", NamedTextColor.WHITE))
                .append(Component.text(level, CUSTOM))
                .append(Component.text(" - ", NamedTextColor.DARK_GRAY))
                .append(Component.text("XP: ", NamedTextColor.WHITE))
                .append(Component.text(xp, CUSTOM));
    }

    /** Alliance chat line. The player's message is plain text and is never parsed for formatting. */
    public static Component allianceChat(String allianceName, String playerName, String message) {
        return PREFIX
                .append(Component.text("[", NamedTextColor.DARK_GRAY))
                .append(Component.text(allianceName, CUSTOM))
                .append(Component.text("] ", NamedTextColor.DARK_GRAY))
                .append(Component.text(playerName, CUSTOM))
                .append(Component.text(": ", NamedTextColor.GRAY))
                .append(Component.text(message, NamedTextColor.WHITE));
    }

    /** Lines of /a info &lt;alliance&gt;. */
    public static List<Component> allianceInfo(String allianceName, String ownerName, String members) {
        Component separator = Component.text("==============================", NamedTextColor.DARK_GRAY, TextDecoration.STRIKETHROUGH);
        return List.of(
                separator,
                Component.text("Alliance: ", NamedTextColor.WHITE).append(Component.text(allianceName, CUSTOM)),
                Component.text("Owner: ", NamedTextColor.WHITE).append(Component.text(ownerName, CUSTOM)),
                Component.text("Members: ", NamedTextColor.WHITE).append(Component.text(members, CUSTOM)),
                separator
        );
    }

    // ---------------------------------------------------------------
    // GUI text
    // ---------------------------------------------------------------

    public static Component deleteAllianceName(String allianceName) {
        return itemName("Delete " + allianceName);
    }

    public static Component allianceItemName(String allianceName) {
        return itemName(allianceName);
    }

    public static List<Component> allianceOverviewLore(String ownerName, int memberCount, int level, long xp, Component progressBar) {
        return List.of(
                loreValue("Owner", ownerName),
                Component.text("Members: ", NamedTextColor.WHITE)
                        .append(Component.text(memberCount, CUSTOM))
                        .append(Component.text("/5", NamedTextColor.WHITE))
                        .decoration(TextDecoration.ITALIC, false),
                loreValue("Alliance Level", level),
                loreValue("Alliance XP", xp),
                Component.text("Progress: ", NamedTextColor.WHITE).append(progressBar)
                        .decoration(TextDecoration.ITALIC, false)
        );
    }

    public static List<Component> levelButtonLore(int level, long xp, Component progressBar) {
        return List.of(
                loreValue("Alliance Level", level),
                loreValue("Alliance XP", xp),
                Component.text("Progress: ", NamedTextColor.WHITE).append(progressBar)
                        .decoration(TextDecoration.ITALIC, false),
                BLANK_LORE,
                OPEN_LEVEL_MENU_LORE
        );
    }

    public static List<Component> levelOverviewLore(String allianceName, int level, long xp, Component progressBar) {
        List<Component> lore = new java.util.ArrayList<>(List.of(
                loreValue("Alliance", allianceName),
                loreValue("Current Alliance Level", level),
                loreValue("Current Alliance XP", xp),
                Component.text("Upgrade Progress: ", NamedTextColor.WHITE).append(progressBar)
                        .decoration(TextDecoration.ITALIC, false),
                BLANK_LORE
        ));
        lore.addAll(levelPerksLore());
        return lore;
    }

    public static List<Component> memberLore(boolean owner, boolean online) {
        return List.of(owner ? OWNER_ROLE : MEMBER_ROLE, online ? ONLINE_STATUS : OFFLINE_STATUS);
    }

    public static Component memberHeadName(String playerName) {
        return Component.text(playerName, NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false);
    }

    public static List<Component> xpEntryLore(String category, long xp) {
        return List.of(loreValue("Category", category), loreValue("Alliance XP", xp));
    }

    /** 20-segment progress bar: filled segments use the alliance colour, the rest are red. */
    public static Component progressBar(int filled) {
        Component bar = Component.empty();
        for (int index = 0; index < 20; index++) {
            bar = bar.append(Component.text("|", index < filled ? CUSTOM : NamedTextColor.RED));
        }
        return bar;
    }

    public static List<Component> levelPerksLore() {
        return List.of(
                perk(1, "Health Boost I"),
                perk(2, "+5% Alliance XP"),
                perk(3, "Speed I"),
                perk(4, "Haste I"),
                perk(5, "Strength I"),
                perk(6, "+10% Alliance XP"),
                perk(7, "+15% Alliance XP"),
                perk(8, "+17.5% Alliance XP"),
                perk(9, "No extra perk"),
                perk(10, "+20% XP, Strength II, Speed II, Health Boost II")
        );
    }

    private static Component perk(int level, String description) {
        return Component.text("Level " + level + ": ", NamedTextColor.WHITE)
                .append(Component.text(description, CUSTOM))
                .decoration(TextDecoration.ITALIC, false);
    }
}
