package me.sbpro.cyber.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

/**
 * Converts player- or admin-written text that uses "&" colour codes (including "&#RRGGBB" hex
 * colours) into Components. This is only used at input boundaries such as anvil renames and
 * LuckPerms prefixes; plugin messages themselves never use legacy formatting.
 */
public final class LegacyText {

    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.builder()
            .character('&')
            .hexColors()
            .build();

    private LegacyText() {
    }

    public static Component deserialize(String text) {
        return SERIALIZER.deserialize(text);
    }
}
