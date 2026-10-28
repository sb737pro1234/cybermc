package me.sbpro.cyberannouncements.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.sbpro.cyberannouncements.Messages;
import me.sbpro.cyberannouncements.manager.ChatLockManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ChatLockListener implements Listener {

    private final ChatLockManager chatLockManager;

    public ChatLockListener(ChatLockManager chatLockManager) {
        this.chatLockManager = chatLockManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncChatEvent event) {

        if (!chatLockManager.isChatLocked()) {
            return;
        }

        event.setCancelled(true);

        event.getPlayer().sendMessage(Messages.CHAT_LOCKED);
    }
}