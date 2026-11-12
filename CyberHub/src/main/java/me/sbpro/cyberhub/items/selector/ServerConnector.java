package me.sbpro.cyberhub.items.selector;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Sends players to another server on the proxy using the BungeeCord plugin
 * messaging channel, which Velocity also supports
 * (bungee-plugin-message-channel = true in velocity.toml, the default).
 */
public final class ServerConnector {

    public static final String CHANNEL = "BungeeCord";

    private ServerConnector() {}

    public static void register(Plugin plugin) {
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, CHANNEL);
    }

    public static void unregister(Plugin plugin) {
        plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(plugin, CHANNEL);
    }

    public static void connect(Plugin plugin, Player player, String server) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (DataOutputStream out = new DataOutputStream(bytes)) {
            out.writeUTF("Connect");
            out.writeUTF(server);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to build connect message for " + player.getName() + ": " + e.getMessage());
            return;
        }

        player.sendPluginMessage(plugin, CHANNEL, bytes.toByteArray());
    }
}
