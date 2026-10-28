package me.sbpro.cyberquests;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.time.Duration;
import java.util.List;

public final class Messages {

    public static final TextColor CUSTOM = TextColor.color(0x14B8A6);

    private static final TextColor COIN_SHOP = TextColor.color(0xFFD54A);

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

    // Menu titles
    public static final Component QUEST_MENU_TITLE = Component.text("Quests", CUSTOM);
    public static final Component QUEST_LIST_TITLE = Component.text("Active Quests", CUSTOM);
    public static final Component EASIFY_CONFIRM_TITLE = Component.text("Confirm Easify", CUSTOM);

    // Menu items
    public static final Component ACTIVE_QUESTS_NAME = item("Active Quests", CUSTOM);
    public static final List<Component> ACTIVE_QUESTS_LORE = List.of(lore("View your active quests."));
    public static final Component COIN_SHOP_NAME = item("Coin Shop", COIN_SHOP);
    public static final List<Component> COIN_SHOP_LORE = List.of(lore("Open the Coin Shop."));
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

    public static Component bossBarTitle(String questTitle, int progress, int required) {
        return Component.text(questTitle, CUSTOM)
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
