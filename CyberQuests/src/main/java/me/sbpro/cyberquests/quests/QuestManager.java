package me.sbpro.cyberquests.quests;

import me.sbpro.cyberquests.CyberQuests;
import me.sbpro.cyberquests.Messages;
import me.sbpro.cyberquests.Quests;
import me.sbpro.cyberquests.data.PlayerData;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import java.util.*;

public class QuestManager {
    /** Every quest slot has its own five-minute cooldown after its quest is completed. */
    public static final long SLOT_COOLDOWN_MILLIS = 5L * 60L * 1000L;

    private final CyberQuests plugin;
    private final Map<UUID, PlayerData> players = new HashMap<>();
    private final Set<UUID> dirty = new HashSet<>();
    private final Random random = new Random();

    public QuestManager(CyberQuests plugin) { this.plugin = plugin; }

    public PlayerData getData(UUID uuid) {
        return players.computeIfAbsent(uuid, id -> {
            PlayerData data = plugin.getDataManager().load(id);
            fillNewSlots(data);
            plugin.getDataManager().save(data);
            return data;
        });
    }

    /** Loads a joining player and unlocks any slot whose cooldown expired while they were offline. */
    public void loadPlayer(Player player) {
        getData(player.getUniqueId());
        processCooldowns(player);
    }

    public void savePlayer(Player player) {
        PlayerData data = players.get(player.getUniqueId());
        if (data != null) plugin.getDataManager().save(data);
        dirty.remove(player.getUniqueId());
    }

    public void saveAll() { plugin.getDataManager().saveAll(players.values()); dirty.clear(); }

    public void unloadPlayer(Player player) { savePlayer(player); players.remove(player.getUniqueId()); }

    /** Writes progress changes that have not been saved yet (called periodically). */
    public void flushDirty() {
        if (dirty.isEmpty()) return;
        List<PlayerData> toSave = new ArrayList<>();
        for (UUID uuid : dirty) {
            PlayerData data = players.get(uuid);
            if (data != null) toSave.add(data);
        }
        dirty.clear();
        if (!toSave.isEmpty()) plugin.getDataManager().saveAll(toSave);
    }

    private void saveNow(PlayerData data) {
        plugin.getDataManager().save(data);
        dirty.remove(data.getUuid());
    }

    private void markDirty(PlayerData data) { dirty.add(data.getUuid()); }

    /** Fills slots that were never assigned (new players, resets, unknown quests). Slots with a cooldown are left alone. */
    private void fillNewSlots(PlayerData data) {
        for (int i = 0; i < PlayerData.SLOT_COUNT; i++) {
            if (data.getSlot(i) != null || data.getCooldownUntil(i) != 0L) continue;
            QuestDefinition next = getRandomAvailableQuest(data);
            if (next == null) break;
            data.setSlot(i, new ActiveQuest(next.getId(), 0, false));
        }
    }

    private QuestDefinition getRandomAvailableQuest(PlayerData data) {
        Set<String> active = new HashSet<>();
        for (ActiveQuest q : data.getActiveQuests()) active.add(q.getQuestId());
        List<QuestDefinition> available = new ArrayList<>();
        for (QuestDefinition q : Quests.NORMAL_QUESTS) if (!active.contains(q.getId())) available.add(q);
        if (available.isEmpty()) return null;
        return available.get(random.nextInt(available.size()));
    }

    public QuestDefinition getDefinition(String id) { return Quests.QUESTS.get(id); }

    /**
     * Assigns a new Normal quest to every slot whose cooldown has expired and tells the player.
     * Called every second for online players and when a player joins.
     */
    public void processCooldowns(Player player) {
        PlayerData data = players.get(player.getUniqueId());
        if (data == null) return;
        long now = System.currentTimeMillis();
        boolean changed = false;
        for (int i = 0; i < PlayerData.SLOT_COUNT; i++) {
            long until = data.getCooldownUntil(i);
            if (data.getSlot(i) != null || until == 0L || until > now) continue;
            QuestDefinition next = getRandomAvailableQuest(data);
            if (next == null) continue;
            data.setSlot(i, new ActiveQuest(next.getId(), 0, false));
            data.setCooldownUntil(i, 0L);
            changed = true;
            player.sendMessage(Messages.slotUnlocked(i + 1, next.getTitle()));
        }
        if (changed) saveNow(data);
    }

    /** Formats a remaining time such as "4m 59s" or "45s". */
    public static String formatDuration(long millis) {
        long totalSeconds = Math.max(0L, (millis + 999L) / 1000L);
        long minutes = totalSeconds / 60L;
        long seconds = totalSeconds % 60L;
        return minutes > 0 ? minutes + "m " + seconds + "s" : seconds + "s";
    }

    /** True when this slot holds a Normal quest that may be Easified (never on cooldown, never already Easy). */
    public boolean canEasify(PlayerData data, int slot) {
        if (slot < 0 || slot >= PlayerData.SLOT_COUNT) return false;
        if (data.isOnCooldown(slot, System.currentTimeMillis())) return false;
        ActiveQuest active = data.getSlot(slot);
        return active != null && !active.isEasy();
    }

