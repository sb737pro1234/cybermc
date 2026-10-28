package me.sbpro.cyberstaff.commands.chatlock;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.sbpro.cyberstaff.Messages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ChatLockListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncChatEvent event) {

        if (!ChatLockCommand.chatLocked) {
            return;
        }

        // Staff with the permission can still chat
        if (event.getPlayer().hasPermission("cyber.chatlock")) {
            return;
        }

        event.setCancelled(true);
        event.getPlayer().sendMessage(Messages.CHAT_CURRENTLY_LOCKED);
    }
}