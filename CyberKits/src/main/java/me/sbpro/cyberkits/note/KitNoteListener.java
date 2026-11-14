package me.sbpro.cyberkits.note;

import java.util.List;
import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import me.sbpro.cyberkits.CyberKits;
import me.sbpro.cyberkits.Kits;
import me.sbpro.cyberkits.Messages;
import me.sbpro.cyberkits.kit.Kit;
import me.sbpro.cyberkits.kit.KitClaimer;

/** Right-click (or left-click) a Kit Note to receive the kit's items. Anyone holding the note can open it. */
public final class KitNoteListener implements Listener {

    private final CyberKits plugin;

    public KitNoteListener(CyberKits plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action == Action.PHYSICAL) return;

        EquipmentSlot hand = event.getHand();
        if (hand == null) return;

        Player player = event.getPlayer();
        ItemStack item = hand == EquipmentSlot.OFF_HAND
                ? player.getInventory().getItemInOffHand()
                : player.getInventory().getItemInMainHand();

        KitNoteManager notes = plugin.kitNotes();
        Optional<KitNoteData> optionalData = notes.read(item);
        if (optionalData.isEmpty()) return;

        event.setCancelled(true);
        KitNoteData note = optionalData.get();

        if (notes.isRedeemed(note.noteId())) {
            player.sendMessage(Messages.NOTE_ALREADY_USED);
            player.playSound(Messages.SOUND_ERROR);
            return;
        }

        Kit kit = Kits.byId(note.kitId());
        if (kit == null) {
            player.sendMessage(Messages.NOTE_UNKNOWN_KIT);
            player.playSound(Messages.SOUND_ERROR);
            return;
        }

        List<ItemStack> items = plugin.kitItems().items(kit);
        if (items.isEmpty()) {
            player.sendMessage(Messages.KIT_EMPTY);
            player.playSound(Messages.SOUND_ERROR);
            return;
        }

        // Space check counts the note's own slot as free if it's the last note in a main-hand stack
        ItemStack[] storage = player.getInventory().getStorageContents();
        if (hand == EquipmentSlot.HAND && item.getAmount() <= 1) {
            storage[player.getInventory().getHeldItemSlot()] = null;
        }
        if (!KitClaimer.fits(storage, items)) {
            player.sendMessage(Messages.NOTE_NO_SPACE);
            player.playSound(Messages.SOUND_ERROR);
            return;
        }

        if (!notes.markRedeemed(note.noteId())) {
            player.sendMessage(Messages.NOTE_ERROR);
            player.playSound(Messages.SOUND_ERROR);
            return;
        }

        ItemStack remaining = consumeOne(item);
        if (hand == EquipmentSlot.OFF_HAND) {
            player.getInventory().setItemInOffHand(remaining);
        } else {
            player.getInventory().setItemInMainHand(remaining);
        }

        // Anything that somehow doesn't fit is dropped rather than lost
        player.getInventory().addItem(items.toArray(ItemStack[]::new))
                .values()
                .forEach(left -> player.getWorld().dropItemNaturally(player.getLocation(), left));

        player.sendMessage(Messages.kitNoteOpened(kit));
        player.playSound(Messages.SOUND_NOTE_OPEN);
    }

    // Kit Notes are paper, so stop them being crafted into books/maps
    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        for (ItemStack item : event.getInventory().getMatrix()) {
            if (plugin.kitNotes().isKitNote(item)) {
                event.getInventory().setResult(null);
                return;
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCraft(CraftItemEvent event) {
        for (ItemStack item : event.getInventory().getMatrix()) {
            if (plugin.kitNotes().isKitNote(item)) {
                event.setCancelled(true);
                event.getWhoClicked().sendMessage(Messages.NOTE_NO_CRAFT);
                return;
            }
        }
    }

    private static ItemStack consumeOne(ItemStack item) {
        if (item.getAmount() <= 1) return null;

        ItemStack result = item.clone();
        result.setAmount(item.getAmount() - 1);
        return result;
    }
}
