package me.sbpro.grassggstaff;

import me.sbpro.grassggstaff.punishment.OffenceRecord;
import me.sbpro.grassggstaff.punishment.Punishment;
import me.sbpro.grassggstaff.punishment.PunishmentTier;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class Messages {

    private Messages() {
    }
    
    
    public static final String CUSTOM = "§x§2§9§7§9§F§F";
    public static final String WHITE = "§f";
    public static final String RED = "§c";

    /*
     * ============================================================
     *                      GENERAL
     * ============================================================
     */

    public static final String PREFIX = CUSTOM + "§lSTAFF §8» §f";

    public static final String NO_PERMISSION =
            PREFIX + RED + "You do not have permission to use this command.";

    public static final String PLAYER_NOT_FOUND =
            PREFIX + RED + "That player could not be identified.";

    public static final String UNKNOWN_PLAYER =
            PREFIX + RED + "That player has never joined the network and cannot be identified.";

    public static final String REASON_NOT_FOUND =
            PREFIX + RED + "That is not a valid punishment reason.";

    public static final String INVALID_PUNISHMENT =
            PREFIX + RED + "The punishment configuration for this offence is invalid.";

    public static final String DATABASE_ERROR =
            PREFIX + RED + "A database error occurred. Please try again.";

    public static final String NO_ACTIVE_PUNISHMENT =
            PREFIX
                    + "§7%player% does not have an active punishment.";

    public static final String RELOAD_SUCCESS =
            PREFIX + "§aConfiguration reloaded.";

    /*
     * ============================================================
     *                       USAGE
     * ============================================================
     */

    public static final String OFFEND_USAGE =
            PREFIX + RED + "Usage: /offend <player> <reason>";

    public static final String OFFENCE_USAGE =
            PREFIX + RED + "Usage: /offence <new|check|list|change|remove> ...";

    /*
     * ============================================================
     *                  PUNISHMENT ISSUED
     * ============================================================
     */

    public static List<String> punishmentIssued(
            String player,
            String reason,
            PunishmentTier tier,
            int offence,
            Punishment punishment
    ) {
        return List.of(
                PREFIX + "§aPunishment issued.",
                "",
                "§7Player: §f" + player,
                "§7Reason: §f" + reason,
                "§7Tier: §f" + tier.displayName(),
                "§7Offence: §f#" + offence,
                "§7Punishment: §f" + punishment.display()
        );
    }

    public static List<String> warningIssued(
            String player,
            String reason,
            PunishmentTier tier,
            int offence
    ) {
        return List.of(
                PREFIX + "§eWarning issued.",
                "",
                "§7Player: §f" + player,
                "§7Reason: §f" + reason,
                "§7Tier: §f" + tier.displayName(),
                "§7Offence: §f#" + offence,
                "§7Punishment: §fWarning"
        );
    }

    /*
     * ============================================================
     *                     HISTORY
     * ============================================================
     */

    public static String historyHeader(String player) {
        return "§2§l" + player + "'s Offence History";
    }

    public static List<String> historyEntry(OffenceRecord record) {

        return List.of(
                "",
                "§8§m------------------------------",
                "§f§l#" + record.offenceId(),
                "§7Reason: §f" + record.reasonName(),
                "§7Tier: §f" + record.tier(),
                "§7Offence: §f#" + record.offenceNumber(),
                "§7Punishment: §f" + record.duration() + " " + record.punishmentType().getDisplayName(),
                "§7Staff: §f" + record.staffName(),
                "§7Date: §f" + formatDate(record.startedAt()),
                "§7Status: §f" + getStatus(record),
                "§8§m------------------------------"
        );
    }

    private static String getStatus(OffenceRecord record) {

        if (record.removed()) {
            return RED + "Removed";
        }

        if (record.expiresAt() != null &&
                record.expiresAt().isBefore(java.time.Instant.now())) {
            return "§7Expired";
        }

        if (record.active()) {
            return "§aActive";
        }

        return "§7Completed";
    }

    /*
     * ============================================================
     *                    REASON LIST
     * ============================================================
     */

    public static String reasonListHeader() {
        return "§2§lGrassGG Punishment Reasons";
    }

    public static String reasonTierHeader(String tier) {
        return "§a§l" + tier + ":";
    }

    public static String reasonEntry(String reason) {
        return "§7- §f" + reason;
    }

    /*
     * ============================================================
     *                     DATE FORMAT
     * ============================================================
     */

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                    .withZone(ZoneId.systemDefault());

    public static String formatDate(java.time.Instant instant) {
        return DATE_FORMAT.format(instant);
    }

    /*
     * ============================================================
     *                  PUNISHMENT REMOVAL
     * ============================================================
     */

    public static String punishmentRemoved(String player) {
        return PREFIX +
                "§aRemoved the active punishment from §f" +
                player +
                "§a.";
    }

    /*
     * ============================================================
     *                  REASON CHANGE
     * ============================================================
     */

    public static String reasonChanged(
            String player,
            String oldReason,
            String newReason
    ) {
        return PREFIX +
                "§aChanged §f" +
                player +
                "§a's punishment reason from §f" +
                oldReason +
                " §ato §f" +
                newReason +
                "§a.";
    }

    /*
     * ============================================================
     *                     ERRORS
     * ============================================================
     */

    public static String configurationError(String message) {
        return PREFIX + RED + "Configuration error: §f" + message;
    }

    public static String databaseError(String message) {
        return PREFIX + RED + "Database error: §f" + message;
    }

    public static String banScreen(OffenceRecord record) {

        String expiry = record.expiresAt() == null
                ? "Permanent"
                : formatDate(record.expiresAt());

        return String.join(
                "\n",
                "",
                "§8━━━━━━━━━━━━━━━━━━━━━━━━━━━━",
                "§2§lGRASS.GG",
                "§8━━━━━━━━━━━━━━━━━━━━━━━━━━━━",
                "",
                "§c§lYOU HAVE BEEN PUNISHED",
                "",
                "§cStaff Member » §f" + record.staffName(),
                "§cReason » §f" + record.reasonName(),
                "§cDuration » §fPermanent",
                "§cDate » §f" + formatDate(record.startedAt()),
                "",
                "§8━━━━━━━━━━━━━━━━━━━━━━━━━━━━",
                "§c§lAPPEAL",
                "§fJoin our Discord to appeal your punishment",
                "§b§nhttps://discord.gg/A97Dkcb7TU",
                "§8━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
        );
    }

    public static String muted(OffenceRecord record) {
        return PREFIX
                + "§cYou are currently muted."
                + " §7Reason: §f"
                + record.reasonName()
                + " §7| Expires: §f"
                + (
                record.expiresAt() == null
                        ? "Permanent"
                        : formatDate(record.expiresAt())
        );
    }

    public static String muteScreen(OffenceRecord record) {
        return PREFIX
                + "§cYou are currently muted."
                + " §7Reason: §f"
                + record.reasonName()
                + " §7| Expires: §f"
                + (
                record.expiresAt() == null
                        ? "Permanent"
                        : formatDate(record.expiresAt())
        );
    }

    public static final String OFFENCE_REMOVE_USAGE =
            RED + "Usage: /offence remove <player>";


    public static String punishmentRemoved(
            String player,
            String reason,
            int offenceNumber
    ) {

        return PREFIX
                + "§aRemoved active offence §f#"
                + offenceNumber
                + " §afrom §f"
                + player
                + "§a."
                + "\n"
                + PREFIX
                + "§7Reason: §f"
                + reason;
    }



    public static final String OFFENCE_NEW_USAGE =
            RED + "Usage: /offence new <player> <reason>";

    public static final String OFFENCE_CHECK_USAGE =
            RED + "Usage: /offence check <player>";

    public static final String OFFENCE_CHANGE_USAGE =
            RED + "Usage: /offence change <player> <new reason>";

    public static final String OFFENCE_HISTORY_HEADER =
            "§8§m━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n"
                    + "§a§lGRASS.GG §8» §fOffence History\n"
                    + "§8§m━━━━━━━━━━━━━━━━━━━━━━━━━━━━";

    public static final String OFFENCE_HISTORY_EMPTY =
            "§7No offences have been recorded.";

    public static final String OFFENCE_HISTORY_ENTRY =
            "§8#§f{offence} §8» §f{reason}\n"
                    + " §7Tier: §f{tier}\n"
                    + " §7Punishment: §f{punishment}\n"
                    + " §7Staff: §f{staff}\n"
                    + " §7Date: §f{date}\n"
                    + " §7Status: {status}\n";

    public static final String OFFENCE_LIST_HEADER =
            "§8§m━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n"
                    + "§a§lGRASS.GG §8» §fPunishment Reasons\n"
                    + "§8§m━━━━━━━━━━━━━━━━━━━━━━━━━━━━";

    public static final String OFFENCE_LIST_EMPTY =
            "§7No punishment reasons are configured.";

    public static final String OFFENCE_LIST_TIER =
            "§a§l{tier}";

    public static final String OFFENCE_LIST_REASON =
            "§8• §f{reason}";


    public static final String PUNISHMENT_REMOVED =
            PREFIX
                    + "§aRemoved the active punishment from §f{player}"
                    + "§a. (§7Offence #{offence}§a)";

    public static final String PUNISHMENT_CHANGED =
            PREFIX
                    + "§aChanged §f{player}§a's punishment.\n"
                    + PREFIX
                    + "§7Reason: §f{reason}\n"
                    + PREFIX
                    + "§7Tier: §f{tier}\n"
                    + PREFIX
                    + "§7Offence: §f#{offence}";




    public static final String OFFENCE_RESET_USAGE =
            PREFIX + RED + "Usage: /offence reset <player>";

    public static final String PUNISHMENT_HISTORY_RESET =
            PREFIX
                    + "§aCompletely reset the punishment history of §f{player}§a."
                    + "\n"
                    + PREFIX
                    + "§7Their next offence will now start at §f#1§7.";

    public static final String RESET_FAILED =
            PREFIX
                    + RED + "Failed to reset the punishment history of §f{player}§c.";




    public static final String SUS_USAGE =
            PREFIX + RED + "Usage: /sus [add|remove] ...";

    public static final String SUS_ADD_USAGE =
            PREFIX + RED + "Usage: /sus add <player> <reason>";

    public static final String SUS_REMOVE_USAGE =
            PREFIX + RED + "Usage: /sus remove <player> <reason>";

    public static final String SUS_PLAYER_ONLY =
            PREFIX + RED + "Only players can open the SUS menu.";

    public static final String SUS_REASON_TOO_LONG =
            PREFIX + RED + "That suspicion reason is too long.";

    public static String susAdded(
            me.sbpro.grassggstaff.sus.SusNote note
    ) {
        return PREFIX
                + WHITE + "Added SUS to" + CUSTOM
                + note.playerName()
                + WHITE + ":" + CUSTOM
                + note.reason();
    }

    public static String susRemoved(
            me.sbpro.grassggstaff.sus.SusNote note
    ) {
        return PREFIX
                + WHITE + "Removed SUS from" + CUSTOM
                + note.playerName()
                + WHITE + ":" + CUSTOM
                + note.reason();
    }

    public static String susAlreadyExists(
            String player,
            String reason
    ) {
        return PREFIX
                + WHITE + "That SUS reason already exists for" + CUSTOM
                + player
                + WHITE + ".";
    }

    public static String susReasonNotFound(
            String player,
            String reason
    ) {
        return PREFIX
                + RED + "That SUS reason does not exist for §f"
                + player
                + RED + ".";
    }
}