package me.sbpro.cybersurvival.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class RebootCommand implements CommandExecutor {

    private static final String PERMISSION = "cybermc.restart";

    private static final Sound WARNING_SOUND = Sound.BLOCK_NOTE_BLOCK_BELL;
    private static final SoundCategory WARNING_SOUND_CATEGORY = SoundCategory.BLOCKS;
    private static final float WARNING_SOUND_VOLUME = 100f;
    private static final float WARNING_SOUND_PITCH = 1f;

    /** A countdown step: ticks to wait AFTER the previous step, and the number shown in chat. */
    private record Step(long delayTicks, int seconds) {}

    private static final Step[] STEPS = {
            new Step(0, 60),
            new Step(600, 30),
            new Step(300, 15),
            new Step(100, 10),
            new Step(100, 5),
            new Step(40, 3),
            new Step(20, 2),
            new Step(20, 1)
    };

    /** Ticks to wait after the last chat message before restarting. */
    private static final long RESTART_DELAY_TICKS = 20;

    private final JavaPlugin plugin;
    private boolean rebooting = false;
    private BukkitTask actionBarTask = null;
    private long ticksRemaining = 0;

    public RebootCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission(PERMISSION)) {
            sender.sendMessage(Component.text("You do not have permission to do that.", NamedTextColor.RED));
            return true;
        }

        if (rebooting) {
            sender.sendMessage(Component.text("A reboot is already in progress.", NamedTextColor.RED));
            return true;
        }

        rebooting = true;
        scheduleCountdown();
        return true;
    }

    private void scheduleCountdown() {
        long total = RESTART_DELAY_TICKS;
        for (Step step : STEPS) {
            total += step.delayTicks();
        }

        // Live action bar: ticks every second, showing the exact seconds remaining.
        ticksRemaining = total;
        actionBarTask = Bukkit.getScheduler().runTaskTimer(plugin, this::tickActionBar, 0L, 20L);

        // Chat message + sound only at the configured intervals.
        long elapsed = 0;
        for (Step step : STEPS) {
            elapsed += step.delayTicks();
            final int seconds = step.seconds();
            Bukkit.getScheduler().runTaskLater(plugin, () -> announce(seconds), elapsed);
        }

        elapsed += RESTART_DELAY_TICKS;
        Bukkit.getScheduler().runTaskLater(plugin, this::restart, elapsed);
    }

    private void tickActionBar() {
        int seconds = (int) (ticksRemaining / 20);
        ticksRemaining -= 20;

        if (seconds <= 0) {
            return;
        }

        Component message = buildMessage(seconds);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendActionBar(message);
        }
    }

    private void announce(int seconds) {
        Bukkit.broadcast(buildMessage(seconds));

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), WARNING_SOUND,
                    WARNING_SOUND_CATEGORY, WARNING_SOUND_VOLUME, WARNING_SOUND_PITCH);
        }
    }

    private Component buildMessage(int seconds) {
        return Component.text()
                .append(Component.text("⚠ Server rebooting in ", NamedTextColor.DARK_RED, TextDecoration.BOLD))
                .append(Component.text(String.valueOf(seconds), NamedTextColor.RED))
                .append(Component.text(seconds == 1 ? " second ⚠" : " seconds ⚠",
                        NamedTextColor.DARK_RED, TextDecoration.BOLD))
                .build();
    }

    private void restart() {
        if (actionBarTask != null) {
            actionBarTask.cancel();
            actionBarTask = null;
        }
        rebooting = false;

        // Requires a restart script configured via "restart-script" in spigot.yml,
        // otherwise this behaves like a normal shutdown.
        Bukkit.restart();
    }
}