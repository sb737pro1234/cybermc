package me.sbpro.cyberstaff.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.sbpro.cyberstaff.CyberStaff;
import me.sbpro.cyberstaff.Messages;
import me.sbpro.cyberstaff.punishment.OffenceRecord;
import me.sbpro.cyberstaff.punishment.PunishmentEnforcementManager;
import me.sbpro.cyberstaff.punishment.PunishmentType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;

public final class PunishmentChatListener implements Listener {

    private final CyberStaff plugin;
    private final PunishmentEnforcementManager enforcement;

    public PunishmentChatListener(
            CyberStaff plugin,
            PunishmentEnforcementManager enforcement
    ) {
        this.plugin = plugin;
        this.enforcement = enforcement;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncChatEvent event) {

        Player player = event.getPlayer();

        OffenceRecord punishment =
                enforcement.getActive(player.getUniqueId());

        if (punishment == null) {
            return;
        }

        if (punishment.punishmentType() != PunishmentType.MUTE) {
            return;
        }

        event.setCancelled(true);

        player.sendMessage(
                Messages.muted(punishment)
        );
    }
}