    /**
     * Converts a Normal quest into a new Easy quest.
     * This is the only allowed quest transformation. It can only happen once because Easy quests are
     * never eligible, and it is never possible while the slot is on cooldown.
     */
    public boolean easify(Player player, int index) {
        PlayerData data = getData(player.getUniqueId());
        if (!canEasify(data, index)) {
            player.sendMessage(data.isOnCooldown(index, System.currentTimeMillis())
                    ? Messages.slotOnCooldown(formatDuration(data.getCooldownUntil(index) - System.currentTimeMillis()))
                    : Messages.EASIFY_UNAVAILABLE);
            return false;
        }

        ActiveQuest current = data.getSlot(index);
        QuestDefinition next = getRandomEasyQuest(data, current.getQuestId());
        if (next == null) return false;

        if (!plugin.getCoinManager().takeCoins(player.getUniqueId(), 2)) {
            player.sendMessage(Messages.NOT_ENOUGH_COINS);
            return false;
        }

        data.setSlot(index, new ActiveQuest(next.getId(), 0, true));
        saveNow(data);
        plugin.getBossBars().hide(player);
        player.sendMessage(Messages.EASY_SUCCESS);
        return true;
    }

    private QuestDefinition getRandomEasyQuest(PlayerData data, String excluded) {
        Set<String> active = new HashSet<>();
        for (ActiveQuest q : data.getActiveQuests()) active.add(q.getQuestId());

        List<QuestDefinition> available = new ArrayList<>();
        for (QuestDefinition q : Quests.EASY_QUESTS) {
            if (!active.contains(q.getId()) && !q.getId().equalsIgnoreCase(excluded)) {
                available.add(q);
            }
        }

        if (available.isEmpty()) return null;
        return available.get(random.nextInt(available.size()));
    }

    public boolean giveQuest(Player target, String questId, boolean notify) {
        PlayerData data = getData(target.getUniqueId());
        QuestDefinition definition = Quests.QUESTS.get(questId);
        if (definition == null) return false;
        int slot = data.findFreeSlot(System.currentTimeMillis());
        if (slot < 0) return false;
        for (ActiveQuest active : data.getActiveQuests()) if (active.getQuestId().equalsIgnoreCase(questId)) return false;
        data.setSlot(slot, new ActiveQuest(definition.getId(), 0, Quests.isEasy(definition.getId())));
        saveNow(data);
        if (notify) target.sendMessage(Messages.questGivenToYou(definition.getTitle()));
        return true;
    }

    public void reset(Player target) {
        PlayerData data = new PlayerData(target.getUniqueId());
        fillNewSlots(data);
        players.put(target.getUniqueId(), data);
        plugin.getBossBars().hide(target);
        saveNow(data);
    }

    public void addProgress(Player player, QuestType type, String target, int amount) {
        if (amount <= 0) return;
        PlayerData data = getData(player.getUniqueId());
        List<Integer> completed = new ArrayList<>();
        boolean changed = false;
        for (int i = 0; i < PlayerData.SLOT_COUNT; i++) {
            ActiveQuest active = data.getSlot(i);
            if (active == null) continue;
            QuestDefinition quest = getDefinition(active.getQuestId());
            if (quest == null || quest.getType() != type || !quest.matches(target)) continue;
            int newProgress = Math.min(quest.getRequiredAmount(), active.getProgress() + amount);
            if (newProgress != active.getProgress()) { active.setProgress(newProgress); changed = true; }
            if (newProgress >= quest.getRequiredAmount()) completed.add(i);
            else plugin.getBossBars().showProgress(player, quest, newProgress);
        }
        for (int slot : completed) completeQuest(player, data, slot);
        if (!completed.isEmpty()) saveNow(data);
        else if (changed) markDirty(data);
    }

    private void completeQuest(Player player, PlayerData data, int slot) {
        ActiveQuest active = data.getSlot(slot);
        if (active == null) return;
        QuestDefinition quest = getDefinition(active.getQuestId());
        if (quest == null || active.getProgress() < quest.getRequiredAmount()) return;
        int reward = active.isEasy() ? 5 : 10;

        // The slot stays empty and enters its own cooldown; the other slots are unaffected.
        data.setSlot(slot, null);
        data.setCooldownUntil(slot, System.currentTimeMillis() + SLOT_COOLDOWN_MILLIS);

        plugin.getBossBars().hide(player);
        plugin.getCoinManager().addCoins(player.getUniqueId(), reward);
        player.showTitle(Title.title(
                Messages.QUEST_COMPLETION_TITLE,
                Messages.questCompletionSubtitle(reward),
                Title.Times.times(Messages.QUEST_COMPLETION_FADE_IN, Messages.QUEST_COMPLETION_STAY, Messages.QUEST_COMPLETION_FADE_OUT)));
        player.sendMessage(Messages.QUEST_COMPLETION_BOX_LINE);
        player.sendMessage(Messages.questCompletionBoxMessage(quest.getTitle(), reward));
        player.sendMessage(Messages.QUEST_COMPLETION_BOX_LINE);
        player.sendMessage(Messages.slotCooldownStarted(slot + 1, formatDuration(SLOT_COOLDOWN_MILLIS)));
    }

    public void refreshAfterConfigChange() { for (PlayerData data : players.values()) fillNewSlots(data); }
}
