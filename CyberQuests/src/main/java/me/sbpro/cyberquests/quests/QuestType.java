package me.sbpro.cyberquests.quests;

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
    EAT_ITEM,
    /**
     * Running a command. The target is the command label without the slash (and without a "plugin:" namespace),
     * so commands from any plugin are detected, e.g. /rtp, /shop or /sell.
     */
    RUN_COMMAND
}
