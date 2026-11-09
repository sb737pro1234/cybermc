package me.sbpro.cyberquests;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.time.Duration;
import java.util.List;

public final class Messages {

    public static final TextColor CUSTOM = TextColor.color(0x14B8A6);

    public static final Component PREFIX = Component.text()
            .append(Component.text("QUESTS", CUSTOM, TextDecoration.BOLD))
            .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
            .build();

    // Standard errors (unprefixed)
    public static final Component PLAYER_ONLY = Component.text(
            "You must be a player to use this command.",
            NamedTextColor.RED
    );

    public static final Component NO_PERMISSION = Component.text(
            "You do not have permission to use this command.",
            NamedTextColor.RED
    );

    // Command feedback
    public static final Component INVALID_PLAYER = error("That player could not be found.");
    public static final Component INVALID_QUEST = error("That quest could not be found.");
    public static final Component USAGE = error("Invalid command. Use /quests for help.");
    public static final Component USAGE_RESET = error("Usage: /quests reset <player>");
    public static final Component USAGE_GIVE = error("Usage: /quests give <player> <quest>");
    public static final Component NOT_ENOUGH_COINS = error("You do not have enough coins.");
    public static final Component ALREADY_ACTIVE = error("That quest is already active for this player.");
    public static final Component EASIFY_UNAVAILABLE = error("That quest cannot be Easified right now.");

    public static final Component EASY_SUCCESS = PREFIX
            .append(Component.text("Quest made easier for ", NamedTextColor.WHITE))
            .append(Component.text("2 coins", CUSTOM))
            .append(Component.text(".", NamedTextColor.WHITE));

    public static final Component RELOAD_SUCCESS = PREFIX
            .append(Component.text("Configuration reloaded successfully.", NamedTextColor.WHITE));

    // Quest completion
    public static final Component QUEST_COMPLETION_TITLE = Component.text("Quest Complete!", CUSTOM);
    public static final Component QUEST_COMPLETION_BOX_LINE =
            Component.text("-----------------------------------------------", NamedTextColor.DARK_GRAY);
    public static final Duration QUEST_COMPLETION_FADE_IN = Duration.ofMillis(500);
    public static final Duration QUEST_COMPLETION_STAY = Duration.ofMillis(2500);
    public static final Duration QUEST_COMPLETION_FADE_OUT = Duration.ofMillis(500);

    // Boss bar
    public static final BossBar.Color BOSSBAR_COLOR = BossBar.Color.BLUE;
    public static final BossBar.Overlay BOSSBAR_OVERLAY = BossBar.Overlay.PROGRESS;
    public static final long BOSSBAR_DISPLAY_TICKS = 100L;
    /** The Tutorial bar is permanent (until the Tutorial is finished) and has no QUESTS prefix. */
    public static final BossBar.Color TUTORIAL_BOSSBAR_COLOR = BossBar.Color.GREEN;

    // Menu titles
    public static final Component HUB_MENU_TITLE = Component.text("Quests", CUSTOM);
    public static final Component TUTORIAL_MENU_TITLE = Component.text("Tutorial", CUSTOM);
    public static final Component QUEST_LIST_TITLE = Component.text("Active Quests", CUSTOM);
    public static final Component EASIFY_CONFIRM_TITLE = Component.text("Confirm Easify", CUSTOM);

    // Tutorial (early-game progression quests, personal messages only)
    public static final Component QUEST_BOARD_LOCKED = error("Finish the Tutorial to unlock the Quest Board.");
    public static final Component QUEST_BOARD_UNLOCKED = PREFIX
            .append(Component.text("You finished the Tutorial! The ", NamedTextColor.WHITE))
            .append(Component.text("Quest Board", CUSTOM))
            .append(Component.text(" is now unlocked. Open it with ", NamedTextColor.WHITE))
            .append(Component.text("/quests", CUSTOM))
            .append(Component.text(".", NamedTextColor.WHITE));
    public static final Sound TUTORIAL_STEP_SOUND =
            Sound.sound(Key.key("entity.player.levelup"), Sound.Source.MASTER, 0.6f, 1.2f);
    public static final Sound TUTORIAL_FINISHED_SOUND =
            Sound.sound(Key.key("ui.toast.challenge_complete"), Sound.Source.MASTER, 1.0f, 1.0f);

