package me.sbpro.cyberkits.kit;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.sbpro.cyberkits.Kits;
import me.sbpro.cyberkits.Messages;
import me.sbpro.cyberkits.note.KitNoteManager;

/** Works out a kit's state for a player and handles claiming (which gives a Kit Note). */
public final class KitClaimer {

    /** What the kit icon in the /kits menu shows. */
    public enum State { LOCKED, COOLDOWN, CLAIMABLE }

    private final KitItemManager kitItems;
    private final CooldownManager cooldowns;
    private final KitNoteManager kitNotes;

    public KitClaimer(KitItemManager kitItems, CooldownManager cooldowns, KitNoteManager kitNotes) {
        this.kitItems = kitItems;
        this.cooldowns = cooldowns;
        this.kitNotes = kitNotes;
    }

    public State state(Player player, Kit kit) {
        if (!player.hasPermission(kit.permission())) return State.LOCKED;
        if (cooldowns.remaining(player.getUniqueId(), kit) > 0) return State.COOLDOWN;
        return State.CLAIMABLE;
    }

    /** Tries to claim the kit (gives a Kit Note), sending the player the result. Returns true if it was given. */
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

        if (kitItems.count(kit) == 0) {
            player.sendMessage(Messages.KIT_EMPTY);
            player.playSound(Messages.SOUND_ERROR);
            return false;
        }

        ItemStack note = kitNotes.create(player, kit);
        if (!fits(player.getInventory().getStorageContents(), List.of(note))) {
            player.sendMessage(Messages.NOTE_INVENTORY_FULL);
            player.playSound(Messages.SOUND_ERROR);
            return false;
        }

        player.getInventory().addItem(note);
        cooldowns.start(player.getUniqueId(), kit, Kits.KITS);

        player.sendMessage(Messages.kitClaimed(kit));
        player.playSound(Messages.SOUND_CLAIM);
        return true;
    }

    /** First join: gives a Starter Kit Note (dropped if it doesn't fit) and starts its cooldown. */
    public void giveFirstJoin(Player player, Kit kit) {
        if (!player.hasPermission(kit.permission())) return;

        if (kitItems.count(kit) > 0) {
            player.getInventory().addItem(kitNotes.create(player, kit))
                    .values()
                    .forEach(left -> player.getWorld().dropItemNaturally(player.getLocation(), left));
            player.sendMessage(Messages.firstJoinKit(kit));
        }

        cooldowns.start(player.getUniqueId(), kit, Kits.KITS);
    }

    /** Whether every item fits into a copy of the given storage contents (36 main inventory slots). */
    public static boolean fits(ItemStack[] storage, List<ItemStack> items) {
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
