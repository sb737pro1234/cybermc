package me.sbpro.cyberquests.data;

import me.sbpro.cyberquests.quests.ActiveQuest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Per-player quest data. A player has {@link #SLOT_COUNT} fixed quest slots and every slot has its own
 * independent cooldown timestamp (epoch milliseconds, 0 = no cooldown).
 */
public class PlayerData {
    public static final int SLOT_COUNT = 4;

    private final UUID uuid;
    private final ActiveQuest[] slots = new ActiveQuest[SLOT_COUNT];
    private final long[] cooldownUntil = new long[SLOT_COUNT];

    public PlayerData(UUID uuid) { this.uuid = uuid; }
    public UUID getUuid() { return uuid; }

    public ActiveQuest getSlot(int slot) { return validSlot(slot) ? slots[slot] : null; }
    public void setSlot(int slot, ActiveQuest quest) { if (validSlot(slot)) slots[slot] = quest; }

    public long getCooldownUntil(int slot) { return validSlot(slot) ? cooldownUntil[slot] : 0L; }
    public void setCooldownUntil(int slot, long timestamp) { if (validSlot(slot)) cooldownUntil[slot] = timestamp; }

    /** True while the slot's cooldown has not expired yet. */
    public boolean isOnCooldown(int slot, long now) { return validSlot(slot) && cooldownUntil[slot] > now; }

    public boolean hasAnyCooldown() {
        for (long until : cooldownUntil) if (until > 0L) return true;
        return false;
    }

    /** The non-empty quests, in slot order. */
    public List<ActiveQuest> getActiveQuests() {
        List<ActiveQuest> result = new ArrayList<>();
        for (ActiveQuest quest : slots) if (quest != null) result.add(quest);
        return result;
    }

    /** Index of the first empty slot that is not on cooldown, or -1. */
    public int findFreeSlot(long now) {
        for (int i = 0; i < SLOT_COUNT; i++) if (slots[i] == null && cooldownUntil[i] == 0L) return i;
        return -1;
    }

    private boolean validSlot(int slot) { return slot >= 0 && slot < SLOT_COUNT; }
}