    // Menu items
    public static final Component TUTORIAL_NAME = item("Tutorial", CUSTOM);
    public static final Component TUTORIAL_INFO_NAME = item("Tutorial Quests", CUSTOM);
    public static final List<Component> TUTORIAL_INFO_LORE = List.of(
            lore("Early-game quests that guide you from"),
            lore("your first log to your first iron pickaxe."),
            lore("Complete them in order to earn item rewards."),
            lore(""),
            lore("Finish the Tutorial to unlock the ")
                    .append(Component.text("Quest Board", CUSTOM))
                    .append(Component.text(".", NamedTextColor.WHITE)));
    public static final Component QUEST_BOARD_NAME = item("Quest Board", CUSTOM);
    public static final Component QUEST_BOARD_LOCKED_NAME = item("Quest Board", NamedTextColor.GRAY);
    public static final List<Component> QUEST_BOARD_LORE = List.of(lore("View and reroll your active quests."));
    public static final List<Component> QUEST_BOARD_LOCKED_LORE = List.of(
            lore("View and reroll your active quests."),
            lore(""),
            item("Locked: finish the Tutorial to unlock.", NamedTextColor.RED));
    public static final List<Component> QUEST_BOARD_GOAL_LORE = List.of(
            lore("Unlocked! Open it from the Quests menu."));
    public static final List<Component> QUEST_BOARD_GOAL_LOCKED_LORE = List.of(
            lore("Unlocks when you finish the Tutorial."),
            item("Locked", NamedTextColor.RED));
    public static final Component BACK_NAME = item("Back", NamedTextColor.YELLOW);
    public static final List<Component> BACK_LORE = List.of(lore("Return to the Quests menu."));
    public static final Component REROLL_BUTTON_NAME = item("Easify Quest", NamedTextColor.YELLOW);
    public static final List<Component> REROLL_BUTTON_LORE = List.of(
            lore("Turn this quest into an easier quest for ")
                    .append(Component.text("2 coins", CUSTOM))
                    .append(Component.text(".", NamedTextColor.WHITE)));
    public static final Component SLOT_COOLDOWN_NAME = item("Quest Slot Unavailable", NamedTextColor.RED);
    public static final Component CONFIRM_NAME = item("Confirm", NamedTextColor.GREEN);
    public static final List<Component> CONFIRM_LORE = List.of(lore("Turn this quest into an easier quest."));
    public static final Component CANCEL_NAME = item("Cancel", NamedTextColor.RED);
    public static final List<Component> CANCEL_LORE = List.of(lore("Return to your quests."));

    private Messages() {
    }

    private static Component error(String text) {
        return PREFIX.append(Component.text(text, NamedTextColor.RED));
    }

    /** Item name without the default italics. */
    private static Component item(String text, TextColor color) {
        return Component.text(text, color).decoration(TextDecoration.ITALIC, false);
    }

    /** White item lore line without the default italics. */
    private static Component lore(String text) {
        return Component.text(text, NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false);
    }

    private static Component loreLabel(String label, Component value) {
        return lore(label).append(value.decoration(TextDecoration.ITALIC, false));
    }

    // Tutorial

