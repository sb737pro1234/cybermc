package me.sbpro.cyberchat;

import me.sbpro.cyberchat.chatcolor.ChatColorCommand;
import me.sbpro.cyberchat.chatcolor.ChatColorListener;
import me.sbpro.cyberchat.chatcolor.ChatColorManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CyberChat extends JavaPlugin {

    private ChatColorManager chatColorManager;

    @Override
    public void onEnable() {

        chatColorManager =
                new ChatColorManager(this);

        getCommand("chatcolor").setExecutor(
                new ChatColorCommand(
                        chatColorManager
                )
        );

        getServer()
                .getPluginManager()
                .registerEvents(
                        new ChatColorListener(
                                chatColorManager
                        ),
                        this
                );

        getLogger().info(
                "CyberChat enabled!"
        );
    }

    public ChatColorManager getChatColorManager() {
        return chatColorManager;
    }
}