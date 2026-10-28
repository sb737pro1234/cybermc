package me.sbpro.cyberstaff;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import me.sbpro.cyberstaff.punishment.OffenceRecord;
import me.sbpro.cyberstaff.punishment.Punishment;
import me.sbpro.cyberstaff.punishment.PunishmentTier;
import me.sbpro.cyberstaff.sus.SusNote;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class Messages {

    /** Cyber network colour (used on ban/mute screens and offence headers). */
    public static final TextColor BRAND = TextColor.color(0x00AAFF);

    public static final TextColor CUSTOM = TextColor.color(0x2979FF);

    public static final Component PREFIX = Component.text()
            .append(Component.text("STAFF", CUSTOM, TextDecoration.BOLD))
            .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
            .build();

    public static final Component PLAYER_ONLY = Component.text(
            "You must be a deleteLorePlayer to use this command.",
            NamedTextColor.RED
    );

    public static final Component NO_PERMISSION = Component.text(
            "You do not have permission to use this command.",
            NamedTextColor.RED
    );

    public static final Component COMMAND_NOT_AVAILABLE = PREFIX
            .append(Component.text("This command is not available. Please use /offend", NamedTextColor.WHITE));

    public static final Component CHAT_LOCKED = PREFIX
            .append(Component.text("Chat is now locked.", NamedTextColor.RED));

    public static final Component CHAT_UNLOCKED = PREFIX
            .append(Component.text("Chat has been unlocked. You may type as normal.", NamedTextColor.GREEN));

    public static final Component CHAT_CURRENTLY_LOCKED = PREFIX
            .append(Component.text("The chat is currently locked. Please wait......", NamedTextColor.RED));

    public static final Component MESSAGE = Component.empty();

    public static final Component CHAT_CLEARED_BY_PREFIX = PREFIX
            .append(Component.text("Chat has been cleared by ", NamedTextColor.WHITE));

    public static final Component CLEAR_CHAT_TITLE = Component.text("CLEAR CHAT", CUSTOM, TextDecoration.BOLD);

    public static final Component CANCEL = Component.text("CANCEL", NamedTextColor.RED, TextDecoration.BOLD)
            .decoration(TextDecoration.ITALIC, false);

    public static final Component CLEAR_CHAT_ITEM_NAME = Component.text("Clear Chat", CUSTOM, TextDecoration.BOLD)
            .decoration(TextDecoration.ITALIC, false);

    public static final Component CLEAR_CHAT_LORE_1 = Component.text("This will clear the public chat", NamedTextColor.WHITE)
            .decoration(TextDecoration.ITALIC, false);

    public static final Component CLEAR_CHAT_LORE_2 = Component.text("for everyone currently online.", NamedTextColor.WHITE)
            .decoration(TextDecoration.ITALIC, false);

    public static Component clearChatLoreLines(int CLEAR_LINES) {
        return Component.text("You will send ", NamedTextColor.WHITE)
            .append(Component.text(CLEAR_LINES, CUSTOM))
            .append(Component.text(" ", CUSTOM))
            .append(Component.text("blank lines.", NamedTextColor.WHITE))
            .decoration(TextDecoration.ITALIC, false);
    }

    public static final Component CONFIRM = Component.text("CONFIRM", NamedTextColor.GREEN, TextDecoration.BOLD)
            .decoration(TextDecoration.ITALIC, false);

    public static Component chatClearedBy(String clearer) {
        return PREFIX
            .append(Component.text("Chat has been cleared by ", NamedTextColor.WHITE))
            .append(Component.text(clearer, CUSTOM));
    }

    /** Plain-text start of the signed line, used to detect items that were already signed. */
    public static final String SIGNED_BY_PLAIN_PREFIX = "Signed by ";

    public static Component signedBy(String playerName) {
        return Component.text(SIGNED_BY_PLAIN_PREFIX, NamedTextColor.WHITE)
                .append(Component.text(playerName, CUSTOM))
                .decoration(TextDecoration.ITALIC, false);
    }

    public static final Component SIGN_NEEDS_ITEM = PREFIX
            .append(Component.text("You must be holding an item to sign it.", NamedTextColor.RED));

    public static final Component SIGN_NO_LORE = PREFIX
            .append(Component.text("This item cannot have lore added to it.", NamedTextColor.RED));

    public static final Component SIGN_ALREADY_SIGNED = PREFIX
            .append(Component.text("This item has already been signed.", NamedTextColor.RED));

    public static final Component SIGN_SUCCESS = PREFIX
            .append(Component.text("Item signed.", NamedTextColor.GREEN));

    public static final Component STATUS_REMOVED = Component.text("Removed", NamedTextColor.RED);

    public static final Component STATUS_REPLACED = Component.text("Replaced", NamedTextColor.YELLOW);

    public static final Component STATUS_EXPIRED = Component.text("Expired", NamedTextColor.GRAY);

    public static final Component STATUS_ACTIVE = Component.text("Active", NamedTextColor.GREEN);

    public static final Component STATUS_COMPLETED = Component.text("Completed", NamedTextColor.GRAY);

    public static final Component STAFF_CHAT_ENABLED = PREFIX
            .append(Component.text("Staff chat has been ", NamedTextColor.WHITE))
            .append(Component.text("enabled", NamedTextColor.GREEN))
            .append(Component.text(".", NamedTextColor.WHITE));

    public static final Component STAFF_CHAT_DISABLED = PREFIX
            .append(Component.text("Staff chat has been ", NamedTextColor.WHITE))
            .append(Component.text("disabled", NamedTextColor.RED))
            .append(Component.text(".", NamedTextColor.WHITE));

    public static final Component MESSAGE_5 = Component.text(" ")
            .append(Component.text("» ", NamedTextColor.DARK_GRAY));

    public static final Component MESSAGE_6 = Component.text(" » ", NamedTextColor.AQUA);

    public static Component susPlayersTitle(int susPage) {
        return Component.text("SUS PLAYERS ", CUSTOM, TextDecoration.BOLD)
            .append(Component.text("» ", NamedTextColor.DARK_GRAY))
            .append(Component.text(susPage, NamedTextColor.WHITE));
    }

    public static Component susPlayerName(String susPlayerName) {
        return Component.text(susPlayerName, CUSTOM, TextDecoration.BOLD)
            .decoration(TextDecoration.ITALIC, false);
    }

    public static Component susNoteReason(String noteReason) {
        return Component.text(noteReason, NamedTextColor.WHITE)
            .decoration(TextDecoration.ITALIC, false);
    }

    public static final Component SUS_LORE_SPECTATE = Component.text("Left click to spectate deleteLorePlayer", CUSTOM)
            .decoration(TextDecoration.ITALIC, false);

    public static final Component SUS_LORE_ADD = Component.text("Right click to change/add another deleteLoreReason", CUSTOM)
            .decoration(TextDecoration.ITALIC, false);

    public static final Component SUS_LORE_DELETE = Component.text("Shift click to delete deleteLoreReason", CUSTOM)
            .decoration(TextDecoration.ITALIC, false);

    public static final Component SUS_PREVIOUS_PAGE = Component.text("Previous Page", CUSTOM, TextDecoration.BOLD)
            .decoration(TextDecoration.ITALIC, false);

    public static Component susPage(int susPage, int totalPages) {
        return Component.text("Page ", CUSTOM, TextDecoration.BOLD)
            .append(Component.text(susPage, CUSTOM, TextDecoration.BOLD))
            .append(Component.text(" ", CUSTOM, TextDecoration.BOLD))
            .append(Component.text("/ ", NamedTextColor.WHITE))
            .append(Component.text(totalPages, NamedTextColor.WHITE))
            .decoration(TextDecoration.ITALIC, false);
    }

    public static final Component SUS_NEXT_PAGE = Component.text("Next Page", CUSTOM, TextDecoration.BOLD)
            .decoration(TextDecoration.ITALIC, false);

    public static Component addSusReasonTitle(String susPlayerName) {
        return Component.text("ADD SUS REASON ", CUSTOM, TextDecoration.BOLD)
            .append(Component.text("» ", NamedTextColor.DARK_GRAY))
            .append(Component.text(susPlayerName, NamedTextColor.WHITE));
    }

    public static final Component SUS_BACK = Component.text("Back", CUSTOM, TextDecoration.BOLD)
            .decoration(TextDecoration.ITALIC, false);

    public static Component deleteSusReasonTitle(String susPlayerName) {
        return Component.text("DELETE SUS REASON ", CUSTOM, TextDecoration.BOLD)
            .append(Component.text("» ", NamedTextColor.DARK_GRAY))
            .append(Component.text(susPlayerName, NamedTextColor.WHITE));
    }

    public static final Component CONFIRM_DELETE_TITLE = Component.text("CONFIRM DELETE", CUSTOM, TextDecoration.BOLD);

    public static final Component SUS_CANCEL = Component.text("Cancel", NamedTextColor.RED, TextDecoration.BOLD)
            .decoration(TextDecoration.ITALIC, false);

    public static final Component DELETE_SUS_REASON_ITEM = Component.text("Delete SUS Reason", CUSTOM, TextDecoration.BOLD)
            .decoration(TextDecoration.ITALIC, false);

    public static Component deleteLorePlayer(String susPlayerName) {
        return Component.text("Player: ", NamedTextColor.WHITE)
            .append(Component.text(susPlayerName, CUSTOM))
            .decoration(TextDecoration.ITALIC, false);
    }

    public static Component deleteLoreReason(String noteReason) {
        return Component.text("Reason: ", NamedTextColor.WHITE)
            .append(Component.text(noteReason, CUSTOM))
            .decoration(TextDecoration.ITALIC, false);
    }

    public static final Component CANNOT_BE_UNDONE = Component.text("This cannot be undone.", NamedTextColor.RED)
            .decoration(TextDecoration.ITALIC, false);

    public static final Component SUS_CONFIRM = Component.text("Confirm", NamedTextColor.GREEN, TextDecoration.BOLD)
            .decoration(TextDecoration.ITALIC, false);

    public static Component susReasonOption(String reasonDisplayName) {
        return Component.text(reasonDisplayName, CUSTOM)
            .decoration(TextDecoration.ITALIC, false);
    }

    public static final Component CLICK_TO_ADD_REASON = Component.text("Click to add this SUS deleteLoreReason.", NamedTextColor.WHITE)
            .decoration(TextDecoration.ITALIC, false);

    public static Component susNoteOption(String noteReason) {
        return Component.text(noteReason, CUSTOM)
            .decoration(TextDecoration.ITALIC, false);
    }

    public static final Component CLICK_TO_REMOVE_REASON = Component.text("Click to remove this deleteLoreReason.", NamedTextColor.WHITE)
            .decoration(TextDecoration.ITALIC, false);

    // ============================================================
    // General
    // ============================================================

    public static final Component PLAYER_NOT_FOUND = error("That player could not be identified.");
    public static final Component UNKNOWN_PLAYER = error("That player has never joined the network and cannot be identified.");
    public static final Component REASON_NOT_FOUND = error("That is not a valid punishment reason.");
    public static final Component INVALID_PUNISHMENT = error("The punishment configuration for this offence is invalid.");
    public static final Component DATABASE_ERROR = error("A database error occurred. Please try again.");
    public static final Component RELOAD_SUCCESS = PREFIX.append(Component.text("Configuration reloaded.", NamedTextColor.GREEN));

    // ============================================================
    // Usage
    // ============================================================

    public static final Component STAFF_RELOAD_USAGE = usage("/cyberstaff reload");
    public static final Component OFFEND_USAGE = usage("/offend <player> <reason>");
    public static final Component OFFENCE_USAGE = usage("/offence <new|check|list|change|remove> ...");
    public static final Component OFFENCE_REMOVE_USAGE = usage("/offence remove <player>");
    public static final Component OFFENCE_NEW_USAGE = usage("/offence new <player> <reason>");
    public static final Component OFFENCE_CHECK_USAGE = usage("/offence check <player>");
    public static final Component OFFENCE_CHANGE_USAGE = usage("/offence change <player> <new reason>");
    public static final Component OFFENCE_RESET_USAGE = usage("/offence reset <player>");
    public static final Component SUS_USAGE = usage("/sus [add|remove] ...");
    public static final Component SUS_ADD_USAGE = usage("/sus add <player> <reason>");
    public static final Component SUS_REMOVE_USAGE = usage("/sus remove <player> <reason>");
    public static final Component SUS_REASON_TOO_LONG = error("That suspicion reason is too long.");

    // ============================================================
    // Offence lists and history
    // ============================================================

    private static final String LINE = "━━━━━━━━━━━━━━━━━━━━━━━━━━━━";

    private static Component line() {
        return Component.text(LINE, NamedTextColor.DARK_GRAY, TextDecoration.STRIKETHROUGH);
    }

    private static Component brandHeader(String title) {
        return Component.text("Cyber", BRAND, TextDecoration.BOLD)
                .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
                .append(Component.text(title, NamedTextColor.WHITE));
    }

    public static final Component OFFENCE_HISTORY_HEADER = Component.text()
            .append(line()).appendNewline().append(brandHeader("Offence History")).appendNewline().append(line()).build();

    public static final Component OFFENCE_HISTORY_EMPTY = Component.text("No offences have been recorded.", NamedTextColor.GRAY);

    public static final Component OFFENCE_LIST_HEADER = Component.text()
            .append(line()).appendNewline().append(brandHeader("Punishment Reasons")).appendNewline().append(line()).build();

    public static final Component OFFENCE_LIST_EMPTY = Component.text("No punishment reasons are configured.", NamedTextColor.GRAY);

    public static Component offenceListTier(String tier) {
        return Component.text(tier, NamedTextColor.GREEN, TextDecoration.BOLD);
    }

    public static Component offenceListReason(String reason) {
        return Component.text("• ", NamedTextColor.DARK_GRAY).append(Component.text(reason, NamedTextColor.WHITE));
    }

    public static Component offenceHistoryEntry(int offence, String reason, String tier, String punishment,
                                                String staff, String date, Component status) {
        return Component.text()
                .append(Component.text("#", NamedTextColor.DARK_GRAY))
                .append(Component.text(offence, NamedTextColor.WHITE))
                .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
                .append(Component.text(reason, NamedTextColor.WHITE)).appendNewline()
                .append(detail(" Tier: ", tier)).appendNewline()
                .append(detail(" Punishment: ", punishment)).appendNewline()
                .append(detail(" Staff: ", staff)).appendNewline()
                .append(detail(" Date: ", date)).appendNewline()
                .append(Component.text(" Status: ", NamedTextColor.GRAY)).append(status)
                .build();
    }

    private static Component detail(String label, String value) {
        return Component.text(label, NamedTextColor.GRAY).append(Component.text(value, NamedTextColor.WHITE));
    }

    // ============================================================
    // Punishment feedback
    // ============================================================

    public static Component noActivePunishment(String player) {
        return PREFIX
                .append(Component.text(player, CUSTOM))
                .append(Component.text(" does not have an active punishment.", NamedTextColor.GRAY));
    }

    public static Component punishmentRemoved(String player, int offence) {
        return PREFIX
                .append(Component.text("Removed the active punishment from ", NamedTextColor.WHITE))
                .append(Component.text(player, CUSTOM))
                .append(Component.text(". (", NamedTextColor.WHITE))
                .append(Component.text("Offence #" + offence, NamedTextColor.GRAY))
                .append(Component.text(")", NamedTextColor.WHITE));
    }

    public static Component punishmentChanged(String player, String reason, String tier, int offence) {
        return Component.text()
                .append(PREFIX).append(Component.text("Changed ", NamedTextColor.WHITE))
                .append(Component.text(player, CUSTOM)).append(Component.text("'s punishment.", NamedTextColor.WHITE)).appendNewline()
                .append(PREFIX).append(detail("Reason: ", reason)).appendNewline()
                .append(PREFIX).append(detail("Tier: ", tier)).appendNewline()
                .append(PREFIX).append(detail("Offence: #", String.valueOf(offence)))
                .build();
    }

    public static Component historyReset(String player) {
        return Component.text()
                .append(PREFIX).append(Component.text("Completely reset the punishment history of ", NamedTextColor.WHITE))
                .append(Component.text(player, CUSTOM)).append(Component.text(".", NamedTextColor.WHITE)).appendNewline()
                .append(PREFIX).append(Component.text("Their next offence will now start at ", NamedTextColor.GRAY))
                .append(Component.text("#1", CUSTOM)).append(Component.text(".", NamedTextColor.GRAY))
                .build();
    }

    public static Component resetFailed(String player) {
        return PREFIX
                .append(Component.text("Failed to reset the punishment history of ", NamedTextColor.RED))
                .append(Component.text(player, CUSTOM))
                .append(Component.text(".", NamedTextColor.RED));
    }

    public static List<Component> punishmentIssued(String player, String reason, PunishmentTier tier, int offence, Punishment punishment) {
        return issued(Component.text("Punishment issued.", NamedTextColor.GREEN), player, reason, tier, offence, punishment.display());
    }

    private static List<Component> issued(Component title, String player, String reason, PunishmentTier tier, int offence, String punishment) {
        return List.of(
                PREFIX.append(title),
                Component.empty(),
                summaryLine("Player: ", player),
                summaryLine("Reason: ", reason),
                summaryLine("Tier: ", tier.displayName()),
                summaryLine("Offence: #", String.valueOf(offence)),
                summaryLine("Punishment: ", punishment)
        );
    }

    private static Component summaryLine(String label, String value) {
        return Component.text(label, NamedTextColor.GRAY).append(Component.text(value, NamedTextColor.WHITE));
    }

    // ============================================================
    // Ban / mute screens
    // ============================================================

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault());

    public static String formatDate(Instant instant) {
        return DATE_FORMAT.format(instant);
    }

    private static String expiry(OffenceRecord record) {
        return record.expiresAt() == null ? "Permanent" : formatDate(record.expiresAt());
    }

    private static Component banDetail(String label, String value) {
        return Component.text(label + " » ", NamedTextColor.RED).append(Component.text(value, NamedTextColor.WHITE));
    }

    public static Component banScreen(OffenceRecord record) {
        Component bar = Component.text(LINE, NamedTextColor.DARK_GRAY);
        return Component.text()
                .appendNewline()
                .append(bar).appendNewline()
                .append(Component.text("Cyber", BRAND, TextDecoration.BOLD)).appendNewline()
                .append(bar).appendNewline().appendNewline()
                .append(Component.text("YOU HAVE BEEN PUNISHED", NamedTextColor.RED, TextDecoration.BOLD)).appendNewline().appendNewline()
                .append(banDetail("Staff Member", record.staffName())).appendNewline()
                .append(banDetail("Reason", record.reasonName())).appendNewline()
                .append(banDetail("Duration", "Permanent")).appendNewline()
                .append(banDetail("Date", formatDate(record.startedAt()))).appendNewline().appendNewline()
                .append(bar).appendNewline()
                .append(Component.text("APPEAL", NamedTextColor.RED, TextDecoration.BOLD)).appendNewline()
                .append(Component.text("Join our Discord to appeal your punishment", NamedTextColor.WHITE)).appendNewline()
                .append(Component.text("https://discord.gg/A97Dkcb7TU", NamedTextColor.AQUA, TextDecoration.UNDERLINED)).appendNewline()
                .append(bar)
                .build();
    }

    public static Component muted(OffenceRecord record) {
        return PREFIX
                .append(Component.text("You are currently muted.", NamedTextColor.RED))
                .append(Component.text(" Reason: ", NamedTextColor.GRAY))
                .append(Component.text(record.reasonName(), NamedTextColor.WHITE))
                .append(Component.text(" | Expires: ", NamedTextColor.GRAY))
                .append(Component.text(expiry(record), NamedTextColor.WHITE));
    }

    public static Component muteScreen(OffenceRecord record) {
        return muted(record);
    }

    // ============================================================
    // SUS
    // ============================================================

    public static final Component SUS_PLAYER_ONLY = PLAYER_ONLY;

    public static Component susAdded(SusNote note) {
        return PREFIX
                .append(Component.text("Added SUS to ", NamedTextColor.WHITE))
                .append(Component.text(note.playerName(), CUSTOM))
                .append(Component.text(": ", NamedTextColor.WHITE))
                .append(Component.text(note.reason(), CUSTOM));
    }

    public static Component susRemoved(SusNote note) {
        return PREFIX
                .append(Component.text("Removed SUS from ", NamedTextColor.WHITE))
                .append(Component.text(note.playerName(), CUSTOM))
                .append(Component.text(": ", NamedTextColor.WHITE))
                .append(Component.text(note.reason(), CUSTOM));
    }

    public static Component susAlreadyExists(String player, String reason) {
        return PREFIX
                .append(Component.text("That SUS reason already exists for ", NamedTextColor.WHITE))
                .append(Component.text(player, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component susReasonNotFound(String player, String reason) {
        return PREFIX
                .append(Component.text("That SUS reason does not exist for ", NamedTextColor.RED))
                .append(Component.text(player, NamedTextColor.WHITE))
                .append(Component.text(".", NamedTextColor.RED));
    }

    private static Component error(String text) {
        return PREFIX.append(Component.text(text, NamedTextColor.RED));
    }

    private static Component usage(String usage) {
        return PREFIX.append(Component.text("Usage: " + usage, NamedTextColor.RED));
    }

    // ============================================================
    // Staff guide
    // ============================================================

    public static final Component STAFF_GUIDE_TITLE = Component.text("Staff Guide", CUSTOM);
    public static final Component STAFF_PROTOCOL_TITLE = Component.text("Staff Protocol", CUSTOM);
    public static final Component STAFF_COMMANDS_TITLE = Component.text("Staff Commands", CUSTOM);

    public static final Component RULES_LINK_LINE = Component.text(
            "----------------------------------------",
            TextColor.color(0x555555)
    );

    public static final Component RULES_LINK_MESSAGE = Component.text()
            .append(Component.text("◆ ", TextColor.color(0x00C2FF)))
            .append(Component.text("[CLICK HERE]", TextColor.color(0x00C2FF), TextDecoration.BOLD))
            .append(Component.text(" Open the Cyber rules.", NamedTextColor.WHITE))
            .build();

    public static Component guideItemName(String name) {
        return Component.text(name, CUSTOM).decoration(TextDecoration.ITALIC, false);
    }

    public static Component guideArguments() {
        return Component.text("Arguments:", CUSTOM).decoration(TextDecoration.ITALIC, false);
    }

    public static Component guideStepNumber(int number) {
        return Component.text(number + ". ", CUSTOM);
    }

    public static Component guideCommand(String command) {
        return Component.text(command, CUSTOM);
    }

    private Messages() {
    }
}
