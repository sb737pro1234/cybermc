package me.sbpro.cyberannouncements.manager;

import me.sbpro.cyberannouncements.Messages;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.atomic.AtomicLong;

public class ChatLockManager {

    private static final long LOCK_MILLIS = 5000;
    private static final long LOCK_TICKS = LOCK_MILLIS / 50;

    private final Plugin plugin;
    private final AtomicLong lockedUntil = new AtomicLong(0);
    private BukkitTask unlockTask;

    public ChatLockManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public synchronized void lockChat() {
        lockedUntil.set(System.currentTimeMillis() + LOCK_MILLIS);

        // If chat is re-locked before the previous lock ended, restart the timer
        // so only one "unlocked" message is sent, after the final lock expires.
        if (unlockTask != null) {
            unlockTask.cancel();
        }

        unlockTask = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            synchronized (ChatLockManager.this) {
                unlockTask = null;
            }
            Bukkit.broadcast(Messages.CHAT_UNLOCKED);
        }, LOCK_TICKS);
    }

    public boolean isChatLocked() {
        return System.currentTimeMillis() < lockedUntil.get();
    }

    /** Cancels any pending unlock message (used when the plugin is disabled). */
    public synchronized void shutdown() {
        if (unlockTask != null) {
            unlockTask.cancel();
            unlockTask = null;
        }
    }
}