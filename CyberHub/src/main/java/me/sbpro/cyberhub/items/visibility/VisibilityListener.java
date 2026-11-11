package me.sbpro.cyberhub.items.visibility;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import me.sbpro.cyberhub.Messages;

public final class VisibilityListener implements Listener {

    private final Plugin plugin;
    private final VisibilityManager manager;

    public VisibilityListener(Plugin plugin, VisibilityManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    // Right-click the dye to toggle
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        ItemStack held = player.getInventory().getItemInMainHand();
        if (!VisibilityToggle.isToggle(plugin, held)) return;

        event.setCancelled(true);

        if (player.hasCooldown(held)) {
            player.sendMessage(Messages.visibilityCooldown(player.getCooldown(held)));
            return;
        }

        boolean nowHiding = !manager.isHiding(player);
        manager.setHiding(player, nowHiding);

        if (nowHiding) {
            for (Player other : Bukkit.getOnlinePlayers()) {
                if (!other.equals(player)) player.hidePlayer(plugin, other);
            }
            player.sendMessage(Messages.VISIBILITY_DISABLED);
        } else {
            for (Player other : Bukkit.getOnlinePlayers()) {
                if (!other.equals(player)) player.showPlayer(plugin, other);
            }
            player.sendMessage(Messages.VISIBILITY_ENABLED);
        }

        ItemStack swapped = VisibilityToggle.create(plugin, !nowHiding);
        player.getInventory().setItemInMainHand(swapped);
        player.setCooldown(swapped, VisibilityToggle.COOLDOWN_TICKS);
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.6F, nowHiding ? 0.8F : 1.2F);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player joined = event.getPlayer();

        // New joiners are hidden from everyone who has visibility disabled
        for (UUID uuid : manager.hiding()) {
            Player viewer = Bukkit.getPlayer(uuid);
            if (viewer != null && !viewer.equals(joined)) viewer.hidePlayer(plugin, joined);
        }

        // Saved setting: a joiner with visibility disabled can't see anyone already online
        if (manager.isHiding(joined)) {
            for (Player other : Bukkit.getOnlinePlayers()) {
                if (!other.equals(joined)) joined.hidePlayer(plugin, other);
            }
        }
    }
}
