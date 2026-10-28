package me.sbpro.cyberstaff.offend;

import me.sbpro.cyberstaff.CyberStaff;
import me.sbpro.cyberstaff.Messages;
import me.sbpro.cyberstaff.reason.PunishmentReason;
import org.bukkit.command.CommandSender;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class OffenceListCommand {

    private final CyberStaff plugin;

    public OffenceListCommand(CyberStaff plugin) {
        this.plugin = plugin;
    }

    public boolean execute(
            CommandSender sender,
            String[] args
    ) {

        sender.sendMessage(
                Messages.OFFENCE_LIST_HEADER
        );

        Map<String, List<PunishmentReason>> byTier =
                new LinkedHashMap<>();

        for (PunishmentReason reason :
                plugin.getReasonManager().all()) {

            byTier.computeIfAbsent(
                    reason.tier(),
                    ignored -> new java.util.ArrayList<>()
            ).add(reason);
        }

        if (byTier.isEmpty()) {

            sender.sendMessage(
                    Messages.OFFENCE_LIST_EMPTY
            );

            return true;
        }

        for (Map.Entry<String, List<PunishmentReason>> entry :
                byTier.entrySet()) {

            String tierId = entry.getKey();

            String tierName =
                    plugin.getPunishmentConfig()
                            .getTier(tierId)
                            .map(tier -> tier.displayName())
                            .orElse(tierId);

            sender.sendMessage(
                    Messages.offenceListTier(tierName)
            );

            for (PunishmentReason reason :
                    entry.getValue()) {

                sender.sendMessage(
                        Messages.offenceListReason(reason.displayName())
                );
            }

            sender.sendMessage("");
        }

        return true;
    }

    public List<String> tabComplete(
            CommandSender sender,
            String[] args
    ) {
        return List.of();
    }
}