    /** e.g. QUESTS » You completed quest "Break 16 logs" and received 2 Apples. */
    public static Component tutorialQuestCompleted(String questTitle, String reward) {
        return PREFIX
                .append(Component.text("You completed quest ", NamedTextColor.WHITE))
                .append(Component.text("\"" + questTitle + "\"", CUSTOM))
                .append(Component.text(" and received ", NamedTextColor.WHITE))
                .append(Component.text(reward, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    /** e.g. "Break 16 logs (3/16)" — no QUESTS prefix, that is only for Quest Board bars. */
    public static Component tutorialBossBarTitle(String questTitle, int progress, int required) {
        return Component.text(questTitle, CUSTOM)
                .append(Component.text(" (" + progress + "/" + required + ")", NamedTextColor.WHITE));
    }

    public static Component tutorialNextQuest(String questTitle) {
        return PREFIX
                .append(Component.text("Next quest: ", NamedTextColor.WHITE))
                .append(Component.text(questTitle, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    /**
     * Action bar shown after each Tutorial quest: progress towards unlocking the Quest Board.
     * e.g. "Quest Board ■■■■□□□□□□□□□ 4/13", or "Quest Board Unlocked! ■■■■■■■■■■■■■" at the end.
     */
    public static Component tutorialActionBar(int completed, int total) {
        boolean done = completed >= total;
        Component bar = Component.text("■".repeat(Math.min(completed, total)), NamedTextColor.GREEN)
                .append(Component.text("■".repeat(Math.max(0, total - completed)), NamedTextColor.DARK_GRAY));
        return done
                ? Component.text("Quest Board Unlocked! ", CUSTOM, TextDecoration.BOLD)
                        .append(bar.decoration(TextDecoration.BOLD, false))
                : Component.text("Quest Board ", CUSTOM)
                        .append(bar)
                        .append(Component.text(" " + completed + "/" + total, NamedTextColor.WHITE));
    }

    public static List<Component> tutorialButtonLore(int completed, int total) {
        return List.of(
                lore("Early-game quests with item rewards."),
                loreLabel("Progress: ", Component.text(completed, CUSTOM)
                        .append(Component.text(" / ", NamedTextColor.WHITE))
                        .append(Component.text(total, CUSTOM))));
    }

    /** e.g. "3. Craft a Crafting Table" in the given colour. */
    public static Component tutorialStepName(int number, String questTitle, TextColor color) {
        return item(number + ". " + questTitle, color);
    }

    public static List<Component> tutorialCompletedLore(String description, String reward) {
        return List.of(lore(description), tutorialRewardLine(reward), lore(""), item("Completed", NamedTextColor.GREEN));
    }

    public static List<Component> tutorialCurrentLore(String description, int progress, int required, String reward) {
        return List.of(lore(description), progressLine(progress, required), tutorialRewardLine(reward),
                lore(""), item("Current quest", NamedTextColor.YELLOW));
    }

    public static List<Component> tutorialLockedLore(String description, String reward) {
        return List.of(lore(description), tutorialRewardLine(reward), lore(""), item("Locked", NamedTextColor.RED));
    }

    private static Component tutorialRewardLine(String reward) {
        return loreLabel("Reward: ", Component.text(reward, CUSTOM));
    }

    // Tutorial admin
    public static final Component USAGE_TUTORIAL = error("Usage: /quests tutorial <player> <next|finish|reset>");
    public static final Component BOARD_LOCKED_FOR_PLAYER = error("That player has not unlocked the Quest Board yet.");
    public static final Component TUTORIAL_ALREADY_FINISHED = error("That player has already finished their Tutorial.");

    public static Component tutorialSkipped(String playerName, String questTitle) {
        return PREFIX
                .append(Component.text("Skipped ", NamedTextColor.WHITE))
                .append(Component.text(questTitle, CUSTOM))
                .append(Component.text(" for ", NamedTextColor.WHITE))
                .append(Component.text(playerName, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component tutorialFinished(String playerName) {
        return PREFIX
                .append(Component.text("Finished the Tutorial for ", NamedTextColor.WHITE))
                .append(Component.text(playerName, CUSTOM))
                .append(Component.text(". Their Quest Board is unlocked.", NamedTextColor.WHITE));
    }

    public static Component tutorialReset(String playerName) {
        return PREFIX
                .append(Component.text("Reset the Tutorial for ", NamedTextColor.WHITE))
                .append(Component.text(playerName, CUSTOM))
                .append(Component.text(". Their Quest Board is locked again.", NamedTextColor.WHITE));
    }

    public static Component slotOnCooldown(String time) {
        return PREFIX
                .append(Component.text("That quest slot is on cooldown. It unlocks in ", NamedTextColor.RED))
                .append(Component.text(time, CUSTOM))
                .append(Component.text(".", NamedTextColor.RED));
    }

    public static Component slotCooldownStarted(int slot, String time) {
        return PREFIX
                .append(Component.text("Quest slot ", NamedTextColor.WHITE))
                .append(Component.text(slot, CUSTOM))
                .append(Component.text(" is on cooldown for ", NamedTextColor.WHITE))
                .append(Component.text(time, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component slotUnlocked(int slot, String questTitle) {
        return PREFIX
                .append(Component.text("Quest slot ", NamedTextColor.WHITE))
                .append(Component.text(slot, CUSTOM))
                .append(Component.text(" has unlocked! New quest: ", NamedTextColor.WHITE))
                .append(Component.text(questTitle, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component questGivenToYou(String questTitle) {
        return PREFIX
                .append(Component.text("You received quest: ", NamedTextColor.WHITE))
                .append(Component.text(questTitle, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component resetSuccess(String playerName) {
        return PREFIX
                .append(Component.text("Quest data for ", NamedTextColor.WHITE))
                .append(Component.text(playerName, CUSTOM))
                .append(Component.text(" has been reset.", NamedTextColor.WHITE));
    }

    public static Component giveSuccess(String questTitle, String playerName) {
        return PREFIX
                .append(Component.text("Quest ", NamedTextColor.WHITE))
                .append(Component.text(questTitle, CUSTOM))
                .append(Component.text(" has been given to ", NamedTextColor.WHITE))
                .append(Component.text(playerName, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component questCompletionSubtitle(int reward) {
        return Component.text("+" + reward + " coins", NamedTextColor.WHITE);
    }

    public static Component questCompletionBoxMessage(String questTitle, int reward) {
        return PREFIX
                .append(Component.text("You completed ", NamedTextColor.WHITE))
                .append(Component.text(questTitle, CUSTOM))
                .append(Component.text(" and received ", NamedTextColor.WHITE))
                .append(Component.text("+" + reward + " coins", CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    /** e.g. "QUESTS » Mine 48 iron ore (12/48)" */
    public static Component bossBarTitle(String questTitle, int progress, int required) {
        return PREFIX
                .append(Component.text(questTitle, CUSTOM))
                .append(Component.text(" (" + progress + "/" + required + ")", NamedTextColor.WHITE));
    }

    public static Component questTitle(String questTitle) {
        return item(questTitle, CUSTOM);
    }

    public static Component difficultyLabel(boolean easy) {
        return loreLabel("Difficulty: ", easy
                ? Component.text("Easy", NamedTextColor.GREEN)
                : Component.text("Normal", NamedTextColor.YELLOW));
    }

    public static Component progressLine(int progress, int required) {
        return loreLabel("Progress: ", Component.text(progress, CUSTOM)
                .append(Component.text(" / ", NamedTextColor.WHITE))
                .append(Component.text(required, CUSTOM)));
    }

    public static Component rewardLine(int reward) {
        return loreLabel("Reward: ", Component.text(reward + " coins", CUSTOM));
    }

    public static List<Component> slotCooldownLore(String time) {
        return List.of(
                lore("This quest slot is on cooldown."),
                loreLabel("Unlocks in: ", Component.text(time, CUSTOM)),
                lore("A new Normal quest will be assigned automatically.")
        );
    }
}
