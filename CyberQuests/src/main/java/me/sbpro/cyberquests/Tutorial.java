package me.sbpro.cyberquests;

import me.sbpro.cyberquests.quests.TutorialStep;
import me.sbpro.cyberquests.quests.QuestDefinition;
import me.sbpro.cyberquests.quests.QuestType;
import org.bukkit.Material;
import org.bukkit.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * The Tutorial: early-game progression quests, completed one at a time and in order.
 * Finishing the last step unlocks the Quest Board. Rewards are small themed items, never coins.
 */
public final class Tutorial {
    private Tutorial() {}

    public static final List<TutorialStep> STEPS;

    static {
        List<String> logs = new ArrayList<>();
        for (Material log : Tag.LOGS.getValues()) logs.add(log.name());

        List<TutorialStep> steps = new ArrayList<>();

        // Command quests name only the main command; aliases are in Aliases.java. /rtp, /shop and /sell come from other plugins.
        steps.add(step("tutorial_open", "Do /tutorial", "Open the Tutorial menu with /tutorial.",
                QuestType.RUN_COMMAND, List.of("tutorial"), 1,
                Material.FILLED_MAP, Material.BREAD, 3, "3 Bread"));
        steps.add(step("tutorial_rtp", "Do /rtp", "Teleport somewhere random in the wild with /rtp.",
                QuestType.RUN_COMMAND, List.of("rtp"), 1,
                Material.ENDER_PEARL, Material.COMPASS, 1, "1 Compass"));
        steps.add(step("tutorial_first_log", "Break your first log", "Punch a tree and break any log.",
                QuestType.BREAK_BLOCK, logs, 1,
                Material.OAK_LOG, Material.OAK_LOG, 3, "3 Oak Logs"));
        steps.add(step("tutorial_logs", "Break 16 logs", "Break 16 logs of any wood type.",
                QuestType.BREAK_BLOCK, logs, 16,
                Material.SPRUCE_LOG, Material.APPLE, 2, "2 Apples"));
        steps.add(step("tutorial_crafting_table", "Craft a Crafting Table", "Craft a Crafting Table from planks.",
                QuestType.CRAFT_ITEM, List.of("CRAFTING_TABLE"), 1,
                Material.CRAFTING_TABLE, Material.STICK, 8, "8 Sticks"));
        steps.add(step("tutorial_homes", "Do /homes", "Open your homes menu with /homes.",
                QuestType.RUN_COMMAND, List.of("homes"), 1,
                Material.RED_BED, Material.WHITE_BED, 1, "1 White Bed"));
        steps.add(step("tutorial_wooden_pickaxe", "Craft a Wooden Pickaxe", "Craft a Wooden Pickaxe.",
                QuestType.CRAFT_ITEM, List.of("WOODEN_PICKAXE"), 1,
                Material.WOODEN_PICKAXE, Material.COBBLESTONE, 10, "10 Cobblestone"));
        steps.add(step("tutorial_stone", "Mine 16 Stone", "Mine 16 Stone or Deepslate.",
                QuestType.BREAK_BLOCK, List.of("STONE", "DEEPSLATE"), 16,
                Material.STONE, Material.BREAD, 3, "3 Bread"));
        steps.add(step("tutorial_stone_pickaxe", "Craft a Stone Pickaxe", "Craft a Stone Pickaxe.",
                QuestType.CRAFT_ITEM, List.of("STONE_PICKAXE"), 1,
                Material.STONE_PICKAXE, Material.TORCH, 4, "4 Torches"));
        steps.add(step("tutorial_coal", "Mine 8 Coal Ore", "Mine 8 Coal Ore or Deepslate Coal Ore.",
                QuestType.BREAK_BLOCK, List.of("COAL_ORE", "DEEPSLATE_COAL_ORE"), 8,
                Material.COAL_ORE, Material.COBBLESTONE, 8, "8 Cobblestone"));
        steps.add(step("tutorial_shop", "Do /shop", "Have a look around the server shop with /shop.",
                QuestType.RUN_COMMAND, List.of("shop"), 1,
                Material.EMERALD, Material.CHEST, 1, "1 Chest"));
        steps.add(step("tutorial_furnace", "Craft a Furnace", "Craft a Furnace from cobblestone.",
                QuestType.CRAFT_ITEM, List.of("FURNACE"), 1,
                Material.FURNACE, Material.COAL, 4, "4 Coal"));
        steps.add(step("tutorial_cook_food", "Cook 4 food", "Cook 4 meat or fish and take it out of a furnace or smoker.",
                QuestType.SMELT_ITEM, List.of("COOKED_BEEF", "COOKED_PORKCHOP", "COOKED_MUTTON", "COOKED_CHICKEN",
                        "COOKED_RABBIT", "COOKED_COD", "COOKED_SALMON"), 4,
                Material.COOKED_BEEF, Material.SMOKER, 1, "1 Smoker"));
        steps.add(step("tutorial_sell", "Do /sell", "Sell items you do not need with /sell.",
                QuestType.RUN_COMMAND, List.of("sell"), 1,
                Material.GOLD_INGOT, Material.BARREL, 1, "1 Barrel"));
        steps.add(step("tutorial_hostiles", "Kill 5 hostile mobs", "Kill 5 Zombies, Skeletons, Spiders or Creepers.",
                QuestType.KILL_ENTITY, List.of("ZOMBIE", "HUSK", "DROWNED", "ZOMBIE_VILLAGER", "SKELETON", "STRAY",
                        "SPIDER", "CAVE_SPIDER", "CREEPER"), 5,
                Material.STONE_SWORD, Material.ARROW, 8, "8 Arrows"));
        steps.add(step("tutorial_coins", "Do /coins", "Check your coin balance with /coins.",
                QuestType.RUN_COMMAND, List.of("coins"), 1,
                Material.SUNFLOWER, Material.GOLD_NUGGET, 3, "3 Gold Nuggets"));
        steps.add(step("tutorial_iron_ore", "Mine 6 Iron Ore", "Mine 6 Iron Ore or Deepslate Iron Ore.",
                QuestType.BREAK_BLOCK, List.of("IRON_ORE", "DEEPSLATE_IRON_ORE"), 6,
                Material.IRON_ORE, Material.COAL, 8, "8 Coal"));
        steps.add(step("tutorial_iron_ingots", "Smelt 6 Iron Ingots", "Smelt 6 Iron Ingots and take them out of a furnace or blast furnace.",
                QuestType.SMELT_ITEM, List.of("IRON_INGOT"), 6,
                Material.IRON_INGOT, Material.SHIELD, 1, "1 Shield"));
        steps.add(step("tutorial_iron_pickaxe", "Craft an Iron Pickaxe", "Craft an Iron Pickaxe.",
                QuestType.CRAFT_ITEM, List.of("IRON_PICKAXE"), 1,
                Material.IRON_PICKAXE, Material.TORCH, 16, "16 Torches"));
        steps.add(step("tutorial_spawn", "Do /spawn", "Head back to spawn with /spawn to finish the Tutorial.",
                QuestType.RUN_COMMAND, List.of("spawn"), 1,
                Material.LODESTONE, Material.COOKED_BEEF, 3, "3 Steak"));

        STEPS = List.copyOf(steps);
    }

    private static TutorialStep step(String id, String title, String description, QuestType type, List<String> targets,
                                    int amount, Material icon, Material reward, int rewardAmount, String rewardLabel) {
        return new TutorialStep(new QuestDefinition(id, title, description, type, targets, amount),
                icon, reward, rewardAmount, rewardLabel);
    }

    public static int size() { return STEPS.size(); }

    /** The step at this index, or null once the Tutorial is finished. */
    public static TutorialStep get(int index) {
        return index >= 0 && index < STEPS.size() ? STEPS.get(index) : null;
    }
}
