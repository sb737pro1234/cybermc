package me.sbpro.cyberannouncements;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public final class Messages {

    /** Highlight colour of the plugin's own feedback messages. */
    public static final TextColor CUSTOM = TextColor.color(0xFFFF00);

    /** Accent colour of livestream / content announcements. */
    public static final TextColor MEDIA = TextColor.color(0xFF69B4);

    // Standard errors (unprefixed)
    public static final Component PLAYER_ONLY = Component.text(
            "You must be a player to use this command.",
            NamedTextColor.RED
    );

    public static final Component NO_PERMISSION = Component.text(
            "You do not have permission to use this command.",
            NamedTextColor.RED
    );

    public static final Component USAGE_ANNOUNCE =
            Component.text("Usage: /announce [announcement]", NamedTextColor.RED);

    public static final Component USAGE_RELOAD =
            Component.text("Usage: /announcements reload", NamedTextColor.RED);

    public static final Component INVALID_URL =
            Component.text("That doesn't look like a valid URL.", NamedTextColor.RED);

    public static final Component CHAT_LOCKED =
            Component.text("Chat is currently locked because of a new announcement.", NamedTextColor.RED);

    public static final Component CONFIG_RELOADED =
            Component.text("CyberAnnouncements configuration reloaded!", NamedTextColor.GREEN);

    public static final Component ANNOUNCEMENT_TITLE =
            Component.text("NEW ANNOUNCEMENT", CUSTOM, TextDecoration.BOLD);

    public static final Component ANNOUNCEMENT_SUBTITLE =
            Component.text("ᴘʟᴇᴀѕᴇ ʀᴇᴀᴅ ᴛʜᴇ ᴍᴇѕѕᴀɢᴇ ɪɴ ᴄʜᴀᴛ", NamedTextColor.WHITE);

    private static final Component CHAT_PAUSED =
            Component.text("ᴄʜᴀᴛ ɪѕ ᴘᴀᴜѕᴇᴅ ꜰᴏʀ 5 ѕᴇᴄᴏɴᴅѕ.", NamedTextColor.RED);

    private Messages() {
    }

    public static Component usageUrl(String commandName) {
        return Component.text("Usage: /" + commandName + " [url]", NamedTextColor.RED);
    }

    public static Component announcementBroadcast(String senderName, String announcement) {
        return Component.text()
                .append(CHAT_PAUSED)
                .appendNewline()
                .appendNewline()
                .append(Component.text("NEW ANNOUNCEMENT:", CUSTOM, TextDecoration.BOLD, TextDecoration.UNDERLINED))
                .append(Component.text(" (From " + senderName + ")", NamedTextColor.WHITE))
                .appendNewline()
                .appendNewline()
                .appendNewline()
                .append(Component.text(announcement, CUSTOM))
                .appendNewline()
                .appendNewline()
                .build();
    }

    /** Title shown to everyone when a media rank announces content, e.g. "SBPRO IS STREAMING". */
    public static Component mediaTitle(String playerName, String action) {
        return Component.text(playerName.toUpperCase() + " " + action, MEDIA, TextDecoration.BOLD);
    }

    public static Component mediaSubtitle(String displayUrl) {
        return Component.text(displayUrl, NamedTextColor.WHITE);
    }

    /** Chat broadcast for a livestream or content announcement. */
    public static Component mediaBroadcast(String heading, String playerName, String displayUrl, String url, String hoverText) {
        Component link = Component.text(displayUrl, NamedTextColor.WHITE, TextDecoration.UNDERLINED)
                .clickEvent(ClickEvent.openUrl(url))
                .hoverEvent(HoverEvent.showText(Component.text(hoverText, NamedTextColor.WHITE)));

        return Component.text()
                .append(CHAT_PAUSED)
                .appendNewline()
                .appendNewline()
                .append(Component.text(heading, MEDIA, TextDecoration.BOLD, TextDecoration.UNDERLINED))
                .append(Component.text(" (From " + playerName + ")", NamedTextColor.WHITE))
                .appendNewline()
                .appendNewline()
                .appendNewline()
                .append(link)
                .appendNewline()
                .appendNewline()
                .build();
    }
}
