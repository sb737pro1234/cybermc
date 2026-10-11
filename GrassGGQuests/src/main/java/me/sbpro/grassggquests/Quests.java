package me.sbpro.grassggquests;

import me.sbpro.grassggquests.quests.QuestDefinition;
import me.sbpro.grassggquests.quests.QuestType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Quest definitions used by GrassGGQuests. There are exactly two difficulties: Normal and Easy. */
public final class Quests {
    private Quests() {}

    /** All available quest definitions. */
    public static final Map<String, QuestDefinition> QUESTS = new LinkedHashMap<>();

    /** Quest ids used by older versions, mapped to their current ids (keeps saved player data valid). */
    public static final Map<String, String> LEGACY_IDS = Map.of(
            "craft_cooked_beef", "smelt_cooked_beef"
    );

    /** Easy quests used when a Normal quest is Easified. */
    public static final List<QuestDefinition> EASY_QUESTS;

    /** Normal quests used for the four starting quests and replacements after a cooldown. */
    public static final List<QuestDefinition> NORMAL_QUESTS;

    private static final List<QuestDefinition> EASY = new ArrayList<>();
    private static final List<QuestDefinition> NORMAL = new ArrayList<>();

    static {
        // ---------------------------------------------------------------------
        // EASY (unchanged)
        // ---------------------------------------------------------------------

        addEasy(new QuestDefinition("mine_dirt", "Mine 150 Dirt", "Mine 150 Dirt blocks.",
                QuestType.BREAK_BLOCK, "DIRT", 150));
        addEasy(new QuestDefinition("mine_sand", "Mine 100 Sand", "Mine 100 Sand blocks.",
                QuestType.BREAK_BLOCK, "SAND", 100));
        addEasy(new QuestDefinition("mine_gravel", "Mine 75 Gravel", "Mine 75 Gravel blocks.",
                QuestType.BREAK_BLOCK, "GRAVEL", 75));
        addEasy(new QuestDefinition("chop_oak", "Break 64 Oak Logs", "Break 64 Oak Logs.",
                QuestType.BREAK_BLOCK, "OAK_LOG", 64));
        addEasy(new QuestDefinition("chop_birch", "Break 64 Birch Logs", "Break 64 Birch Logs.",
                QuestType.BREAK_BLOCK, "BIRCH_LOG", 64));
        addEasy(new QuestDefinition("kill_chickens", "Kill 15 Chickens", "Kill 15 Chickens.",
                QuestType.KILL_ENTITY, "CHICKEN", 15));
        addEasy(new QuestDefinition("kill_cows", "Kill 15 Cows", "Kill 15 Cows.",
                QuestType.KILL_ENTITY, "COW", 15));
        addEasy(new QuestDefinition("kill_pigs", "Kill 15 Pigs", "Kill 15 Pigs.",
                QuestType.KILL_ENTITY, "PIG", 15));
        addEasy(new QuestDefinition("craft_torches", "Craft 32 Torches", "Craft 32 Torches.",
                QuestType.CRAFT_ITEM, "TORCH", 32));
        addEasy(new QuestDefinition("craft_bread", "Craft 8 Bread", "Craft 8 Bread.",
                QuestType.CRAFT_ITEM, "BREAD", 8));
        addEasy(new QuestDefinition("eat_food", "Eat 10 Steak", "Eat 10 Cooked Beef.",
                QuestType.EAT_ITEM, "COOKED_BEEF", 10));

        // ---------------------------------------------------------------------
        // NORMAL
        // Roughly 8-12 minutes each. Ore quests count the regular and deepslate variants.
        // ---------------------------------------------------------------------

        // Mining
        addNormal(new QuestDefinition("mine_stone", "Mine 256 Stone", "Mine 256 Stone blocks.",
                QuestType.BREAK_BLOCK, "STONE", 256));
        addNormal(new QuestDefinition("mine_deepslate", "Mine 192 Deepslate", "Mine 192 Deepslate blocks.",
                QuestType.BREAK_BLOCK, "DEEPSLATE", 192));
        addNormal(new QuestDefinition("mine_coal", "Mine 96 Coal Ore", "Mine 96 Coal Ore or Deepslate Coal Ore.",
                QuestType.BREAK_BLOCK, List.of("COAL_ORE", "DEEPSLATE_COAL_ORE"), 96));
        addNormal(new QuestDefinition("mine_iron", "Mine 48 Iron Ore", "Mine 48 Iron Ore or Deepslate Iron Ore.",
                QuestType.BREAK_BLOCK, List.of("IRON_ORE", "DEEPSLATE_IRON_ORE"), 48));
        addNormal(new QuestDefinition("mine_copper", "Mine 96 Copper Ore", "Mine 96 Copper Ore or Deepslate Copper Ore.",
                QuestType.BREAK_BLOCK, List.of("COPPER_ORE", "DEEPSLATE_COPPER_ORE"), 96));
        addNormal(new QuestDefinition("mine_gold", "Mine 24 Gold Ore", "Mine 24 Gold Ore or Deepslate Gold Ore.",
                QuestType.BREAK_BLOCK, List.of("GOLD_ORE", "DEEPSLATE_GOLD_ORE"), 24));
        addNormal(new QuestDefinition("mine_redstone", "Mine 48 Redstone Ore", "Mine 48 Redstone Ore or Deepslate Redstone Ore.",
                QuestType.BREAK_BLOCK, List.of("REDSTONE_ORE", "DEEPSLATE_REDSTONE_ORE"), 48));
        addNormal(new QuestDefinition("mine_lapis", "Mine 32 Lapis Ore", "Mine 32 Lapis Ore or Deepslate Lapis Ore.",
                QuestType.BREAK_BLOCK, List.of("LAPIS_ORE", "DEEPSLATE_LAPIS_ORE"), 32));
        addNormal(new QuestDefinition("mine_diamond", "Mine 12 Diamond Ore", "Mine 12 Diamond Ore or Deepslate Diamond Ore.",
                QuestType.BREAK_BLOCK, List.of("DIAMOND_ORE", "DEEPSLATE_DIAMOND_ORE"), 12));

        // Gathering
        addNormal(new QuestDefinition("chop_spruce", "Break 192 Spruce Logs", "Break 192 Spruce Logs.",
                QuestType.BREAK_BLOCK, "SPRUCE_LOG", 192));

        // Farming (only fully grown crops count)
        addNormal(new QuestDefinition("harvest_wheat", "Harvest 128 Wheat", "Harvest 128 fully grown Wheat crops.",
                QuestType.HARVEST_CROP, "WHEAT", 128));
        addNormal(new QuestDefinition("harvest_carrots", "Harvest 96 Carrots", "Harvest 96 fully grown Carrot crops.",
                QuestType.HARVEST_CROP, "CARROTS", 96));
        addNormal(new QuestDefinition("harvest_melons", "Harvest 64 Melons", "Break 64 Melon blocks grown on a farm.",
                QuestType.BREAK_BLOCK, "MELON", 64));

        // Crafting and smelting
        addNormal(new QuestDefinition("craft_furnaces", "Craft 16 Furnaces", "Craft 16 Furnaces.",
                QuestType.CRAFT_ITEM, "FURNACE", 16));
        addNormal(new QuestDefinition("craft_chests", "Craft 16 Chests", "Craft 16 Chests.",
                QuestType.CRAFT_ITEM, "CHEST", 16));
        addNormal(new QuestDefinition("craft_bread_normal", "Craft 24 Bread", "Craft 24 Bread.",
                QuestType.CRAFT_ITEM, "BREAD", 24));
        addNormal(new QuestDefinition("smelt_cooked_beef", "Smelt 32 Steak", "Smelt 32 Steak in a furnace, smoker or blast furnace and take it out.",
                QuestType.SMELT_ITEM, "COOKED_BEEF", 32));
        addNormal(new QuestDefinition("smelt_iron_ingots", "Smelt 48 Iron Ingots", "Smelt 48 Iron Ingots in a furnace or blast furnace and take them out.",
                QuestType.SMELT_ITEM, "IRON_INGOT", 48));

        // Fishing
        addNormal(new QuestDefinition("fish_cod", "Catch 32 Cod", "Catch 32 Cod.",
                QuestType.FISH_ITEM, "COD", 32));
        addNormal(new QuestDefinition("fish_salmon", "Catch 16 Salmon", "Catch 16 Salmon.",
                QuestType.FISH_ITEM, "SALMON", 16));

        // Eating
        addNormal(new QuestDefinition("eat_steak", "Eat 32 Steak", "Eat 32 Cooked Beef.",
                QuestType.EAT_ITEM, "COOKED_BEEF", 32));
        addNormal(new QuestDefinition("eat_porkchops", "Eat 24 Cooked Porkchops", "Eat 24 Cooked Porkchops.",
                QuestType.EAT_ITEM, "COOKED_PORKCHOP", 24));

        // Mob killing
        addNormal(new QuestDefinition("kill_spiders", "Kill 40 Spiders", "Kill 40 Spiders.",
                QuestType.KILL_ENTITY, "SPIDER", 40));
        addNormal(new QuestDefinition("kill_creepers", "Kill 30 Creepers", "Kill 30 Creepers.",
                QuestType.KILL_ENTITY, "CREEPER", 30));
        addNormal(new QuestDefinition("kill_zombies", "Kill 50 Zombies", "Kill 50 Zombies.",
                QuestType.KILL_ENTITY, "ZOMBIE", 50));
        addNormal(new QuestDefinition("kill_skeletons", "Kill 40 Skeletons", "Kill 40 Skeletons.",
                QuestType.KILL_ENTITY, "SKELETON", 40));

        EASY_QUESTS = List.copyOf(EASY);
        NORMAL_QUESTS = List.copyOf(NORMAL);
    }

    private static void addEasy(QuestDefinition quest) {
        QUESTS.put(quest.getId(), quest);
        EASY.add(quest);
    }

    private static void addNormal(QuestDefinition quest) {
        QUESTS.put(quest.getId(), quest);
        NORMAL.add(quest);
    }

    /** Returns true when the quest id belongs to an Easy quest. */
    public static boolean isEasy(String questId) {
        QuestDefinition definition = QUESTS.get(questId);
        return definition != null && EASY_QUESTS.contains(definition);
    }

    /** Resolves a stored quest id (including ids from older versions) to a current quest id, or null if unknown. */
    public static String resolveId(String storedId) {
        if (storedId == null) return null;
        String id = storedId.toLowerCase(Locale.ROOT);
        id = LEGACY_IDS.getOrDefault(id, id);
        return QUESTS.containsKey(id) ? id : null;
    }
}
