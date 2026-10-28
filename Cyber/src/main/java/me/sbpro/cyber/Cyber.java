package me.sbpro.cyber;

import me.sbpro.cyber.color.AnvilColorListener;
import me.sbpro.cyber.color.ColorsCommand;
import me.sbpro.cyber.commands.clear.ClearCommand;
import me.sbpro.cyber.commands.clear.ClearConfirmListener;
import me.sbpro.cyber.commands.feed.FeedCommand;
import me.sbpro.cyber.commands.feed.FeedListener;
import me.sbpro.cyber.commands.gamemode.*;
import me.sbpro.cyber.commands.gui.*;
import me.sbpro.cyber.commands.heal.HealCommand;
import me.sbpro.cyber.commands.heal.HealListener;
import me.sbpro.cyber.commands.kill.KillCommand;
import me.sbpro.cyber.commands.kill.KillListener;
import me.sbpro.cyber.commands.message.MessageCommand;
import me.sbpro.cyber.commands.message.MessageManager;
import me.sbpro.cyber.commands.message.MsgToggleCommand;
import me.sbpro.cyber.commands.message.ReplyCommand;
import me.sbpro.cyber.commands.misc.*;
import me.sbpro.cyber.commands.rules.RulesCommand;
import me.sbpro.cyber.commands.rules.RulesListener;
import me.sbpro.cyber.commands.say.SayCommand;
import me.sbpro.cyber.commands.trash.TrashCommand;
import me.sbpro.cyber.commands.trash.TrashListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.net.URI;

public final class Cyber extends JavaPlugin implements Listener {

    // /message <player> <message>
    // /reply <message>
    private final MessageManager messageManager = new MessageManager();

    public MessageManager getMessageManager() {
        return messageManager;
    }

    @Override
    public void onEnable() {
        getCommand("kill").setExecutor(new KillCommand());
        getServer().getPluginManager().registerEvents(new KillListener(), this);

        getCommand("heal").setExecutor(new HealCommand());
        getServer().getPluginManager().registerEvents(new HealListener(), this);

        getCommand("feed").setExecutor(new FeedCommand());
        getServer().getPluginManager().registerEvents(new FeedListener(), this);

        getCommand("clear").setExecutor(new ClearCommand());
        getServer().getPluginManager().registerEvents(new ClearConfirmListener(),this);

        getCommand("youtube").setExecutor(new YouTubeCommand());
        getCommand("discord").setExecutor(new DiscordCommand());
        getCommand("staff").setExecutor(new StaffCommand());
        getCommand("ping").setExecutor(new PingCommand());
        getCommand("nightvision").setExecutor(new NightVisionCommand());

        getCommand("msg").setExecutor(new MessageCommand(this));
        getCommand("r").setExecutor(new ReplyCommand(this));
        getCommand("msgtoggle").setExecutor(new MsgToggleCommand(this));
        Bukkit.getPluginManager().registerEvents(this, this);

        GameModeCommand gamemode = new GameModeCommand();
        GmcCommand gmc = new GmcCommand();
        GmsCommand gms = new GmsCommand();
        GmaCommand gma = new GmaCommand();
        GmspCommand gmsp = new GmspCommand();
        getCommand("gamemode").setExecutor(gamemode);
        getCommand("gamemode").setTabCompleter(gamemode);
        getCommand("gmc").setExecutor(gmc);
        getCommand("gmc").setTabCompleter(gmc);
        getCommand("gms").setExecutor(gms);
        getCommand("gms").setTabCompleter(gms);
        getCommand("gma").setExecutor(gma);
        getCommand("gma").setTabCompleter(gma);
        getCommand("gmsp").setExecutor(gmsp);
        getCommand("gmsp").setTabCompleter(gmsp);
        getServer().getPluginManager().registerEvents(new GamemodeListener(), this);

        getCommand("craft").setExecutor(new CraftCommand());
        getCommand("smithing").setExecutor(new SmithingCommand());
        getCommand("stonecutter").setExecutor(new StonecutterCommand());
        getCommand("loom").setExecutor(new LoomCommand());
        getCommand("cartography").setExecutor(new CartographyCommand());
        getCommand("grindstone").setExecutor(new GrindstoneCommand());
        getCommand("anvil").setExecutor(new AnvilCommand());

        getCommand("rules").setExecutor(new RulesCommand());
        getServer().getPluginManager().registerEvents(new RulesListener(),this);

        getCommand("trash").setExecutor(new TrashCommand());
        getServer().getPluginManager().registerEvents(new TrashListener(this), this);

        getCommand("say").setExecutor(new SayCommand());

        getCommand("colors").setExecutor(new ColorsCommand());
        getServer().getPluginManager().registerEvents(new AnvilColorListener(), this);

        // ServerLinks
        getServer().getServerLinks().addLink(
                net.kyori.adventure.text.Component.text("Discord")
                        .color(TextColor.fromHexString("#7289DA"))
                        .decorate(TextDecoration.BOLD),
                URI.create("https://discord.gg/A97Dkcb7TU"));

        getServer().getServerLinks().addLink(
                net.kyori.adventure.text.Component.text("Bug Reporting")
                        .color(TextColor.fromHexString("#FF746C"))
                        .decorate(TextDecoration.BOLD),
                URI.create("https://discord.gg/A97Dkcb7TU")
        );

        getServer().getServerLinks().addLink(
                Component.text("MrGrassYT")
                        .color(NamedTextColor.DARK_GREEN)
                        .decorate(TextDecoration.BOLD),
                URI.create("https://youtube.com/@MrGrassYT1")
        );

        getServer().getServerLinks().addLink(
                Component.text("Server Rules")
                        .color(TextColor.fromHexString("#2979FF"))
                        .decorate(TextDecoration.BOLD),
                URI.create("https://docs.google.com/document/d/1wZWo1C1_vQj826vAjiInfvQced7srQfRcTJrb9H-BoQ/edit?usp=sharing")
        );
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.hasPlayedBefore()) {

            if (player.hasPermission("cyber.announcejoinleave")) {
                event.joinMessage(Messages.joinMessage(event.getPlayer().displayName()));
            } else {
                event.joinMessage(null);
                return;
            }

        } else {
            event.joinMessage(Messages.firstJoinWelcome(player.displayName()));
        }

    }

    // Death
    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {

        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer != null) {
            event.deathMessage(
                    Messages.deathMessage(victim.getName(), killer.getName())
            );
        } else {
            event.deathMessage(null);
        }
    }

    // Player Quit
    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if(player.hasPermission("cyber.announcejoinleave")){
            event.quitMessage(Messages.quitMessage(event.getPlayer().displayName()));
        } else {
            event.quitMessage(null);
            return;
        }
    }

}
