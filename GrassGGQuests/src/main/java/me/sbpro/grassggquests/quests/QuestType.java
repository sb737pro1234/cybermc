package me.sbpro.grassggquests.quests;

public enum QuestType {
    /** Breaking blocks. Only naturally occurring blocks count (player-placed blocks are ignored). */
    BREAK_BLOCK,
    PLACE_BLOCK,
    KILL_ENTITY,
    CRAFT_ITEM,
    /** Taking smelted items out of a furnace, smoker or blast furnace. */
    SMELT_ITEM,
    /** Breaking fully grown crops. Replanted crops are allowed, immature crops do not count. */
    HARVEST_CROP,
    FISH_ITEM,
    EAT_ITEM
}
