package me.sbpro.cybermastery;

import me.sbpro.cybermastery.model.ActionMath;
import me.sbpro.cybermastery.model.Category;
import me.sbpro.cybermastery.model.Group;
import me.sbpro.cybermastery.model.LevelCurve;
import me.sbpro.cybermastery.model.MasteryType;
import me.sbpro.cybermastery.util.AmountFormatter;
import me.sbpro.cybermastery.util.ProgressBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Sound;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * All player-facing CyberMastery messages, titles, colours, sounds and GUI text live here.
 *
 * Messages are Adventure Components. Main text is white and highlighted values use {@link #CUSTOM}.
 */
public final class Messages {

    private Messages() {
    }

    // =============================================================
    // Colours
    // =============================================================

    /**
     * Main CyberMastery highlight/title colour.
     * Change this one value to change the highlight colour everywhere.
     * Current colour: #00AAFF
     */
    public static final TextColor CUSTOM = TextColor.color(0x00AAFF);

    private static final String SEPARATOR = "-----------------------------------------------";

    /** Progress bar look. */
    private static final int BAR_LENGTH = 20;
    private static final String BAR_FILLED = "|";
    private static final String BAR_EMPTY = "|";
    private static final TextColor BAR_EMPTY_COLOR = NamedTextColor.DARK_GRAY;

    // =============================================================
    // Sounds
    // =============================================================

    /** Normal level-ups (not every 5th level). */
    public static final Sound SMALL_LEVEL_SOUND = Sound.BLOCK_NOTE_BLOCK_PLING;
    public static final float SMALL_LEVEL_PITCH = 1.5f;
    /** Every 5th level. */
    public static final Sound LEVEL_UP_SOUND = Sound.ENTITY_PLAYER_LEVELUP;
    /** Level 45. */
    public static final Sound MAX_LEVEL_SOUND = Sound.UI_TOAST_CHALLENGE_COMPLETE;
    public static final Sound CLAIM_SOUND = Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
    public static final Sound ERROR_SOUND = Sound.ENTITY_VILLAGER_NO;
    /** Played when pressing a Back button. */
    public static final Sound BACK_SOUND = Sound.UI_BUTTON_CLICK;
    /** Played when opening a category from /mastery. */
    public static final Sound CATEGORY_SOUND = Sound.UI_BUTTON_CLICK;
    public static final float SOUND_VOLUME = 1.0f;
    public static final float SOUND_PITCH = 1.0f;

    // =============================================================
    // Prefix / general messages
    // =============================================================

    public static final Component PREFIX = Component.text()
            .append(Component.text("MASTERY", CUSTOM, TextDecoration.BOLD))
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

    public static Component usage(boolean admin) {
        if (!admin) {
            return PREFIX
                    .append(Component.text("Usage: /mastery", NamedTextColor.RED));
        }

        return Component.text(SEPARATOR + "\n", NamedTextColor.DARK_GRAY)
                .append(PREFIX).append(white("Admin commands:\n"))
                .append(usageLine("/mastery reload", "Reload config.yml and mastery.yml.")).append(Component.newline())
                .append(usageLine("/mastery give <player> <type> <levels>", "Give levels (with level-up effects).")).append(Component.newline())
                .append(usageLine("/mastery set <player> <type> <level>", "Set a type's level (no effects).")).append(Component.newline())
                .append(usageLine("/mastery reset <player> [type]", "Reset one type, or everything.")).append(Component.newline())
                .append(usageLine("/mastery check", "Is the block you're looking at player-placed?")).append(Component.newline())
                .append(Component.text(SEPARATOR, NamedTextColor.DARK_GRAY));
    }

    /** Usage of one subcommand, e.g. "Usage: /mastery give <player> <type> <points>" plus an example. */
    public static Component subUsage(String usage, String example) {
        return PREFIX
                .append(Component.text("Usage: " + usage, NamedTextColor.RED))
                .append(Component.newline())
                .append(PREFIX)
                .append(white("Example: "))
                .append(custom(example));
    }

    public static final String GIVE_USAGE = "/mastery give <player> <type> <levels>";
    public static final String GIVE_EXAMPLE = "/mastery give SB737Pro1234 acacia 1";
    public static final String SET_USAGE = "/mastery set <player> <type> <level>";
    public static final String SET_EXAMPLE = "/mastery set SB737Pro1234 diamond_ore 44";
    public static final String RESET_USAGE = "/mastery reset <player> [type]";
    public static final String RESET_EXAMPLE = "/mastery reset SB737Pro1234 stone";

    private static Component usageLine(String command, String description) {
        return bullet(custom(command)).append(white(" - " + description));
    }

    // =============================================================
    // Admin commands
    // =============================================================

    public static Component playerNotOnline() {
        return PREFIX
                .append(Component.text("That player is not online.", NamedTextColor.RED));
    }

    public static Component unknownType(String id) {
        return PREFIX
                .append(Component.text("Unknown type '" + id + "'. Use the ids from mastery.yml, e.g. stone.", NamedTextColor.RED));
    }

    public static Component invalidLevelAmount() {
        return PREFIX
                .append(Component.text("Levels must be a whole number from 1 to " + LevelCurve.MAX_LEVEL + ".", NamedTextColor.RED));
    }

    public static Component alreadyMaxLevel(String playerName, MasteryType type) {
        return PREFIX
                .append(Component.text(playerName + " is already level " + LevelCurve.MAX_LEVEL + " in " + type.name() + ".",
                        NamedTextColor.RED));
    }

    public static Component levelsGiven(String playerName, MasteryType type, int levels, int level) {
        return PREFIX
                .append(white("Gave "))
                .append(custom(levels + (levels == 1 ? " level" : " levels")))
                .append(white(" of "))
                .append(custom(type.name()))
                .append(white(" to "))
                .append(custom(playerName))
                .append(white(". They are now level "))
                .append(custom(level + "/" + LevelCurve.MAX_LEVEL))
                .append(white("."));
    }

    public static Component invalidLevel() {
        return PREFIX
                .append(Component.text("Level must be a whole number from 0 to " + LevelCurve.MAX_LEVEL + ".", NamedTextColor.RED));
    }

    public static Component levelSet(String playerName, MasteryType type, int level) {
        return PREFIX
                .append(white("Set "))
                .append(custom(playerName))
                .append(white("'s "))
                .append(custom(type.name()))
                .append(white(" to level "))
                .append(custom(level + "/" + LevelCurve.MAX_LEVEL))
                .append(white("."));
    }

    public static Component typeReset(String playerName, MasteryType type) {
        return PREFIX
                .append(white("Reset "))
                .append(custom(playerName))
                .append(white("'s "))
                .append(custom(type.name()))
                .append(white(" progress and rewards."));
    }

    public static Component allReset(String playerName) {
        return PREFIX
                .append(white("Reset "))
                .append(custom("all"))
                .append(white(" of "))
                .append(custom(playerName))
                .append(white("'s mastery progress and rewards."));
    }

    public static Component checkNoBlock() {
        return PREFIX
                .append(Component.text("Look at a block within 6 blocks.", NamedTextColor.RED));
    }

    /** /mastery check result. type may be null when the block isn't tracked. */
    public static Component checkResult(String blockName, MasteryType type, boolean placed, boolean credited) {
        Component typeText = type == null
                ? white("not tracked (gives nothing)")
                : custom(type.name()).append(white(" (" + categoryName(type.category()) + ")"));

        return Component.text(SEPARATOR + "\n", NamedTextColor.DARK_GRAY)
                .append(PREFIX).append(white("Block: ")).append(custom(blockName)).append(Component.newline())
                .append(bullet(white("Type: "))).append(typeText).append(Component.newline())
                .append(bullet(white("Player-placed: "))).append(custom(placed ? "Yes" : "No"))
                .append(white(placed ? " (mining gives nothing)" : " (mining gives points)")).append(Component.newline())
                .append(bullet(white("Placement points used: "))).append(custom(credited ? "Yes" : "No"))
                .append(white(credited ? " (placing here gives nothing)" : "")).append(Component.newline())
                .append(Component.text(SEPARATOR, NamedTextColor.DARK_GRAY));
    }

    public static Component reloadSuccess(int types) {
        return PREFIX
                .append(Component.text("Configuration reloaded successfully. ", NamedTextColor.GREEN))
                .append(white("Loaded "))
                .append(custom(types + " types"))
                .append(white("."));
    }

    public static Component reloadFailed() {
        return PREFIX
                .append(Component.text("Reload failed. Check the console for errors.", NamedTextColor.RED));
    }

    // =============================================================
    // Names
    // =============================================================

    public static String categoryName(Category category) {
        return switch (category) {
            case BLOCKS -> "Blocks";
            case SLAYING -> "Slaying";
            case FARMING -> "Farming";
            case FORAGING -> "Foraging";
            case FISHING -> "Fishing";
        };
    }

    // =============================================================
    // Level-up feedback
    // =============================================================

    /** Every level: "MASTERY » Reached Stone 6!" */
    public static Component levelUpActionBar(MasteryType type, int level) {
        return PREFIX
                .append(white("Reached "))
                .append(custom(type.name() + " " + level))
                .append(white("!"));
    }

    /** Every 5th level. */
    public static Title milestoneTitle(MasteryType type, int level) {
        return Title.title(
                Component.text("LEVEL " + level, CUSTOM, TextDecoration.BOLD),
                custom(type.name())
                        .append(white(" mastery " + level + "/" + LevelCurve.MAX_LEVEL + " reached!")),
                titleTimes()
        );
    }

    public static Component milestoneChat(MasteryType type, int level) {
        return PREFIX
                .append(white("You reached "))
                .append(custom(type.name() + " " + level))
                .append(white("! Claim your rewards in "))
                .append(custom("/mastery"))
                .append(white("."));
    }

    /** Level 45. */
    public static Title maxLevelTitle(MasteryType type) {
        return Title.title(
                Component.text("MAX LEVEL", CUSTOM, TextDecoration.BOLD),
                custom(type.name())
                        .append(white(" mastered! (" + LevelCurve.MAX_LEVEL + "/" + LevelCurve.MAX_LEVEL + ")")),
                titleTimes()
        );
    }

    /** Sent to everyone when a player reaches level 45. */
    public static Component maxLevelBroadcast(String playerName, MasteryType type) {
        return Component.text(SEPARATOR + "\n", NamedTextColor.DARK_GRAY)
                .append(PREFIX)
                .append(custom(playerName))
                .append(white(" reached level "))
                .append(custom(String.valueOf(LevelCurve.MAX_LEVEL)))
                .append(white(" in "))
                .append(custom(type.name()))
                .append(white("!\n"))
                .append(Component.text(SEPARATOR, NamedTextColor.DARK_GRAY));
    }

    private static Title.Times titleTimes() {
        return Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(2500), Duration.ofMillis(750));
    }

    // =============================================================
    // Claiming
    // =============================================================

    public static Component rewardClaimed(MasteryType type, int level) {
        return PREFIX
                .append(white("You claimed the "))
                .append(custom(type.name() + " " + level))
                .append(white(" reward."));
    }

    public static Component rewardsClaimed(int count) {
        return PREFIX
                .append(white("You claimed "))
                .append(custom(count + (count == 1 ? " reward" : " rewards")))
                .append(white("."));
    }

    public static Component nothingToClaim() {
        return PREFIX
                .append(Component.text("You have no rewards to claim.", NamedTextColor.RED));
    }

    public static Component levelNotReached() {
        return PREFIX
                .append(Component.text("You haven't reached that level yet.", NamedTextColor.RED));
    }

    // =============================================================
    // Shared GUI pieces
    // =============================================================

    /** e.g. ||||||||||||........ 60% */
    public static Component progressBar(double fraction) {
        int filled = ProgressBar.filled(fraction, BAR_LENGTH);

        return Component.text(BAR_FILLED.repeat(filled), CUSTOM)
                .append(Component.text(BAR_EMPTY.repeat(BAR_LENGTH - filled), BAR_EMPTY_COLOR))
                .append(white(" " + ProgressBar.percent(fraction) + "%"));
    }

    public static Component previousPageName() {
        return Component.text("Previous Page", CUSTOM, TextDecoration.BOLD);
    }

    public static List<Component> previousPageLore(int page) {
        return List.of(white("Go to page ").append(custom(String.valueOf(page))).append(white(".")));
    }

    public static Component nextPageName() {
        return Component.text("Next Page", CUSTOM, TextDecoration.BOLD);
    }

    public static List<Component> nextPageLore(int page) {
        return List.of(white("Go to page ").append(custom(String.valueOf(page))).append(white(".")));
    }

    public static Component backName() {
        return Component.text("Back", CUSTOM, TextDecoration.BOLD);
    }

    public static List<Component> backLore(String to) {
        return List.of(white("Go back to ").append(custom(to)).append(white(".")));
    }

    public static String mainMenuName() {
        return "Mastery";
    }

    public static Component claimAllName() {
        return Component.text("Claim All", CUSTOM, TextDecoration.BOLD);
    }

    public static List<Component> claimAllLore(int ready, boolean everything) {
        List<Component> lore = new ArrayList<>();

        lore.add(white(everything
                ? "Claim every ready reward from every type."
                : "Claim every ready reward of this type."));
        lore.add(Component.empty());

        if (ready > 0) {
            lore.add(white("Rewards ready: ").append(custom(String.valueOf(ready))));
            lore.add(Component.empty());
            lore.add(Component.text("Click to claim all!", CUSTOM, TextDecoration.BOLD));
        } else {
            lore.add(white("No rewards to claim right now."));
        }

        return lore;
    }

    // =============================================================
    // /mastery menu
    // =============================================================

    public static Component mainMenuTitle() {
        return Component.text("MASTERY", CUSTOM, TextDecoration.BOLD);
    }

    public static Component totalItemName() {
        return Component.text("Total Mastery", CUSTOM, TextDecoration.BOLD);
    }

    /** Total levels across every type, e.g. "Levels: 44/5,490", then each category's levels. */
    public static List<Component> totalItemLore(
            long total,
            long totalMax,
            Map<Category, Long> levels,
            Map<Category, Long> maxLevels
    ) {
        List<Component> lore = new ArrayList<>();

        lore.add(white("Levels: ").append(custom(total + "/" + totalMax)));
        lore.add(progressBar(ProgressBar.fraction(total, totalMax)));
        lore.add(Component.empty());

        for (Category category : Category.values()) {
            lore.add(bullet(white(categoryName(category) + ": "))
                    .append(custom(levels.get(category) + "/" + maxLevels.get(category))));
        }

        lore.add(Component.empty());
        lore.add(white("All your type levels added up."));

        return lore;
    }

    public static Component categoryItemName(Category category) {
        return Component.text(categoryName(category), CUSTOM, TextDecoration.BOLD);
    }

    public static List<Component> categoryItemLore(
            Category category,
            long levels,
            long maxLevels,
            int types,
            int ready
    ) {
        List<Component> lore = new ArrayList<>();

        lore.add(white(categoryDescription(category)));
        lore.add(Component.empty());
        lore.add(white("Levels: ").append(custom(levels + "/" + maxLevels)));
        lore.add(progressBar(ProgressBar.fraction(levels, maxLevels)));
        lore.add(white("Types: ").append(custom(String.valueOf(types))));

        if (ready > 0) {
            lore.add(white("Rewards ready: ").append(custom(String.valueOf(ready))));
        }

        lore.add(Component.empty());
        lore.add(white("Click to view."));

        return lore;
    }

    private static String categoryDescription(Category category) {
        return switch (category) {
            case BLOCKS -> "Mine natural blocks.";
            case SLAYING -> "Kill mobs.";
            case FARMING -> "Harvest fully grown crops.";
            case FORAGING -> "Chop natural logs.";
            case FISHING -> "Catch fish.";
        };
    }

    public static Component howMasteryWorksName() {
        return Component.text("How Mastery Works", CUSTOM, TextDecoration.BOLD);
    }

    public static List<Component> howMasteryWorksLore() {
        return List.of(
                white("Every type has levels ").append(custom("0 to 45")).append(white(".")),
                white("A type is one block, mob, crop, wood or fish."),
                Component.empty(),
                custom("Five categories:"),
                bullet(custom("Blocks")).append(white(" - mining natural blocks")),
                bullet(custom("Slaying")).append(white(" - killing mobs")),
                bullet(custom("Farming")).append(white(" - harvesting grown crops")),
                bullet(custom("Foraging")).append(white(" - chopping natural logs")),
                bullet(custom("Fishing")).append(white(" - catching fish")),
                Component.empty(),
                custom("Types:"),
                bullet(white("Variants count as their main type,")),
                white("  e.g. stone brick stairs count as ").append(custom("Stone Bricks")).append(white(".")),
                bullet(white("Blocks types are sorted into groups")),
                white("  like ").append(custom("Stones")).append(white(" and ")).append(custom("Ores")).append(white(".")),
                bullet(white("Rarer types need fewer actions per level,")),
                white("  so every type takes similar effort."),
                bullet(white("Each type shows how many you've done")),
                white("  this level, e.g. ").append(custom("Mine Stone: 5/15")).append(white(".")),
                Component.empty(),
                custom("Total Mastery:"),
                bullet(white("All your type levels added up.")),
                Component.empty(),
                custom("Levels and rewards:"),
                bullet(white("Every 5th level shows a title.")),
                bullet(white("Level 45 is announced to everyone.")),
                bullet(white("Claim each level's reward in the type's menu.")),
                bullet(white("Rewards are coming soon!"))
        );
    }

    public static Component topButtonName() {
        return Component.text("Top Mastery", CUSTOM, TextDecoration.BOLD);
    }

    public static List<Component> topButtonLore() {
        return List.of(
                Component.text("View the players with the most mastery levels.", NamedTextColor.WHITE)
        );
    }

    // =============================================================
    // /mastery top menu
    // =============================================================

    public static Component topMasteryMenuTitle() {
        return Component.text("TOP MASTERY", CUSTOM, TextDecoration.BOLD);
    }

    public static Component topMasteryPlayerName(String playerName, int position) {
        return Component.text(playerName, NamedTextColor.WHITE)
                .append(Component.text(" (", NamedTextColor.WHITE))
                .append(Component.text("#", CUSTOM))
                .append(Component.text(position, CUSTOM))
                .append(Component.text(")", NamedTextColor.WHITE));
    }

    public static Component topMasteryPlayerNameUnranked(String playerName) {
        return Component.text(playerName, NamedTextColor.WHITE);
    }

    public static List<Component> topMasteryPlayerLore(long levels) {
        return List.of(
                Component.text("Levels: ", NamedTextColor.WHITE)
                        .append(Component.text(AmountFormatter.format(levels), CUSTOM))
        );
    }

    // =============================================================
    // Category / group menus
    // =============================================================

    public static Component categoryMenuTitle(Category category, int page, int pages) {
        return pagedTitle(categoryName(category), page, pages);
    }

    public static Component groupMenuTitle(Group group, int page, int pages) {
        return pagedTitle(group.name(), page, pages);
    }

    private static Component pagedTitle(String name, int page, int pages) {
        Component title = Component.text(name.toUpperCase(Locale.ROOT), CUSTOM, TextDecoration.BOLD);
        if (pages > 1) {
            title = title.append(Component.text(" (" + (page + 1) + "/" + pages + ")", NamedTextColor.DARK_GRAY));
        }
        return title;
    }

    public static Component groupItemName(Group group) {
        return Component.text(group.name(), CUSTOM, TextDecoration.BOLD);
    }

    /** e.g. Stones: 68/405 */
    public static List<Component> groupItemLore(Group group, long levels, long maxLevels, int ready) {
        List<Component> lore = new ArrayList<>();

        lore.add(white(group.name() + ": ").append(custom(levels + "/" + maxLevels)).append(white(" levels")));
        lore.add(progressBar(ProgressBar.fraction(levels, maxLevels)));
        lore.add(white("Types: ").append(custom(String.valueOf(group.types().size()))));

        if (ready > 0) {
            lore.add(white("Rewards ready: ").append(custom(String.valueOf(ready))));
        }

        lore.add(Component.empty());
        lore.add(white("Click to view."));

        return lore;
    }

    public static Component typeItemName(MasteryType type) {
        return Component.text(type.name(), CUSTOM, TextDecoration.BOLD);
    }

    /**
     * Type button lore, e.g. "Mine Stone: 5/15" for this level.
     *
     * @param placePercent how much placing counts (0 if placing gives nothing)
     */
    public static List<Component> typeItemLore(
            MasteryType type,
            int level,
            ActionMath.LevelProgress progress,
            int placePercent,
            int ready
    ) {
        List<Component> lore = new ArrayList<>();

        lore.add(white("Level: ").append(custom(level + "/" + LevelCurve.MAX_LEVEL)));

        if (level >= LevelCurve.MAX_LEVEL) {
            lore.add(progressBar(1.0));
            lore.add(Component.text("MAX LEVEL", CUSTOM, TextDecoration.BOLD));
        } else {
            lore.add(progressBar(progress.fraction()));
            lore.add(actionLine(type, progress.done(), progress.need()));
        }

        lore.add(Component.empty());
        lore.add(custom("What counts:"));
        lore.addAll(typeCounts(type, placePercent));

        if (type.includes() != null && !type.includes().isBlank()) {
            lore.add(bullet(white("Includes: ")).append(custom(type.includes())));
        }

        if (ready > 0) {
            lore.add(Component.empty());
            lore.add(white("Rewards ready: ").append(custom(String.valueOf(ready))));
        }

        lore.add(Component.empty());
        lore.add(white("Click to view all 45 levels."));

        return lore;
    }

    /** e.g. "Mine Stone: 5/15", "Kill Zombies: 3/10", "Catch Salmon: 1/4". */
    private static Component actionLine(MasteryType type, long done, long need) {
        return white(actionVerb(type.category()) + " " + actionNoun(type) + ": ")
                .append(custom(AmountFormatter.format(done) + "/" + AmountFormatter.format(need)));
    }

    private static String actionVerb(Category category) {
        return switch (category) {
            case BLOCKS -> "Mine";
            case SLAYING -> "Kill";
            case FARMING -> "Harvest";
            case FORAGING -> "Chop";
            case FISHING -> "Catch";
        };
    }

    /** Mobs read as plurals ("Kill Zombies"); blocks, crops, wood and fish stay as they are. */
    private static String actionNoun(MasteryType type) {
        String name = type.name();

        if (type.category() != Category.SLAYING) {
            return name;
        }

        String lower = name.toLowerCase(Locale.ROOT);

        if (lower.endsWith("sheep") || lower.endsWith("fish") || lower.endsWith("drowned") || lower.endsWith("squid")) {
            return name;
        }
        if (lower.endsWith("man")) {
            return name.substring(0, name.length() - 3) + "men";
        }
        if (lower.endsWith("ch") || lower.endsWith("sh") || lower.endsWith("s") || lower.endsWith("x")) {
            return name + "es";
        }
        return name + "s";
    }

    /** 25 -> "¼", 50 -> "½", 75 -> "¾", 100 -> "1", anything else as a percent. */
    private static String placeShare(int percent) {
        return switch (percent) {
            case 25 -> "¼";
            case 50 -> "½";
            case 75 -> "¾";
            case 100 -> "1";
            default -> percent + "%";
        };
    }

    private static List<Component> typeCounts(MasteryType type, int placePercent) {
        List<Component> lines = new ArrayList<>();

        switch (type.category()) {
            case BLOCKS -> {
                lines.add(bullet(white("Mining natural " + type.name() + ".")));
                if (placePercent > 0) {
                    lines.add(bullet(white("Placing counts as "))
                            .append(custom(placeShare(placePercent)))
                            .append(white(placePercent >= 100 ? " block," : " of a block,")));
                    lines.add(white("  once per spot."));
                }
                lines.add(bullet(white("Mining placed blocks gives nothing.")));
            }
            case SLAYING -> {
                lines.add(bullet(white("Killing adult " + actionNoun(type) + ".")));
                lines.add(bullet(white("Spawner mobs count.")));
            }
            case FARMING -> {
                lines.add(bullet(white("Harvesting fully grown " + type.name() + ".")));
            }
            case FORAGING -> {
                lines.add(bullet(white("Chopping natural " + type.name() + " logs.")));
                lines.add(bullet(white("Placed logs give nothing.")));
            }
            case FISHING -> {
                lines.add(bullet(white("Catching " + type.name() + " with a rod.")));
            }
        }

        return lines;
    }

    public static Component howCategoryWorksName(Category category) {
        return Component.text("How " + categoryName(category) + " Works", CUSTOM, TextDecoration.BOLD);
    }

    public static List<Component> howCategoryWorksLore(Category category, Settings settings) {
        List<Component> lore = new ArrayList<>();

        lore.add(custom("How you level up:"));

        switch (category) {
            case BLOCKS -> {
                lore.add(bullet(white("Mine natural blocks.")));
                if (settings.placementEnabled() && settings.placementPercent() > 0) {
                    lore.add(bullet(white("Placing a block counts as "))
                            .append(custom(placeShare(settings.placementPercent())))
                            .append(white(settings.placementPercent() >= 100 ? " block," : " of a block,")));
                    lore.add(white("  only once per spot, ever."));
                    if (settings.hourlyCap() > 0) {
                        lore.add(bullet(white("Placing is limited each hour.")));
                    }
                } else {
                    lore.add(bullet(white("Placing blocks gives nothing.")));
                }
                lore.add(bullet(white("Mining a block a player placed")));
                lore.add(white("  gives nothing."));
                lore.add(bullet(white("Stone and basalt from generators")));
                lore.add(white("  count."));
                lore.add(bullet(white("Logs are ")).append(custom("Foraging")).append(white(", melons and")));
                lore.add(white("  pumpkins are ").append(custom("Farming")).append(white(".")));
            }
            case SLAYING -> {
                lore.add(bullet(white("Kill mobs.")));
                lore.add(bullet(white("Mobs from spawners count.")));
                lore.add(bullet(white("Baby mobs and players give nothing.")));
                lore.add(bullet(white("Bosses level up the fastest.")));
            }
            case FARMING -> {
                lore.add(bullet(white("Break crops when ")).append(custom("fully grown")).append(white(".")));
                lore.add(bullet(white("Crops you planted count once grown.")));
                lore.add(bullet(white("Sugar cane, cactus, bamboo, kelp:")));
                lore.add(white("  every grown piece counts, but not"));
                lore.add(white("  the piece you placed."));
                lore.add(bullet(white("Melons and pumpkins from stems")));
                lore.add(white("  count. Placed ones give nothing."));
                lore.add(bullet(white("Sweet berries count when picked")));
                lore.add(white("  or broken fully grown."));
            }
            case FORAGING -> {
                lore.add(bullet(white("Chop natural logs, stems, wood")));
                lore.add(white("  and hyphae."));
                lore.add(bullet(white("Trees grown from saplings count.")));
                lore.add(bullet(white("Placed logs give nothing, and")));
                lore.add(white("  placing logs gives nothing."));
                lore.add(bullet(white("Planks count under ")).append(custom("Blocks")).append(white(".")));
            }
            case FISHING -> {
                lore.add(bullet(white("Catch fish with a fishing rod.")));
                lore.add(bullet(white("Only cod, salmon, pufferfish and")));
                lore.add(white("  tropical fish count."));
                lore.add(bullet(white("Treasure and junk give nothing.")));
            }
        }

        lore.add(Component.empty());
        lore.add(custom("Levels:"));
        lore.add(bullet(white("Each level needs more than the last.")));
        lore.add(bullet(white("Rarer types need fewer actions")));
        lore.add(white("  per level."));
        lore.add(bullet(white("Each type shows exactly how many")));
        lore.add(white("  you need for its next level."));

        return lore;
    }

    // =============================================================
    // Type menu (all 45 levels)
    // =============================================================

    public static Component typeMenuTitle(MasteryType type) {
        return Component.text(type.name().toUpperCase(Locale.ROOT), CUSTOM, TextDecoration.BOLD);
    }

    public static final Component REWARD_COMING_SOON = white("Coming soon");

    public static Component levelItemName(int level) {
        return Component.text("Level " + level, CUSTOM, TextDecoration.BOLD);
    }

    /**
     * Shared level lines. done / need are totals from level 0, e.g. "Mine Stone: 260/420"
     * for level 5 (done is capped at need once the level is reached).
     */
    private static List<Component> levelInfo(MasteryType type, int level, long done, long need, Component reward) {
        List<Component> lore = new ArrayList<>();
        lore.add(white("Level: ").append(custom(level + "/" + LevelCurve.MAX_LEVEL)));
        lore.add(actionLine(type, Math.min(done, need), need));
        lore.add(white("Reward: ").append(reward == null ? REWARD_COMING_SOON : reward));
        return lore;
    }

    /** Chest minecart: reached, not claimed. */
    public static List<Component> levelReadyLore(MasteryType type, int level, long done, long need, Component reward) {
        List<Component> lore = levelInfo(type, level, done, need, reward);
        lore.add(Component.empty());
        lore.add(Component.text("Click to claim!", CUSTOM, TextDecoration.BOLD));
        return lore;
    }

    /** Empty minecart: claimed. */
    public static List<Component> levelClaimedLore(MasteryType type, int level, long done, long need, Component reward) {
        List<Component> lore = levelInfo(type, level, done, need, reward);
        lore.add(Component.empty());
        lore.add(white("Claimed"));
        return lore;
    }

    /** Barrier: not reached yet. */
    public static List<Component> levelLockedLore(MasteryType type, int level, long done, long need, Component reward) {
        List<Component> lore = levelInfo(type, level, done, need, reward);
        lore.add(Component.empty());
        lore.add(white("Not reached yet."));
        return lore;
    }

    /**
     * Summary item in the type menu.
     *
     * @param totalDone total actions done so far (e.g. stone mined)
     */
    public static List<Component> typeSummaryLore(
            MasteryType type,
            int level,
            ActionMath.LevelProgress progress,
            long totalDone,
            int placePercent
    ) {
        List<Component> lore = new ArrayList<>();

        lore.add(white("Level: ").append(custom(level + "/" + LevelCurve.MAX_LEVEL)));

        if (level >= LevelCurve.MAX_LEVEL) {
            lore.add(progressBar(1.0));
            lore.add(Component.text("MAX LEVEL", CUSTOM, TextDecoration.BOLD));
        } else {
            lore.add(progressBar(progress.fraction()));
            lore.add(actionLine(type, progress.done(), progress.need()));
        }

        lore.add(white(totalLabel(type) + ": ").append(custom(AmountFormatter.format(totalDone))));

        lore.add(Component.empty());
        lore.add(custom("What counts:"));
        lore.addAll(typeCounts(type, placePercent));

        if (type.includes() != null && !type.includes().isBlank()) {
            lore.add(bullet(white("Includes: ")).append(custom(type.includes())));
        }

        return lore;
    }

    /** e.g. "Stone mined", "Zombies killed", "Wheat harvested". */
    private static String totalLabel(MasteryType type) {
        String noun = actionNoun(type);
        return switch (type.category()) {
            case BLOCKS -> noun + " mined";
            case SLAYING -> noun + " killed";
            case FARMING -> noun + " harvested";
            case FORAGING -> noun + " chopped";
            case FISHING -> noun + " caught";
        };
    }

    // =============================================================
    // Helpers
    // =============================================================

    private static TextComponent white(String text) {
        return Component.text(text, NamedTextColor.WHITE);
    }

    private static TextComponent custom(String text) {
        return Component.text(text, CUSTOM);
    }

    /** "✦ " in CUSTOM, followed by the given text. */
    private static Component bullet(Component text) {
        return Component.text("✦ ", CUSTOM).append(text);
    }
}
