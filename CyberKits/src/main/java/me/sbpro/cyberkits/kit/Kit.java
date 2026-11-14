package me.sbpro.cyberkits.kit;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

/**
 * A kit definition (see Kits.java).
 * @param id identifier, used in commands, kititems.yml, cooldowns.yml and the permission cyber.kits.&lt;id&gt;
 * @param name plain name (tab completion, logs)
 * @param prefix rank prefix in &amp; codes with &amp;#RRGGBB hex, e.g. "&amp;#6EE7B7&amp;lJ&amp;#5CDEB4&amp;le..."
 * @param slot position in the /kits menu
 * @param cooldown time between claims
 */
public record Kit(String id, String name, String prefix, int slot, Duration cooldown) {

    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.builder()
            .character('&')
            .hexCharacter('#')
            .hexColors()
            .build();

    private static final Pattern HEX = Pattern.compile("&#([0-9A-Fa-f]{6})");

    public String permission() {
        return "cyber.kits." + id;
    }

    /** The rank prefix as a component (no italics). */
    public Component prefixComponent() {
        return LEGACY.deserialize(prefix.strip()).decoration(TextDecoration.ITALIC, false);
    }

    /** The last hex colour in the prefix (white if it has none). Used for " Kit" and highlights. */
    public TextColor color() {
        Matcher matcher = HEX.matcher(prefix);
        String last = null;
        while (matcher.find()) last = matcher.group(1);
        return last == null ? NamedTextColor.WHITE : TextColor.fromHexString("#" + last);
    }

    /** Prefix + " Kit" in the prefix's last colour, e.g. "Jester Kit". */
    public Component displayName() {
        return Component.text()
                .append(prefixComponent())
                .append(Component.text(" Kit", color(), TextDecoration.BOLD))
                .build()
                .decoration(TextDecoration.ITALIC, false);
    }
}
