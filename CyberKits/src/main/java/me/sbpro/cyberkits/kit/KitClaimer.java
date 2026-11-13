package me.sbpro.cyberkits.kit;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.sbpro.cyberkits.Kits;
import me.sbpro.cyberkits.Messages;

/** Works out a kit's state for a player and handles claiming. */
public final class KitClaimer {

    /** What the kit icon in the /kits menu shows. */
    public enum State { LOCKED, COOLDOWN, CLAIMABLE }

    private final KitItemManager kitItems;
    private final CooldownManager cooldowns;

    public KitClaimer(KitItemManager kitItems, CooldownManager cooldowns) {
        this.kitItems = kitItems;
        this.cooldowns = cooldowns;
    }

    public State state(Player player, Kit kit) {
        if (!player.hasPermission(kit.permission())) return State.LOCKED;
        if (cooldowns.remaining(player.getUniqueId(), kit) > 0) return State.COOLDOWN;
        return State.CLAIMABLE;
    }

    /** Tries to claim the kit, sending the player the result. Returns true if it was given. */
    public boolean claim(Player player, Kit kit) {
        switch (state(player, kit)) {
            case LOCKED -> {
                player.sendMessage(Messages.kitLocked(kit));
                player.playSound(Messages.SOUND_ERROR);
                return false;
            }
            case COOLDOWN -> {
                player.sendMessage(Messages.kitOnCooldown(kit, cooldowns.remaining(player.getUniqueId(), kit)));
                player.playSound(Messages.SOUND_ERROR);
                return false;
            }
            case CLAIMABLE -> {}
        }

        List<ItemStack> items = kitItems.items(kit);
        if (items.isEmpty()) {
            player.sendMessage(Messages.KIT_EMPTY);
            player.playSound(Messages.SOUND_ERROR);
            return false;
        }

        if (!fits(player, items)) {
            player.sendMessage(Messages.INVENTORY_FULL);
            player.playSound(Messages.SOUND_ERROR);
            return false;
        }

        player.getInventory().addItem(items.toArray(ItemStack[]::new));
        cooldowns.start(player.getUniqueId(), kit, Kits.KITS);

        player.sendMessage(Messages.kitClaimed(kit));
        player.playSound(Messages.SOUND_CLAIM);
        return true;
    }

    /** First join: gives the Starter kit (anything that doesn't fit is dropped) and starts its cooldown. */
    public void giveFirstJoin(Player player, Kit kit) {
        if (!player.hasPermission(kit.permission())) return;

        List<ItemStack> items = kitItems.items(kit);
        if (!items.isEmpty()) {
            player.getInventory().addItem(items.toArray(ItemStack[]::new))
                    .values()
                    .forEach(left -> player.getWorld().dropItemNaturally(player.getLocation(), left));
            player.sendMessage(Messages.firstJoinKit(kit));
        }

        cooldowns.start(player.getUniqueId(), kit, Kits.KITS);
    }

    /** Whether every item fits in the player's main inventory, tested on a copy. */
    private static boolean fits(Player player, List<ItemStack> items) {
        ItemStack[] storage = player.getInventory().getStorageContents();
        Inventory test = Bukkit.createInventory(null, 36);

        for (int i = 0; i < storage.length && i < 36; i++) {
            if (storage[i] != null) test.setItem(i, storage[i].clone());
        }

        for (ItemStack item : items) {
            if (!test.addItem(item.clone()).isEmpty()) return false;
        }
        return true;
    }
}
