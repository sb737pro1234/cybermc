package me.sbpro.cyberalliances.listener;

import me.sbpro.cyberalliances.Messages;
import io.papermc.paper.event.player.AsyncChatEvent;
import java.util.Optional;
import java.util.UUID;
import me.sbpro.cyberalliances.ChatToggleService;
import me.sbpro.cyberalliances.data.AllianceStorage;
import me.sbpro.cyberalliances.model.Alliance;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class AllianceChatListener
implements Listener {
    private final AllianceStorage allianceStorage;
    private final ChatToggleService chatToggleService;

    public AllianceChatListener(AllianceStorage allianceStorage, ChatToggleService chatToggleService) {
        this.allianceStorage = allianceStorage;
        this.chatToggleService = chatToggleService;
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        if (!this.chatToggleService.isAllianceChatEnabled(player.getUniqueId())) {
            return;
        }
        Optional<Alliance> allianceOptional = this.allianceStorage.getAllianceByPlayer(player.getUniqueId());
        if (allianceOptional.isEmpty()) {
            this.chatToggleService.disable(player.getUniqueId());
            return;
        }
        event.setCancelled(true);
        Alliance alliance = allianceOptional.get();
        String plainMessage = PlainTextComponentSerializer.plainText().serialize(event.message());
        Component formatted = Messages.allianceChat(alliance.getName(), player.getName(), plainMessage);
        for (UUID member : alliance.getMembers()) {
            Player target = Bukkit.getPlayer((UUID)member);
            if (target == null) continue;
            target.sendMessage(formatted);
        }
    }
}

