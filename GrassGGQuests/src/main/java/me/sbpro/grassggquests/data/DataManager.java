package me.sbpro.grassggquests.data;

import me.sbpro.grassggquests.GrassGGQuests;
import me.sbpro.grassggquests.Quests;
import me.sbpro.grassggquests.quests.ActiveQuest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class DataManager {
    private final GrassGGQuests plugin;
    private final File file;
    private final YamlConfiguration data;

    public DataManager(GrassGGQuests plugin) {
        this.plugin = plugin;
        if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdirs();
        file = new File(plugin.getDataFolder(), "players.yml");
        data = YamlConfiguration.loadConfiguration(file);
    }

    public PlayerData load(UUID uuid) {
        PlayerData playerData = new PlayerData(uuid);
        String path = "players." + uuid;

        // Current format: players.<uuid>.slots.<0-3>.{id,progress,easy,cooldown-until}
        ConfigurationSection slots = data.getConfigurationSection(path + ".slots");
        if (slots != null) {
            for (int i = 0; i < PlayerData.SLOT_COUNT; i++) {
                ConfigurationSection slot = slots.getConfigurationSection(String.valueOf(i));
                if (slot == null) continue;
                playerData.setCooldownUntil(i, Math.max(0L, slot.getLong("cooldown-until", 0L)));
                ActiveQuest quest = readQuest(slot);
                if (quest != null) playerData.setSlot(i, quest);
            }
            return playerData;
        }

        // Older format: players.<uuid>.active-quests.<index>.{id,progress,easy}
        ConfigurationSection legacy = data.getConfigurationSection(path + ".active-quests");
        if (legacy != null) {
            int next = 0;
            for (String key : legacy.getKeys(false)) {
                if (next >= PlayerData.SLOT_COUNT) break;
                ConfigurationSection section = legacy.getConfigurationSection(key);
                if (section == null) continue;
                ActiveQuest quest = readQuest(section);
                if (quest != null) playerData.setSlot(next++, quest);
            }
        }
        return playerData;
    }

    private ActiveQuest readQuest(ConfigurationSection section) {
        String questId = Quests.resolveId(section.getString("id"));
        if (questId == null) return null; // unknown quest, the slot will be refilled
        int progress = Math.max(0, section.getInt("progress", 0));
        // The difficulty always follows the quest definition so labels and rewards can never be inconsistent.
        return new ActiveQuest(questId, progress, Quests.isEasy(questId));
    }

    private void write(PlayerData playerData) {
        String path = "players." + playerData.getUuid();
        data.set(path, null);
        for (int i = 0; i < PlayerData.SLOT_COUNT; i++) {
            ActiveQuest quest = playerData.getSlot(i);
            long cooldown = playerData.getCooldownUntil(i);
            String slotPath = path + ".slots." + i;
            if (quest != null) {
                data.set(slotPath + ".id", quest.getQuestId());
                data.set(slotPath + ".progress", quest.getProgress());
                data.set(slotPath + ".easy", quest.isEasy());
            }
            if (cooldown > 0L) data.set(slotPath + ".cooldown-until", cooldown);
        }
    }

    public void save(PlayerData playerData) {
        write(playerData);
        saveFile();
    }

    public void delete(UUID uuid) { data.set("players." + uuid, null); saveFile(); }

    public void saveAll(Iterable<PlayerData> players) {
        for (PlayerData player : players) write(player);
        saveFile();
    }

    private void saveFile() {
        try { data.save(file); } catch (IOException e) { plugin.getLogger().severe("Could not save players.yml: " + e.getMessage()); }
    }
}
