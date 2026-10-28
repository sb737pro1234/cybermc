package me.sbpro.cybersurvival.commands.playtop;

import me.sbpro.cybersurvival.Messages;
import me.sbpro.cybersurvival.CyberSurvival;
import me.sbpro.cybersurvival.util.PlaytimeFormatter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class PlayTopMenu {

    private static final List<PlaytimeEntry> cachedTopPlayers = new ArrayList<>();
    private static final Map<UUID, Integer> cachedPositions = new ConcurrentHashMap<>();
    private static final Map<UUID, Long> cachedPlaytimes = new ConcurrentHashMap<>();

    private PlayTopMenu() {
    }

    public static void open(CyberSurvival plugin, Player player) {
        PlayTopMenuHolder holder = new PlayTopMenuHolder();

        Inventory inventory = Bukkit.createInventory(
                holder,
                54,
                Messages.PLAYTOP_TITLE
        );

        holder.setInventory(inventory);

        if (cachedTopPlayers.isEmpty()) {
            refresh(plugin);
        }

        synchronized (cachedTopPlayers) {
            for (int slot = 0; slot < cachedTopPlayers.size() && slot < 45; slot++) {
                PlaytimeEntry entry = cachedTopPlayers.get(slot);
                int position = slot + 1;

                inventory.setItem(
                        slot,
                        createPlayerPlaytimeHead(
                                entry.uuid(),
                                entry.playerName(),
                                entry.playtimeTicks(),
                                position
                        )
                );
            }
        }

        long playerPlaytime = cachedPlaytimes.getOrDefault(player.getUniqueId(), getPlaytimeTicks(player));
        int playerPosition = cachedPositions.getOrDefault(player.getUniqueId(), -1);

        inventory.setItem(
                49,
                createPlayerPlaytimeHead(
                        player,
                        playerPlaytime,
                        playerPosition
                )
        );

        player.openInventory(inventory);
    }

    public static void refresh(CyberSurvival plugin) {
        List<PlaytimeEntry> playtimes = new ArrayList<>();

        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            if (!player.hasPlayedBefore() && !player.isOnline()) {
                continue;
            }

            long playtimeTicks = getPlaytimeTicks(player);
            String playerName = player.getName() != null ? player.getName() : "Unknown";

            playtimes.add(new PlaytimeEntry(
                    player.getUniqueId(),
                    playerName,
                    playtimeTicks
            ));
        }

        playtimes.sort(
                Comparator.comparingLong(PlaytimeEntry::playtimeTicks)
                        .reversed()
        );

        Map<UUID, Integer> positions = new ConcurrentHashMap<>();
        Map<UUID, Long> playerPlaytimes = new ConcurrentHashMap<>();

        for (int i = 0; i < playtimes.size(); i++) {
            PlaytimeEntry entry = playtimes.get(i);
            positions.put(entry.uuid(), i + 1);
            playerPlaytimes.put(entry.uuid(), entry.playtimeTicks());
        }

        synchronized (cachedTopPlayers) {
            cachedTopPlayers.clear();
            cachedTopPlayers.addAll(
                    playtimes.subList(0, Math.min(45, playtimes.size()))
            );
        }

        cachedPositions.clear();
        cachedPositions.putAll(positions);

        cachedPlaytimes.clear();
        cachedPlaytimes.putAll(playerPlaytimes);
    }

    /**
     * Total time played, in ticks. Despite its name, PLAY_ONE_MINUTE is the
     * statistic that tracks total ticks played.
     */
    private static long getPlaytimeTicks(OfflinePlayer player) {
        return player.getStatistic(Statistic.PLAY_ONE_MINUTE);
    }

    private static ItemStack createPlayerPlaytimeHead(
            UUID uuid,
            String playerName,
            long playtimeTicks,
            int position
    ) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        meta.setOwningPlayer(player);

        meta.displayName(Messages.topPlayerName(position, playerName));

        List<Component> lore = new ArrayList<>();
        lore.add(Messages.blankLore());
        lore.add(Messages.playtimeLine(PlaytimeFormatter.format(playtimeTicks)));

        meta.lore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private static ItemStack createPlayerPlaytimeHead(
            Player player,
            long playtimeTicks,
            int position
    ) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        meta.setOwningPlayer(player);
        meta.displayName(Messages.YOUR_PLAYTIME_NAME);

        List<Component> lore = new ArrayList<>();
        lore.add(Messages.blankLore());
        lore.add(Messages.playtimeLine(PlaytimeFormatter.format(playtimeTicks)));
        lore.add(Messages.positionLine(position));

        meta.lore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private record PlaytimeEntry(
            UUID uuid,
            String playerName,
            long playtimeTicks
    ) {
    }
}
