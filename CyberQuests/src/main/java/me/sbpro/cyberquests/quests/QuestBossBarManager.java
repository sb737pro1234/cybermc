package me.sbpro.cyberquests.quests;

import me.sbpro.cyberquests.CyberQuests;
import me.sbpro.cyberquests.Messages;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class QuestBossBarManager {
    private final CyberQuests plugin;
    private final Map<UUID, BossBar> bars = new HashMap<>();
    private final Map<UUID, Integer> hideTasks = new HashMap<>();

    public QuestBossBarManager(CyberQuests plugin) {
        this.plugin = plugin;
    }

    public void showProgress(Player player, QuestDefinition quest, int progress) {
        UUID uuid = player.getUniqueId();
        // The bossbar text is the quest itself (for example, "Bake 32 bread").
        // The filled portion of the bar represents the player's actual progress.
        float fill = quest.getRequiredAmount() <= 0
                ? 1.0f
                : Math.min(1.0f, Math.max(0.0f, (float) progress / quest.getRequiredAmount()));
        Component title = Messages.bossBarTitle(quest.getTitle(), progress, quest.getRequiredAmount());

        BossBar bar = bars.computeIfAbsent(uuid, id -> BossBar.bossBar(title, fill, Messages.BOSSBAR_COLOR, Messages.BOSSBAR_OVERLAY));
        bar.name(title);
        bar.color(Messages.BOSSBAR_COLOR);
        bar.overlay(Messages.BOSSBAR_OVERLAY);
        bar.progress(fill);

        player.showBossBar(bar);

        Integer oldTask = hideTasks.remove(uuid);
        if (oldTask != null) Bukkit.getScheduler().cancelTask(oldTask);

        int task = Bukkit.getScheduler().runTaskLater(plugin, () -> hide(player), Messages.BOSSBAR_DISPLAY_TICKS).getTaskId();
        hideTasks.put(uuid, task);
    }

    public void hide(Player player) {
        UUID uuid = player.getUniqueId();
        Integer task = hideTasks.remove(uuid);
        if (task != null) Bukkit.getScheduler().cancelTask(task);

        BossBar bar = bars.remove(uuid);
        if (bar != null) player.hideBossBar(bar);
    }

    public void removeAll() {
        for (Map.Entry<UUID, BossBar> entry : bars.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null) player.hideBossBar(entry.getValue());
        }
        bars.clear();
        for (Integer task : hideTasks.values()) Bukkit.getScheduler().cancelTask(task);
        hideTasks.clear();
    }
}
