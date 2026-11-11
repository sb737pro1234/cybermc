
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
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.kyori.adventure.bossbar.BossBar;

import me.sbpro.cyberhub.items.HubItemLockListener;
import me.sbpro.cyberhub.items.HubItems;
import me.sbpro.cyberhub.items.enderbow.EnderBow;
import me.sbpro.cyberhub.items.enderbow.EnderBowListener;
import me.sbpro.cyberhub.items.visibility.VisibilityListener;
import me.sbpro.cyberhub.items.visibility.VisibilityManager;
import me.sbpro.cyberhub.items.visibility.VisibilityToggle;
import me.sbpro.cyberhub.protection.HubProtectionListener;
import me.sbpro.cyberhub.selector.ServerConnector;
import me.sbpro.cyberhub.selector.ServerSelector;
import me.sbpro.cyberhub.selector.ServerSelectorListener;

public final class CyberHub extends JavaPlugin implements Listener {

    private static final TextColor CYBER_BLUE = TextColor.fromHexString("#00AAFF");

    private static final TextColor GREY = NamedTextColor.GRAY;

    private static final String HUB_WORLD = "NewHub";

    private VisibilityManager visibilityManager;

    /** Permanent bossbar shown to everyone. */
    private final BossBar bossBar = BossBar.bossBar(Messages.BOSSBAR, 1F, BossBar.Color.BLUE, BossBar.Overlay.PROGRESS);

    /** Hub spawn point, falling back to the given world's spawn if NewHub isn't loaded. */
    public static Location hubSpawn(World fallback) {
        World world = Bukkit.getWorld(HUB_WORLD);
        return world != null
                ? new Location(world, 0.5, 1, 0.5, 270F, 0F)
                : fallback.getSpawnLocation();
    }

    @Override
    public void onEnable() {
        getLogger().info("Cyber » Server Starting.");

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new EnderBowListener(this), this);
        visibilityManager = new VisibilityManager(this);
        getServer().getPluginManager().registerEvents(new VisibilityListener(this, visibilityManager), this);
        getServer().getPluginManager().registerEvents(new HubProtectionListener(this), this);
        getServer().getPluginManager().registerEvents(new ServerSelectorListener(this), this);
        getServer().getPluginManager().registerEvents(new HubItemLockListener(this), this);

        ServerConnector.register(this);

        // Covers /reload: anyone already online gets the bossbar
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showBossBar(bossBar);
        }

        startTabTask();
    }

    @Override
    public void onDisable() {
        ServerConnector.unregister(this);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.hideBossBar(bossBar);
        }
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
        giveHubItems(player);
        player.showBossBar(bossBar);

        event.joinMessage(null);

        if (Bukkit.getWorld(HUB_WORLD) != null) {
            player.teleport(hubSpawn(player.getWorld()));
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

        // Hub items are given back on respawn, so don't drop copies on the ground
        event.getDrops().removeIf(item -> HubItems.isHubItem(this, item));
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        giveHubItems(event.getPlayer());
    }

    /** Server selector in slot 1, visibility toggle in slot 8, Ender Bow in slot 9. */
    private void giveHubItems(Player player) {
        boolean visible = !visibilityManager.isHiding(player);

        player.getInventory().setItem(ServerSelector.SLOT, ServerSelector.create(this));
        player.getInventory().setItem(VisibilityToggle.SLOT, VisibilityToggle.create(this, visible));
        player.getInventory().setItem(EnderBow.SLOT, EnderBow.create(this));
    }
}
