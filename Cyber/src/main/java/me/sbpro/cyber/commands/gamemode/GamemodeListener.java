package me.sbpro.cyber.commands.gamemode;

import me.sbpro.cyber.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GamemodeListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if (!(event.getView().getTopInventory().getHolder() instanceof GamemodeConfirmHolder)) {
            return;
        }

        event.setCancelled(true);

        if (event.getCurrentItem() == null) {
            return;
        }

        PendingGamemodeChange pending =
                GameModeCommand.getPending().get(player.getUniqueId());

        if (pending == null) {
            player.closeInventory();
            return;
        }

        switch (event.getCurrentItem().getType()) {

            case GREEN_STAINED_GLASS_PANE -> {

                Player target = pending.getTarget();

                if (target == null || !target.isOnline()) {
                    player.sendMessage(Messages.ADMIN_PLAYER_NO_LONGER_ONLINE);
                    player.closeInventory();
                    GameModeCommand.getPending().remove(player.getUniqueId());
                    return;
                }

                target.setGameMode(pending.getGamemode().getGameMode());

                player.sendMessage(
                        Messages.gamemodeChangedOther(target.getName(), pending.getGamemode().getDisplayName())
                );

                target.sendMessage(
                        Messages.gamemodeChangedByStaff(pending.getGamemode().getDisplayName())
                );

                GameModeCommand.getPending().remove(player.getUniqueId());
                player.closeInventory();
            }

            case RED_STAINED_GLASS_PANE -> {
                GameModeCommand.getPending().remove(player.getUniqueId());
                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        GameModeCommand.getPending().remove(event.getPlayer().getUniqueId());
    }

}