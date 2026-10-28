package me.sbpro.cyber;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.List;

public final class Messages {

    public static final TextColor CUSTOM = TextColor.color(0x00AAFF);

    public static final Component PREFIX = Component.text()
            .append(Component.text("CYBER", CUSTOM, TextDecoration.BOLD))
            .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
            .build();

    public static final Component ADMIN_PREFIX = Component.text()
            .append(Component.text("ADMIN", TextColor.color(0xEF4444), TextDecoration.BOLD))
            .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
            .build();

    public static final Component STAFF_PREFIX = Component.text()
            .append(Component.text("STAFF", TextColor.color(0x2979FF), TextDecoration.BOLD))
            .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
            .build();

    public static final Component PLAYER_ONLY = Component.text(
            "You must be a player to use this command.",
            NamedTextColor.RED
    );

    public static final Component NO_PERMISSION = Component.text(
            "You do not have permission to use this command.",
            NamedTextColor.RED
    );

    public static Component joinMessage(Component displayName) {
        return Component.text("[", NamedTextColor.DARK_GRAY)
                .append(Component.text("+", NamedTextColor.GREEN, TextDecoration.BOLD))
                .append(Component.text("] ", NamedTextColor.DARK_GRAY))
                .append(displayName.colorIfAbsent(NamedTextColor.WHITE));
    }

    public static Component firstJoinWelcome(Component displayName) {
        return PREFIX
                .append(displayName.colorIfAbsent(NamedTextColor.WHITE).decorate(TextDecoration.BOLD))
                .append(Component.text(" ", NamedTextColor.WHITE, TextDecoration.BOLD))
                .append(Component.text("has joined the server for the first time! Welcome!", NamedTextColor.WHITE));
    }

