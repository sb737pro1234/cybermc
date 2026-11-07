package me.sbpro.cybermastery.data;

import java.util.UUID;

public record TopMasteryEntry(
        UUID uuid,
        String playerName,
        long levels
) {
}
