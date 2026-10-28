package me.sbpro.cyberhomes.listener;

import me.sbpro.cyberhomes.Messages;
import me.sbpro.cyberhomes.CyberHomes;
import me.sbpro.cyberhomes.manager.TeleportManager;
import me.sbpro.cyberhomes.menu.DeleteHomeMenu;
import me.sbpro.cyberhomes.menu.HomesMenu;
import me.sbpro.cyberhomes.menu.HomesMenuHolder;
import me.sbpro.cyberhomes.menu.RenameHomeMenu;
import me.sbpro.cyberhomes.model.Home;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class HomesListener implements Listener {

    private final CyberHomes plugin;

    public HomesListener(CyberHomes plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getView().getTopInventory().getHolder() instanceof HomesMenuHolder) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if (!(event.getView().getTopInventory().getHolder() instanceof HomesMenuHolder holder)) {
            return;
        }

        // Nothing in these menus can be moved, including shift-clicks from the player inventory.
        event.setCancelled(true);

        // Only clicks inside the menu itself are handled.
        if (event.getClickedInventory() != event.getView().getTopInventory()) {
            return;
        }

        switch (holder.getType()) {
            case VIEW -> handleViewClick(player, holder, event.getRawSlot());
            case HOMES -> handleHomesMenuClick(player, holder, event.getSlot(), event.getClick());
            case DELETE -> handleDeleteClick(player, holder, event.getCurrentItem());
        }
    }

    /** Staff viewing another player's homes. */
    private void handleViewClick(Player player, HomesMenuHolder holder, int slot) {
        if (slot < 10 || slot > 16) {
            return;
        }

        Home home = plugin.getHomesManager().getHome(holder.getTargetId(), slot - 9);
        if (home == null) {
            return;
        }

        player.closeInventory();
        player.sendMessage(Messages.teleportingTo(home.getName()));
        new TeleportManager(plugin, player, home.getLocation()).start();
    }

    private void handleHomesMenuClick(Player player, HomesMenuHolder holder, int slot, ClickType click) {
        Player target = plugin.getServer().getPlayer(holder.getTargetId());
        if (target == null) {
            return;
        }

        for (int i = 0; i < 7; i++) {
            int number = i + 1;

            if (slot == HomesMenu.BED_SLOTS[i]) {
                handleBedClick(player, target, number);
                return;
            }

            if (slot == HomesMenu.BUTTON_SLOTS[i]) {
                handleButtonClick(player, target, number, click);
                return;
            }
        }
    }

    private void handleDeleteClick(Player player, HomesMenuHolder holder, ItemStack item) {
        if (item == null) {
            return;
        }

        if (item.getType() == Material.GREEN_STAINED_GLASS_PANE) {
            plugin.getHomesManager().deleteHome(player.getUniqueId(), holder.getHomeNumber());

            player.sendMessage(Messages.HOME_DELETED);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);

            new HomesMenu(plugin, player, player).open();
            return;
        }

        if (item.getType() == Material.RED_STAINED_GLASS_PANE) {
            new HomesMenu(plugin, player, player).open();
        }
    }

    private void handleBedClick(Player viewer, Player target, int number) {
        if (getHomeLimit(target) < number) {
            viewer.sendMessage(Messages.NO_PERMISSION_SLOT);
            return;
        }

        Home home = plugin.getHomesManager().getHome(target.getUniqueId(), number);

        if (home == null) {
            viewer.sendMessage(Messages.SET_HOME_FIRST);
            return;
        }

        viewer.closeInventory();
        new TeleportManager(plugin, viewer, home.getLocation()).start();
    }

    private void handleButtonClick(Player viewer, Player target, int number, ClickType click) {
        // Staff viewing another player
        if (!viewer.equals(target)) {
            viewer.sendMessage(Messages.CANNOT_MODIFY_HOMES);
            return;
        }

        if (getHomeLimit(target) < number) {
            viewer.sendMessage(Messages.NO_PERMISSION_SLOT);
            return;
        }

        Home home = plugin.getHomesManager().getHome(target.getUniqueId(), number);

        if (home != null) {
            if (click.isRightClick()) {
                new RenameHomeMenu(plugin, viewer, target, number).open();
                return;
            }

            new DeleteHomeMenu(plugin, viewer, number).open();
            return;
        }

        Location location = viewer.getLocation();

        if (!isAllowedWorld(location.getWorld())) {
            viewer.sendMessage(Messages.CANNOT_SET_HOME_HERE);
            return;
        }

        plugin.getHomesManager().setHome(target.getUniqueId(), number, location);
        viewer.sendMessage(Messages.HOME_CREATED);

        new HomesMenu(plugin, viewer, target).open();
    }

    private boolean isAllowedWorld(World world) {
        return world != null
                && List.of("world", "world_nether", "world_the_end").contains(world.getName());
    }

    private int getHomeLimit(Player player) {
        for (int i = 7; i >= 1; i--) {
            if (player.hasPermission("cyber.homes." + i)) {
                return i;
            }
        }

        return 0;
    }

    /** Teleport movement cancellation. */
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!TeleportManager.isTeleporting(event.getPlayer())) {
            return;
        }

        if (event.getTo() == null) {
            return;
        }

        // Only X and Z movement cancels the teleport.
        // Y movement (jumping/falling) is allowed.
        if (event.getFrom().getBlockX() != event.getTo().getBlockX()
                || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {

            TeleportManager.cancel(event.getPlayer());
        }
    }
}
