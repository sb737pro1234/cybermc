package me.sbpro.cyberkits;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

import net.kyori.adventure.text.format.TextColor;

import me.sbpro.cyberkits.kit.Kit;

/**
 * Kit definitions: identifier, display name, colour, menu slot and cooldown.
 * The items in each kit are NOT set here: they live in kititems.yml and are edited in-game with /kits editor.
 * Each kit needs the permission cyber.kits.&lt;id&gt; (declare new ones in plugin.yml).
 */
public final class Kits {

    private Kits() {}

    /** Size of the /kits menu (multiple of 9). */
    public static final int MENU_SIZE = 36;

    /** Kit given automatically on a player's first join (its cooldown starts straight away). */
    public static final String FIRST_JOIN_KIT = "starter";

    public static final List<Kit> KITS = List.of(
            //      id         display name  colour                         slot  cooldown
            new Kit("starter", "Starter",    TextColor.color(0xAAAAAA), 10, Duration.ofHours(12)),
            new Kit("jester",  "Jester",     TextColor.color(0x55FF55), 12, Duration.ofHours(24)),
            new Kit("squire",  "Squire",     TextColor.color(0x55FFFF), 14, Duration.ofHours(48)),
            new Kit("bishop",  "Bishop",     TextColor.color(0xAA55FF), 16, Duration.ofHours(72)),
            new Kit("lord",    "Lord",       TextColor.color(0xFFAA00), 19, Duration.ofHours(96)),
            new Kit("knight",  "Knight",     TextColor.color(0xFF5555), 21, Duration.ofHours(120)),
            new Kit("king",    "King",       TextColor.color(0xFFFF55), 23, Duration.ofHours(144)),
            new Kit("cyber",   "Cyber",      TextColor.color(0x00AAFF), 25, Duration.ofHours(168))
    );

    /** Kit by id (case-insensitive), or null. */
    public static Kit byId(String id) {
        if (id == null) return null;
        String lower = id.toLowerCase(Locale.ROOT);
        for (Kit kit : KITS) {
            if (kit.id().equals(lower)) return kit;
        }
        return null;
    }

    /** Kit shown in the given menu slot, or null. */
    public static Kit bySlot(int slot) {
        for (Kit kit : KITS) {
            if (kit.slot() == slot) return kit;
        }
        return null;
    }
}
