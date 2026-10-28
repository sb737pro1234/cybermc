package me.sbpro.cyberalliances.gui;

import me.sbpro.cyberalliances.Messages;
import java.util.Optional;
import me.sbpro.cyberalliances.ChatToggleService;
import me.sbpro.cyberalliances.data.AllianceStorage;
import me.sbpro.cyberalliances.gui.DeleteGuiHolder;
import me.sbpro.cyberalliances.model.Alliance;
import me.sbpro.cyberalliances.service.AllianceBuffService;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;

public final class DeleteAllianceListener
implements Listener {
    private final AllianceStorage allianceStorage;
    private final ChatToggleService chatToggleService;
    private final AllianceBuffService buffService;

    public DeleteAllianceListener(AllianceStorage allianceStorage, ChatToggleService chatToggleService, AllianceBuffService buffService) {
        this.allianceStorage = allianceStorage;
        this.chatToggleService = chatToggleService;
        this.buffService = buffService;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity humanEntity = event.getWhoClicked();
        if (!(humanEntity instanceof Player)) {
            return;
        }
        Player player = (Player)humanEntity;
        InventoryHolder inventoryHolder = event.getInventory().getHolder();
        if (!(inventoryHolder instanceof DeleteGuiHolder)) {
            return;
        }
        DeleteGuiHolder holder = (DeleteGuiHolder)inventoryHolder;
        event.setCancelled(true);
        if (event.getClickedInventory() != event.getView().getTopInventory()) {
            return;
        }
        if (event.getCurrentItem() == null) {
            return;
        }
        if (event.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE) {
            player.closeInventory();
            player.sendMessage(Messages.DELETE_CANCELLED);
            return;
        }
        if (event.getCurrentItem().getType() != Material.GREEN_STAINED_GLASS_PANE) {
            return;
        }
        Optional<Alliance> allianceOptional = this.allianceStorage.getAllianceByPlayer(player.getUniqueId());
        if (allianceOptional.isEmpty()) {
            player.closeInventory();
            player.sendMessage(Messages.NO_ALLIANCE);
            return;
        }
        Alliance alliance = allianceOptional.get();
        if (!alliance.getOwner().equals(player.getUniqueId()) || !alliance.getName().equalsIgnoreCase(holder.getAllianceName())) {
            player.closeInventory();
            player.sendMessage(Messages.OWNER_ONLY_DELETE);
            return;
        }
        this.chatToggleService.disableAll(alliance.getMembers());
        this.buffService.clearAlliance(alliance);
        this.allianceStorage.deleteAlliance(alliance.getName());
        player.closeInventory();
        player.sendMessage(Messages.deleted(alliance.getName()));
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof DeleteGuiHolder) {
            event.setCancelled(true);
        }
    }
}

