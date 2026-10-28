package me.sbpro.cyberstaff.offend;

import me.sbpro.cyberstaff.CyberStaff;
import me.sbpro.cyberstaff.Messages;
import me.sbpro.cyberstaff.punishment.OffenceRecord;
import me.sbpro.cyberstaff.punishment.PunishmentType;
import me.sbpro.cyberstaff.util.PlayerLookup;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public final class OffenceCheckCommand {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern(
                    "dd/MM/yyyy HH:mm"
            ).withZone(
                    ZoneId.systemDefault()
            );

    private final CyberStaff plugin;

    public OffenceCheckCommand(CyberStaff plugin) {
        this.plugin = plugin;
    }

    public boolean execute(
            CommandSender sender,
            String[] args
    ) {

        if (!sender.hasPermission(
                "cyber.staff.check"
        )) {

            sender.sendMessage(
                    Messages.NO_PERMISSION
            );

            return true;
        }

        if (args.length != 2) {

            sender.sendMessage(
                    Messages.OFFENCE_CHECK_USAGE
            );

            return true;
        }

        String playerName = args[1];

        PlayerLookup.find(
                plugin,
                playerName
        ).whenComplete((result, throwable) -> {

            Bukkit.getScheduler().runTask(
                    plugin,
                    () -> {

                        if (throwable != null) {

                            sender.sendMessage(
                                    Messages.DATABASE_ERROR
                            );

                            return;
                        }

                        if (!result.found()) {

                            sender.sendMessage(
                                    Messages.UNKNOWN_PLAYER
                            );

                            return;
                        }

                        plugin.getPunishmentManager()
                                .getHistory(result.uuid())
                                .whenComplete((history,
                                               historyThrowable) -> {

                                    Bukkit.getScheduler().runTask(
                                            plugin,
                                            () -> {

                                                if (historyThrowable != null) {

                                                    sender.sendMessage(
                                                            Messages.DATABASE_ERROR
                                                    );

                                                    return;
                                                }

                                                sender.sendMessage(
                                                        Messages.OFFENCE_HISTORY_HEADER
                                                );

                                                if (history.isEmpty()) {

                                                    sender.sendMessage(
                                                            Messages.OFFENCE_HISTORY_EMPTY
                                                    );

                                                    return;
                                                }

                                                for (
                                                        OffenceRecord offence :
                                                        history
                                                ) {

                                                    sendOffence(
                                                            sender,
                                                            offence
                                                    );
                                                }
                                            }
                                    );
                                });
                    }
            );
        });

        return true;
    }

    private void sendOffence(
            CommandSender sender,
            OffenceRecord offence
    ) {

        sender.sendMessage(
                Messages.offenceHistoryEntry( offence.offenceNumber() , offence.reasonName(), offence.tier(), formatPunishment(offence), offence.staffName(), DATE_FORMAT.format( offence.startedAt() ), getStatus(offence))
        );
    }

    private String formatPunishment(
            OffenceRecord offence
    ) {

        if (offence.punishmentType() ==
                PunishmentType.WARNING) {

            return "Warning";
        }

        if (offence.duration() == null ||
                offence.duration().isBlank()) {

            return offence.punishmentType()
                    .getDisplayName();
        }

        return offence.duration()
                + " "
                + offence.punishmentType()
                .getDisplayName();
    }

    private Component getStatus(
            OffenceRecord offence
    ) {

        if (offence.removed()) {
            return Messages.STATUS_REMOVED;
        }

        if (offence.replaced()) {
            return Messages.STATUS_REPLACED;
        }

        if (offence.expiresAt() != null &&
                offence.expiresAt().isBefore(
                        Instant.now()
                )) {

            return Messages.STATUS_EXPIRED;
        }

        if (offence.active()) {
            return Messages.STATUS_ACTIVE;
        }

        return Messages.STATUS_COMPLETED;
    }

    public List<String> tabComplete(
            CommandSender sender,
            String[] args
    ) {

        if (args.length != 2) {
            return List.of();
        }

        String input =
                args[1].toLowerCase(Locale.ROOT);

        return Bukkit.getOnlinePlayers()
                .stream()
                .map(Player::getName)
                .filter(name ->
                        name.toLowerCase(Locale.ROOT)
                                .startsWith(input)
                )
                .sorted()
                .toList();
    }
}