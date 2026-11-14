package me.sbpro.cyberkits.note;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import me.sbpro.cyberkits.Messages;
import me.sbpro.cyberkits.kit.Kit;

/**
 * Kit Notes: tradeable paper given when a kit is claimed. Same security model as CyberCoins bank notes:
 * every note has a unique id in its persistent data, and redeemed ids are written to kitnotes.yml
 * (saved before the items are given, rolled back if the save fails), so copies of a note can only be used once.
 */
public final class KitNoteManager {

    private final Plugin plugin;
    private final File file;
    private final YamlConfiguration data;

    private final NamespacedKey noteIdKey;
    private final NamespacedKey kitKey;
    private final NamespacedKey claimedByUuidKey;
    private final NamespacedKey claimedByNameKey;

    public KitNoteManager(Plugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "kitnotes.yml");
        this.data = YamlConfiguration.loadConfiguration(file);

        this.noteIdKey = new NamespacedKey(plugin, "kit_note_id");
        this.kitKey = new NamespacedKey(plugin, "kit_note_kit");
        this.claimedByUuidKey = new NamespacedKey(plugin, "kit_note_claimed_by_uuid");
        this.claimedByNameKey = new NamespacedKey(plugin, "kit_note_claimed_by_name");
    }

    public ItemStack create(Player player, Kit kit) {
        ItemStack item = new ItemStack(Messages.KIT_NOTE_MATERIAL);

        item.editMeta(meta -> {
            meta.displayName(Messages.KIT_NOTE_NAME);
            meta.lore(Messages.kitNoteLore(kit));

            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            pdc.set(noteIdKey, PersistentDataType.STRING, UUID.randomUUID().toString());
            pdc.set(kitKey, PersistentDataType.STRING, kit.id());
            pdc.set(claimedByUuidKey, PersistentDataType.STRING, player.getUniqueId().toString());
            pdc.set(claimedByNameKey, PersistentDataType.STRING, player.getName());
        });

        return item;
    }

    public Optional<KitNoteData> read(ItemStack item) {
        if (item == null || item.getType().isAir() || !item.hasItemMeta()) return Optional.empty();

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        String noteId = pdc.get(noteIdKey, PersistentDataType.STRING);
        String kitId = pdc.get(kitKey, PersistentDataType.STRING);
        String claimedByUuid = pdc.get(claimedByUuidKey, PersistentDataType.STRING);
        String claimedByName = pdc.get(claimedByNameKey, PersistentDataType.STRING);

        if (noteId == null || kitId == null || claimedByUuid == null || claimedByName == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(new KitNoteData(
                    UUID.fromString(noteId),
                    kitId,
                    UUID.fromString(claimedByUuid),
                    claimedByName
            ));
        } catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }

    public boolean isKitNote(ItemStack item) {
        return read(item).isPresent();
    }

    public synchronized boolean isRedeemed(UUID noteId) {
        return data.getBoolean("redeemed." + noteId, false);
    }

    /** Marks the note as used and saves. Returns false (and changes nothing) if it was already used or the save failed. */
    public synchronized boolean markRedeemed(UUID noteId) {
        if (isRedeemed(noteId)) return false;

        String path = "redeemed." + noteId;
        data.set(path, true);

        if (!saveInternal()) {
            data.set(path, null);
            return false;
        }
        return true;
    }

    public synchronized void save() {
        saveInternal();
    }

    private boolean saveInternal() {
        try {
            plugin.getDataFolder().mkdirs();
            data.save(file);
            return true;
        } catch (IOException exception) {
            plugin.getLogger().severe("Could not save kitnotes.yml: " + exception.getMessage());
            return false;
        }
    }
}
