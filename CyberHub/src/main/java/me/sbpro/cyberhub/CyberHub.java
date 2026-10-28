
package me.sbpro.cyberhub;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class CyberHub extends JavaPlugin implements Listener {

    private static final TextColor CYBER_BLUE = TextColor.fromHexString("#00AAFF");

    private static final TextColor GREY = NamedTextColor.GRAY;

    @Override
    public void onEnable() {
        getLogger().info("Cyber » Server Starting.");

        getServer().getPluginManager().registerEvents(this, this);

        startTabTask();
    }

    private void startTabTask() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {

            int online = Bukkit.getOnlinePlayers().size();

            for (Player player : Bukkit.getOnlinePlayers()) {

                Component header = Component.text()
                        .append(Component.text(
                                "                                                ",
                                NamedTextColor.WHITE
                        ).decorate(TextDecoration.STRIKETHROUGH))
                        .append(Component.newline())
                        .append(Component.text("CYBER MC", CYBER_BLUE)
                                .decorate(TextDecoration.BOLD))
                        .append(Component.newline())
                        .append(Component.text(
                                "ᴄʏʙᴇʀᴍᴄ.ᴍʏ.ᴘᴇʙʙʟᴇ.ʜᴏѕᴛ",
                                GREY
                        ))
                        .append(Component.newline())
                        .build();

                Component footer = Component.text()
                        .append(Component.newline())
                        .append(Component.text("Server: ", GREY))
                        .append(Component.text("Hub", CYBER_BLUE))
                        .append(Component.text(" | Online: ", GREY))
                        .append(Component.text(String.valueOf(online), CYBER_BLUE))
                        .append(Component.newline())
                        .append(Component.text("Account: ", GREY))
                        .append(Component.text(player.getName(), CYBER_BLUE))
                        .append(Component.text(" | Ping: ", GREY))
                        .append(Component.text(String.valueOf(player.getPing()), CYBER_BLUE))
                        .append(Component.newline())
                        .append(Component.text(
                                "                                                ",
                                NamedTextColor.WHITE
                        ).decorate(TextDecoration.STRIKETHROUGH))
                        .build();

                player.sendPlayerListHeaderAndFooter(header, footer);
            }

        }, 0L, 20L);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.getInventory().clear();

        Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(),
                "zmenu giveopenitem zmenu:server " + player.getName()
        );

        Bukkit.getScheduler().runTaskLater(this, () -> {
            ItemStack item = player.getInventory().getItem(0);

            if (item != null) {
                player.getInventory().setItem(4, item);
                player.getInventory().setItem(0, null);
            }
        }, 1L);

        event.joinMessage(null);

        World world = Bukkit.getWorld("NewHub");

        if (world != null) {
            player.teleport(
                    new Location(world, 0.5, 1, 0.5, 270F, 0F)
            );
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.quitMessage(null);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        event.message(null);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.deathMessage(null);
    }
}
