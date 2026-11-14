package me.sbpro.cyberkits;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

import me.sbpro.cyberkits.kit.Kit;

/**
 * Kit definitions: identifier, name, rank prefix, menu slot and cooldown.
 * The items in each kit are NOT set here: they live in kititems.yml and are edited in-game with /kits editor.
 * Each kit needs the permission cyber.kits.&lt;id&gt; (declare new ones in plugin.yml).
 * Kits are shown as the prefix + " Kit", with " Kit" in the prefix's last hex colour.
 */
public final class Kits {

    private Kits() {}

    /** Size of the /kits menu (multiple of 9). */
    public static final int MENU_SIZE = 45;

    /** Kit given automatically on a player's first join (its cooldown starts straight away). */
    public static final String FIRST_JOIN_KIT = "starter";

    public static final List<Kit> KITS = List.of(
            new Kit("starter", "Starter",
                    "&f&lStarter",
                    10, Duration.ofHours(12)),
            new Kit("jester", "Jester",
                    "&#6EE7B7&lJ&#5CDEB4&le&#4AD4B0&ls&#38CBAD&lt&#26C1A9&le&#14B8A6&lr",
                    12, Duration.ofHours(24)),
            new Kit("squire", "Squire",
                    "&#60A5FA&lS&#5498F7&lq&#488BF4&lu&#3D7DF1&li&#3170EE&lr&#2563EB&le",
                    14, Duration.ofHours(48)),
            new Kit("bishop", "Bishop",
                    "&#A78BFA&lB&#9E7BF7&li&#966BF5&ls&#8D5AF2&lh&#854AF0&lo&#7C3AED&lp",
                    16, Duration.ofHours(72)),
            new Kit("knight", "Knight",
                    "&#F472B6&lK&#EF63A9&ln&#EA549D&li&#E54590&lg&#E03684&lh&#DB2777&lt",
                    28, Duration.ofHours(96)),
            new Kit("lord", "Lord",
                    "&#FB923C&lL&#F57F2C&lo&#F06B1C&lr&#EA580C&ld",
                    30, Duration.ofHours(120)),
            new Kit("king", "King",
                    "&#FDE047&lK&#FACA33&li&#F8B41F&ln&#F59E0B&lg",
                    32, Duration.ofHours(144)),
            new Kit("cyber", "Cyber",
                    "&#008BD1&lC&#0080C0&ly&#0075AF&lb&#00699D&le&#005E8C&lr",
                    34, Duration.ofHours(168))
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