    public static Component deathMessage(String victim, String killer) {
        return Component.text("[", NamedTextColor.DARK_GRAY)
                .append(Component.text("⚔", NamedTextColor.DARK_RED, TextDecoration.BOLD))
                .append(Component.text("] ", NamedTextColor.DARK_GRAY))
                .append(Component.text(victim, NamedTextColor.DARK_RED))
                .append(Component.text(" ", NamedTextColor.DARK_RED))
                .append(Component.text("was killed by ", NamedTextColor.WHITE))
                .append(Component.text(killer, NamedTextColor.DARK_RED))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component quitMessage(Component displayName) {
        return Component.text("[", NamedTextColor.DARK_GRAY)
                .append(Component.text("-", NamedTextColor.RED, TextDecoration.BOLD))
                .append(Component.text("] ", NamedTextColor.DARK_GRAY))
                .append(displayName.colorIfAbsent(NamedTextColor.WHITE));
    }

    // /clear
    public static final Component SPECIFY_PLAYER = ADMIN_PREFIX
            .append(Component.text("You must specify a player to use this command.", NamedTextColor.RED));

    public static final Component NO_PERMISSION_CLEAR_OTHERS = ADMIN_PREFIX
            .append(Component.text("You do not have permission to clear other players.", NamedTextColor.RED));

    public static final Component PLAYER_NOT_ONLINE = Component.text("That player is not online.", NamedTextColor.RED);

    public static final Component USAGE_CLEAR = Component.text("Usage: /clear [player]", NamedTextColor.RED);

    public static Component inventoryClearedOther(String target) {
        return ADMIN_PREFIX
                .append(Component.text(target, CUSTOM))
                .append(Component.text("'s inventory has been cleared.", NamedTextColor.WHITE));
    }

    public static Component inventoryClearedByStaff(String sender) {
        return Component.text("Your inventory has been cleared by ", NamedTextColor.WHITE)
                .append(Component.text(sender, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static final Component CLEAR_CONFIRM_TITLE = Component.text("Clear Confirm", CUSTOM, TextDecoration.BOLD);

    public static final Component CONFIRM = Component.text("Confirm", NamedTextColor.GREEN)
            .decoration(TextDecoration.ITALIC, false);

    public static final Component CLEAR_SELF_ITEM_LORE = Component.text("This will clear all your items.", NamedTextColor.WHITE)
            .decoration(TextDecoration.ITALIC, false);

    public static Component clearOtherItemLore(String target) {
        return Component.text("This will clear all ", NamedTextColor.WHITE)
                .append(Component.text(target, CUSTOM))
                .append(Component.text("'s items.", NamedTextColor.WHITE))
                .decoration(TextDecoration.ITALIC, false);
    }

    public static final Component CLEAR_SELF_INFO_LORE = Component.text("Clicking confirm will clear ", NamedTextColor.WHITE)
            .append(Component.text("your", CUSTOM))
            .append(Component.text(" inventory.", NamedTextColor.WHITE))
            .decoration(TextDecoration.ITALIC, false);

    public static Component clearOtherInfoLore(String target) {
        return Component.text("Clicking confirm will clear ", NamedTextColor.WHITE)
                .append(Component.text(target, CUSTOM))
                .append(Component.text("'s inventory.", NamedTextColor.WHITE))
                .decoration(TextDecoration.ITALIC, false);
    }

    public static final Component CANCEL = Component.text("Cancel", NamedTextColor.RED)
            .decoration(TextDecoration.ITALIC, false);

    public static final Component PLAYER_NO_LONGER_ONLINE = Component.text("That player is no longer online.", NamedTextColor.RED);

    public static final Component INVENTORY_CLEARED_SELF = ADMIN_PREFIX
            .append(Component.text("Your inventory has been cleared.", NamedTextColor.WHITE));

    public static Component inventoryClearedByAdmin(String player) {
        return Component.text("Your inventory has been cleared by ", NamedTextColor.WHITE)
                .append(Component.text(player, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    // /feed
    public static final Component NO_PERMISSION_FEED_OTHERS = ADMIN_PREFIX
            .append(Component.text("You do not have permission to feed other players.", NamedTextColor.RED));

    public static final Component USAGE_FEED = Component.text("Usage: /feed [player]", NamedTextColor.RED);

    public static Component hungerRestoredOther(String target) {
        return ADMIN_PREFIX
                .append(Component.text(target, CUSTOM))
                .append(Component.text("'s hunger has been restored.", NamedTextColor.WHITE));
    }

    public static Component hungerRestoredByStaff(String sender) {
        return Component.text("Your hunger has been restored by ", NamedTextColor.WHITE)
                .append(Component.text(sender, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static final Component FEED_CONFIRM_TITLE = Component.text("Feed Confirm", CUSTOM, TextDecoration.BOLD);

    public static final Component FEED_SELF_ITEM_LORE = Component.text("This will restore your hunger.", NamedTextColor.WHITE)
            .decoration(TextDecoration.ITALIC, false);

    public static Component feedOtherItemLore(String target) {
        return Component.text("This will restore ", NamedTextColor.WHITE)
                .append(Component.text(target, CUSTOM))
                .append(Component.text("'s hunger.", NamedTextColor.WHITE))
                .decoration(TextDecoration.ITALIC, false);
    }

    public static final Component FEED_SELF_INFO_LORE = Component.text("Clicking confirm will restore ", NamedTextColor.WHITE)
            .append(Component.text("your", CUSTOM))
            .append(Component.text(" hunger.", NamedTextColor.WHITE))
            .decoration(TextDecoration.ITALIC, false);

    public static Component feedOtherInfoLore(String target) {
        return Component.text("Clicking confirm will restore ", NamedTextColor.WHITE)
                .append(Component.text(target, CUSTOM))
                .append(Component.text("'s hunger.", NamedTextColor.WHITE))
                .decoration(TextDecoration.ITALIC, false);
    }

    public static final Component HUNGER_RESTORED_SELF = ADMIN_PREFIX
            .append(Component.text("Your hunger has been restored.", NamedTextColor.WHITE));

    public static Component hungerRestoredByAdmin(String player) {
        return Component.text("Your hunger has been restored by ", NamedTextColor.WHITE)
                .append(Component.text(player, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    // /gamemode
    public static final Component USAGE_GAMEMODE = Component.text("Usage: /gamemode <gamemode> [player]", NamedTextColor.RED);

    public static final Component UNKNOWN_GAMEMODE = Component.text("Unknown gamemode.", NamedTextColor.RED);

    public static Component gamemodeChangedSelf(String typeDisplayName) {
        return ADMIN_PREFIX
                .append(Component.text("Your gamemode has been changed to ", NamedTextColor.WHITE))
                .append(Component.text(typeDisplayName, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static final Component NO_PERMISSION_GAMEMODE_OTHERS = ADMIN_PREFIX
            .append(Component.text("You don't have permission to change other players' gamemodes.", NamedTextColor.RED));

    public static final Component ADMIN_PLAYER_NOT_ONLINE = Component.text("That player is not online.", NamedTextColor.RED);

    public static final Component GAMEMODE_CONFIRM_TITLE = Component.text("Gamemode Confirm", CUSTOM, TextDecoration.BOLD);

    public static final Component CANCEL_LORE = Component.text("Cancel this action.", NamedTextColor.RED)
            .decoration(TextDecoration.ITALIC, false);

    public static final Component GAMEMODE_LORE_THIS_WILL_CHANGE = Component.text("This will change", NamedTextColor.WHITE)
            .decoration(TextDecoration.ITALIC, false);

    public static Component gamemodeLoreTarget(String target) {
        return Component.text(target, CUSTOM)
                .decoration(TextDecoration.ITALIC, false);
    }

    public static Component gamemodeLoreMode(String typeDisplayName) {
        return Component.text("to ", NamedTextColor.WHITE)
                .append(Component.text(typeDisplayName, NamedTextColor.WHITE))
                .append(Component.text(" mode.", NamedTextColor.WHITE))
                .decoration(TextDecoration.ITALIC, false);
    }

    public static Component gamemodeSwitchName(String target, String typeDisplayName) {
        return Component.text("Switch ", NamedTextColor.WHITE, TextDecoration.BOLD)
                .append(Component.text(target, NamedTextColor.WHITE, TextDecoration.BOLD))
                .append(Component.text(" to ", NamedTextColor.WHITE, TextDecoration.BOLD))
                .append(Component.text(typeDisplayName, NamedTextColor.WHITE, TextDecoration.BOLD))
                .decoration(TextDecoration.ITALIC, false);
    }

    public static final Component ADMIN_PLAYER_NO_LONGER_ONLINE = Component.text("That player is no longer online.", NamedTextColor.RED);

    public static Component gamemodeChangedOther(String target, String pendingDisplayName) {
        return ADMIN_PREFIX
                .append(Component.text(target, CUSTOM))
                .append(Component.text("'s gamemode has been changed to ", NamedTextColor.WHITE))
                .append(Component.text(pendingDisplayName, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component gamemodeChangedByStaff(String pendingDisplayName) {
        return Component.text("Your gamemode has been changed to ", NamedTextColor.WHITE)
                .append(Component.text(pendingDisplayName, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    // /heal
    public static final Component NO_PERMISSION_HEAL_OTHERS = ADMIN_PREFIX
            .append(Component.text("You do not have permission to heal other players.", NamedTextColor.RED));

    public static final Component USAGE_HEAL = Component.text("Usage: /heal [player]", NamedTextColor.RED);

    public static Component healthRestoredOther(String target) {
        return ADMIN_PREFIX
                .append(Component.text(target, CUSTOM))
                .append(Component.text("'s health has been restored.", NamedTextColor.WHITE));
    }

    public static Component healthRestoredByStaff(String sender) {
        return Component.text("Your health has been restored by ", NamedTextColor.WHITE)
                .append(Component.text(sender, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static final Component HEAL_CONFIRM_TITLE = Component.text("Heal Confirm", CUSTOM, TextDecoration.BOLD);

    public static final Component HEAL_SELF_ITEM_LORE = Component.text("This will restore your health.", NamedTextColor.WHITE)
            .decoration(TextDecoration.ITALIC, false);

    public static Component healOtherItemLore(String target) {
        return Component.text("This will restore ", NamedTextColor.WHITE)
                .append(Component.text(target, CUSTOM))
                .append(Component.text("'s health.", NamedTextColor.WHITE))
                .decoration(TextDecoration.ITALIC, false);
    }

    public static final Component HEAL_SELF_INFO_LORE = Component.text("Clicking confirm will restore ", NamedTextColor.WHITE)
            .append(Component.text("your", CUSTOM))
            .append(Component.text(" health.", NamedTextColor.WHITE))
            .decoration(TextDecoration.ITALIC, false);

    public static Component healOtherInfoLore(String target) {
        return Component.text("Clicking confirm will restore ", NamedTextColor.WHITE)
                .append(Component.text(target, CUSTOM))
                .append(Component.text("'s health.", NamedTextColor.WHITE))
                .decoration(TextDecoration.ITALIC, false);
    }

    public static final Component HEALTH_RESTORED_SELF = ADMIN_PREFIX
            .append(Component.text("Your health has been restored.", NamedTextColor.WHITE));

    public static Component healthRestoredByAdmin(String player) {
        return Component.text("Your health has been restored by ", NamedTextColor.WHITE)
                .append(Component.text(player, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    // /kill
    public static final Component NO_PERMISSION_KILL_OTHERS = ADMIN_PREFIX
            .append(Component.text("You do not have permission to kill other players.", NamedTextColor.RED));

    public static final Component USAGE_KILL = Component.text("Usage: /kill [player]", NamedTextColor.RED);

    public static Component killedOther(String target) {
        return ADMIN_PREFIX
                .append(Component.text(target, CUSTOM))
                .append(Component.text(" has been killed.", NamedTextColor.WHITE));
    }

    public static Component killedByStaff(String sender) {
        return Component.text("You have been killed by ", NamedTextColor.WHITE)
                .append(Component.text(sender, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static final Component KILL_CONFIRM_TITLE = Component.text("Kill Confirm", CUSTOM, TextDecoration.BOLD);

    public static final Component KILL_SELF_ITEM_LORE = Component.text("This will kill you.", NamedTextColor.WHITE)
            .decoration(TextDecoration.ITALIC, false);

    public static final Component KILL_NO_REFUND_LORE = Component.text("Staff will not refund any items lost.", NamedTextColor.WHITE)
            .decoration(TextDecoration.ITALIC, false);

    public static Component killOtherItemLore(String target) {
        return Component.text("This will kill ", NamedTextColor.WHITE)
                .append(Component.text(target, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE))
                .decoration(TextDecoration.ITALIC, false);
    }

    public static final Component KILL_SELF_INFO_LORE = Component.text("Clicking confirm will kill ", NamedTextColor.WHITE)
            .append(Component.text("you", CUSTOM))
            .append(Component.text(".", NamedTextColor.WHITE))
            .decoration(TextDecoration.ITALIC, false);

    public static Component killOtherInfoLore(String target) {
        return Component.text("Clicking confirm will kill ", NamedTextColor.WHITE)
                .append(Component.text(target, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE))
                .decoration(TextDecoration.ITALIC, false);
    }

    public static final Component KILLED_SELF = ADMIN_PREFIX
            .append(Component.text("You have been killed.", NamedTextColor.WHITE));

    public static Component killedByAdmin(String player) {
        return Component.text("You have been killed by ", NamedTextColor.WHITE)
                .append(Component.text(player, CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static final Component PRIVATE_MESSAGES_DISABLED_SELF = Component.text("You have private messages disabled. Use /msgtoggle to re-enable them.", NamedTextColor.RED);

    public static final Component USAGE_MSG = Component.text("Invalid usage. Usage: /msg [player] [message]", NamedTextColor.RED);

    public static final Component PLAYER_NOT_FOUND = Component.text("Player not found.", NamedTextColor.RED);

    public static final Component CANNOT_MESSAGE_YOURSELF = Component.text("You cannot message yourself.", NamedTextColor.RED);

    public static Component privateMessagesDisabledOther(String target) {
        return Component.text(target, NamedTextColor.RED)
                .append(Component.text(" has private messages disabled.", NamedTextColor.RED));
    }

    public static Component privateMessageSent(String target, String message) {
        return Component.text("[", NamedTextColor.WHITE)
                .append(Component.text("You ", CUSTOM))
                .append(Component.text("-> ", NamedTextColor.WHITE))
                .append(Component.text(target, CUSTOM))
                .append(Component.text("] ", NamedTextColor.WHITE))
                .append(Component.text(message, NamedTextColor.WHITE));
    }

    public static Component privateMessageReceived(String sender, String message) {
        return Component.text("[", NamedTextColor.WHITE)
                .append(Component.text(sender, CUSTOM))
                .append(Component.text("-> ", NamedTextColor.WHITE))
                .append(Component.text("You", CUSTOM))
                .append(Component.text("] ", NamedTextColor.WHITE))
                .append(Component.text(message, NamedTextColor.WHITE));
    }

    public static final Component USAGE_MSGTOGGLE = Component.text("Invalid usage. Usage: /msgtoggle [on|off]", NamedTextColor.RED);

    public static final Component PRIVATE_MESSAGES_OFF = PREFIX
            .append(Component.text("You will no longer receive private messages.", NamedTextColor.RED));

    public static final Component PRIVATE_MESSAGES_ON = PREFIX
            .append(Component.text("You will now receive private messages.", NamedTextColor.GREEN));

    public static final Component USAGE_REPLY = Component.text("Invalid usage. Usage: /r [message]", NamedTextColor.RED);

    public static final Component NOBODY_TO_REPLY = Component.text("You have nobody to reply to.", NamedTextColor.RED);

    public static final Component JOIN_DISCORD = PREFIX
            .append(Component.text("Join the discord: ", NamedTextColor.WHITE))
            .append(Component.text("discord.gg/A97Dkcb7TU", CUSTOM));

    public static final Component NO_PERMISSION_NIGHT_VISION_OTHERS = PREFIX
            .append(Component.text("You do not have permission to use night vision on other players.", NamedTextColor.RED));

    public static final Component PLAYER_NOT_ONLINE_PREFIXED = PREFIX
            .append(Component.text("That player is not online.", NamedTextColor.RED));

    public static final Component NIGHT_VISION_ENABLED = PREFIX
            .append(Component.text("Your night vision has been ", NamedTextColor.WHITE))
            .append(Component.text("enabled", NamedTextColor.GREEN))
            .append(Component.text(".", NamedTextColor.WHITE));

    public static final Component NIGHT_VISION_DISABLED = PREFIX
            .append(Component.text("Your night vision has been ", NamedTextColor.WHITE))
            .append(Component.text("disabled", NamedTextColor.RED))
            .append(Component.text(".", NamedTextColor.WHITE));

    public static Component nightVisionEnabledOther(String playerName) {
        return PREFIX
                .append(Component.text(playerName, CUSTOM))
                .append(Component.text("'s night vision has been ", NamedTextColor.WHITE))
                .append(Component.text("enabled", NamedTextColor.GREEN))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component nightVisionDisabledOther(String playerName) {
        return PREFIX
                .append(Component.text(playerName, CUSTOM))
                .append(Component.text("'s night vision has been ", NamedTextColor.WHITE))
                .append(Component.text("disabled", NamedTextColor.RED))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static final Component SPECIFY_PLAYER_FROM_CONSOLE = PREFIX
            .append(Component.text("You must specify a player from console.", NamedTextColor.RED));

    public static Component ping(int playerPing) {
        return PREFIX
                .append(Component.text("Your ping is ", CUSTOM))
                .append(Component.text(playerPing, NamedTextColor.GREEN))
                .append(Component.text("ms", NamedTextColor.GREEN))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static final Component NO_PERMISSION_PING_OTHERS = PREFIX
            .append(Component.text("You do not have permission to check other players ping.", NamedTextColor.RED));

    public static Component pingOther(String target, int targetPing) {
        return PREFIX
                .append(Component.text(target, NamedTextColor.GREEN))
                .append(Component.text("'", NamedTextColor.GREEN))
                .append(Component.text("s ping is ", CUSTOM))
                .append(Component.text(targetPing, NamedTextColor.GREEN))
                .append(Component.text("ms", NamedTextColor.GREEN))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static final Component USAGE_PING = PREFIX
            .append(Component.text("Usage: /ping [player]", NamedTextColor.RED));

    public static final Component CLICK_TO_OPEN_YOUTUBE = Component.text("Click to open YouTube", NamedTextColor.RED);

    public static final Component USAGE_SAY = Component.text("Usage: /say <text>", NamedTextColor.RED);

    public static Component sayConsole(String message) {
        return Component.text("[CONSOLE] ", NamedTextColor.WHITE)
                .append(Component.text(message, NamedTextColor.WHITE));
    }

    public static Component sayPlayerName(String player) {
        return Component.text(player, CUSTOM)
                .append(Component.text(":", NamedTextColor.WHITE));
    }

    public static Component sayText(String message) {
        return Component.text(message, NamedTextColor.WHITE);
    }

    public static Component saySent(String message) {
        return STAFF_PREFIX
                .append(Component.text("You sent the message: \"", NamedTextColor.WHITE))
                .append(Component.text(message, CUSTOM))
                .append(Component.text("\" to all online players.", NamedTextColor.WHITE));
    }

    public static Component trashed(int confirmHolderSize) {
        return PREFIX
                .append(Component.text("Trashed ", NamedTextColor.WHITE))
                .append(Component.text(confirmHolderSize, CUSTOM))
                .append(Component.text(" item stack(s).", NamedTextColor.WHITE));
    }

    public static final Component TRASH_CANCELLED = PREFIX
            .append(Component.text("Cancelled - items returned.", NamedTextColor.RED));

    // Rules menu
    private static final TextColor RULES_COLOR = TextColor.color(0x2979FF);

    public static final Component RULES_TITLE = Component.text("Rules", RULES_COLOR);

    public static final Component RULES_BOOK_NAME = Component.text("Rules", RULES_COLOR)
            .decoration(TextDecoration.ITALIC, false);

    private static final List<String> RULES = List.of(
            "No chat spam, symbols or caps abuse",
            "English only, no swearing or inappropriate topics",
            "No disrespect, harassment or toxicity",
            "No scamming, fake messages or impersonation",
            "No advertising or hackusating",
            "No disallowed mods, hacking, ESP or Xray",
            "No glitch/exploit abuse or duping",
            "No lag machines, bad builds or casinos",
            "No alt abuse, autoclicking or macros",
            "No teleport trapping, command or report abuse",
            "No racism, threats, IRL scamming or leaking info"
    );

    public static final List<Component> RULES_LORE = rulesLore();

    public static final Component RULES_LINK_LINE = Component.text(
            "----------------------------------------",
            TextColor.color(0x555555)
    );

    public static final Component RULES_LINK_MESSAGE = Component.text()
            .append(Component.text("◆ ", CUSTOM))
            .append(Component.text("[CLICK HERE]", CUSTOM, TextDecoration.BOLD))
            .append(Component.text(" Open the Cyber rules.", NamedTextColor.WHITE))
            .build();

    private static List<Component> rulesLore() {
        List<Component> lore = new ArrayList<>();
        for (String rule : RULES) {
            lore.add(Component.text("◆ ", RULES_COLOR)
                    .append(Component.text(rule, NamedTextColor.WHITE))
                    .decoration(TextDecoration.ITALIC, false));
        }
        lore.add(Component.empty());
        lore.add(Component.text("For more details on the rules. Click here.", RULES_COLOR)
                .decoration(TextDecoration.ITALIC, false));
        return List.copyOf(lore);
    }

    // /staff
    public static final Component NO_STAFF_ONLINE = Component.text("There are currently no staff online.", NamedTextColor.RED);

    public static Component staffOnlineHeader(int count) {
        return Component.text("Staff Online ", CUSTOM, TextDecoration.BOLD)
                .append(Component.text("(" + count + ")", NamedTextColor.DARK_GRAY));
    }

    // /colors
    public static final Component COLORS_HEADER = Component.text("ChatColors", NamedTextColor.DARK_GRAY);
    public static final Component FORMATTING_HEADER = Component.text("Formatting", NamedTextColor.DARK_GRAY);

    private static final NamedTextColor[] CHAT_COLORS = {
            NamedTextColor.BLACK, NamedTextColor.DARK_BLUE, NamedTextColor.DARK_GREEN, NamedTextColor.DARK_AQUA,
            NamedTextColor.DARK_RED, NamedTextColor.DARK_PURPLE, NamedTextColor.GOLD, NamedTextColor.GRAY,
            NamedTextColor.DARK_GRAY, NamedTextColor.BLUE, NamedTextColor.GREEN, NamedTextColor.AQUA,
            NamedTextColor.RED, NamedTextColor.LIGHT_PURPLE, NamedTextColor.YELLOW, NamedTextColor.WHITE
    };

    /** One row of eight colour samples, e.g. "&0  &1  &2 ..." where each code is shown in its own colour. */
    public static Component colorRow(int firstIndex) {
        Component row = Component.empty();
        for (int i = firstIndex; i < firstIndex + 8; i++) {
            String code = Integer.toHexString(i);
            row = row.append(Component.text("&" + code + "  ", CHAT_COLORS[i]));
        }
        return row;
    }

    private static Component formatSample(String code, TextDecoration decoration, String label) {
        return Component.text("&" + code, NamedTextColor.WHITE, decoration)
                .append(Component.text(" (" + label + ")", NamedTextColor.DARK_GRAY));
    }

    public static final Component FORMAT_BOLD_STRIKETHROUGH = formatSample("l", TextDecoration.BOLD, "Bold")
            .append(Component.text("  "))
            .append(formatSample("m", TextDecoration.STRIKETHROUGH, "Strikethrough"));

    public static final Component FORMAT_ITALIC_UNDERLINE = formatSample("o", TextDecoration.ITALIC, "Italic")
            .append(Component.text("  "))
            .append(formatSample("n", TextDecoration.UNDERLINED, "Underline"));

    public static final Component FORMAT_OBFUSCATED_RESET = formatSample("k", TextDecoration.OBFUSCATED, "Obfuscated")
            .append(Component.text("  "))
            .append(Component.text("&r", NamedTextColor.WHITE))
            .append(Component.text(" (Reset)", NamedTextColor.DARK_GRAY));

    private Messages() {
    }
}