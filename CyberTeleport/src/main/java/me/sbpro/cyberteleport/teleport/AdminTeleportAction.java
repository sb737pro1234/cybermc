package me.sbpro.cyberteleport.teleport;

import me.sbpro.cyberteleport.gui.ConfirmationAction;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class AdminTeleportAction implements ConfirmationAction {

    private final TeleportManager teleportManager;
    private final Player teleportedPlayer;
    private final Location destination;
    private final Player messagePlayer;

    public AdminTeleportAction(
            TeleportManager teleportManager,
            Player teleportedPlayer,
            Location destination,
            Player messagePlayer
    ) {
        this.teleportManager = teleportManager;
        this.teleportedPlayer = teleportedPlayer;
        this.destination = destination;
        this.messagePlayer = messagePlayer;
    }

    @Override
    public void confirm() {
        teleportManager.instantTeleport(
                teleportedPlayer,
                destination,
                messagePlayer
        );
    }

    @Override
    public void cancel() {
    }
}