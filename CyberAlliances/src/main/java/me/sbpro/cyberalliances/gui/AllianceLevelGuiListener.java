package me.sbpro.cyberalliances.gui;

import me.sbpro.cyberalliances.Messages;
import java.util.Optional;
import me.sbpro.cyberalliances.CyberAlliances;
import me.sbpro.cyberalliances.data.AllianceStorage;
import me.sbpro.cyberalliances.model.Alliance;
import me.sbpro.cyberalliances.service.AllianceXpService;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;

public final class AllianceLevelGuiListener implements Listener {
    private final CyberAlliances plugin;
    private final AllianceStorage allianceStorage;
    private final AllianceXpService xpService;

    public AllianceLevelGuiListener(CyberAlliances plugin, AllianceStorage allianceStorage, AllianceXpService xpService) {
        this.plugin = plugin;
        this.allianceStorage = allianceStorage;
        this.xpService = xpService;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof AllianceInfoGuiHolder
                || holder instanceof AllianceMembersGuiHolder
                || holder instanceof AllianceLevelGuiHolder
                || holder instanceof AllianceXpValuesGuiHolder)) {
            return;
        }

        event.setCancelled(true);
        if (event.getCurrentItem() == null) {
            return;
        }

        if (holder instanceof AllianceInfoGuiHolder infoHolder) {
            Optional<Alliance> allianceOptional = getAlliance(infoHolder.getAllianceName());
            if (allianceOptional.isEmpty()) {
                player.closeInventory();
                return;
            }

            Alliance alliance = allianceOptional.get();
            if (event.getSlot() == 40 && event.getCurrentItem().getType() == Material.BARRIER) {
                player.closeInventory();
                return;
            }
            if (event.getSlot() == 20 && event.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                AllianceInfoGui.openMembers(plugin, player, alliance);
                return;
            }
            if (event.getSlot() == 22 && event.getCurrentItem().getType() == Material.EXPERIENCE_BOTTLE) {
                AllianceLevelGui.openMain(plugin, player, alliance);
                return;
            }
            if (event.getSlot() == 24 && event.getCurrentItem().getType() == Material.CHEST) {
                AllianceLevelGui.openXpValues(plugin, player, alliance, 0);
            }
            return;
        }

        if (holder instanceof AllianceMembersGuiHolder membersHolder) {
            Optional<Alliance> allianceOptional = getAlliance(membersHolder.getAllianceName());
            if (allianceOptional.isEmpty()) {
                player.closeInventory();
                return;
            }
            if (event.getSlot() == 22 && event.getCurrentItem().getType() == Material.ARROW) {
                AllianceInfoGui.open(plugin, player, allianceOptional.get());
            }
            return;
        }

        if (holder instanceof AllianceLevelGuiHolder levelHolder) {
            Optional<Alliance> allianceOptional = getAlliance(levelHolder.getAllianceName());
            if (allianceOptional.isEmpty()) {
                player.closeInventory();
                return;
            }
            Alliance alliance = allianceOptional.get();

            if (event.getCurrentItem().getType() == Material.BARRIER) {
                player.closeInventory();
                return;
            }
            if (event.getSlot() == 22 && event.getCurrentItem().getType() == Material.ARROW) {
                AllianceInfoGui.open(plugin, player, alliance);
                return;
            }
            if (event.getSlot() == 26 && event.getCurrentItem().getType() == Material.CHEST) {
                AllianceLevelGui.openXpValues(plugin, player, alliance, 0);
            }
            return;
        }

        AllianceXpValuesGuiHolder xpHolder = (AllianceXpValuesGuiHolder) holder;
        Optional<Alliance> allianceOptional = getAlliance(xpHolder.getAllianceName());
        if (allianceOptional.isEmpty()) {
            player.closeInventory();
            return;
        }

        Alliance alliance = allianceOptional.get();
        if (event.getCurrentItem().getType() == Material.BARRIER) {
            AllianceLevelGui.openMain(plugin, player, alliance);
            return;
        }
        if (event.getCurrentItem().getType() != Material.ARROW) {
            return;
        }

        int maxPage = AllianceLevelGui.getMaxPage(plugin);
        if (event.getSlot() == 0) {
            if (xpHolder.getPage() <= 0) {
                player.sendMessage(Messages.NO_PREVIOUS_PAGE);
                return;
            }
            AllianceLevelGui.openXpValues(plugin, player, alliance, xpHolder.getPage() - 1);
            return;
        }
        if (event.getSlot() == 8) {
            if (xpHolder.getPage() >= maxPage) {
                player.sendMessage(Messages.NO_NEXT_PAGE);
                return;
            }
            AllianceLevelGui.openXpValues(plugin, player, alliance, xpHolder.getPage() + 1);
        }
    }

    private Optional<Alliance> getAlliance(String allianceName) {
        return allianceStorage.getAllianceByName(allianceName);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof AllianceInfoGuiHolder
                || holder instanceof AllianceMembersGuiHolder
                || holder instanceof AllianceLevelGuiHolder
                || holder instanceof AllianceXpValuesGuiHolder) {
            event.setCancelled(true);
        }
    }
}
