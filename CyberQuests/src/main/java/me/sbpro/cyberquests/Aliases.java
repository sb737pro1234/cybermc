package me.sbpro.cyberquests;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Command aliases used on the server, for command quests (e.g. "Do /sell").
 *
 * Quests only name the main command; any alias listed here counts as that command. Add the aliases the server
 * actually has (including ones from other plugins) to the matching entry, or add a new entry for a new command.
 * Everything is lowercase and written without the slash.
 */
public final class Aliases {
    private Aliases() {}

    /** Main command -> its aliases. */
    public static final Map<String, List<String>> ALIASES = new LinkedHashMap<>();

    static {
        // Other plugins
        add("rtp", "wild", "wilderness", "randomtp");
        add("shop");
        add("sell", "sellall", "sellhand", "sellgui");

        // CyberMC plugins
        add("tutorial");
        add("homes");
        add("coins", "coin", "c");
        add("spawn", "lobby");
    }

    /** Alias -> main command, built from {@link #ALIASES}. */
    private static final Map<String, String> TO_MAIN = new HashMap<>();

    static {
        for (Map.Entry<String, List<String>> entry : ALIASES.entrySet()) {
            for (String alias : entry.getValue()) TO_MAIN.put(alias, entry.getKey());
        }
    }

    private static void add(String command, String... aliases) {
        ALIASES.put(command.toLowerCase(Locale.ROOT),
                List.of(aliases).stream().map(alias -> alias.toLowerCase(Locale.ROOT)).toList());
    }

    /** Returns the main command for a typed label (e.g. "sellall" -> "sell"), or the label itself if it is not an alias. */
    public static String resolve(String label) {
        String lower = label.toLowerCase(Locale.ROOT);
        return TO_MAIN.getOrDefault(lower, lower);
    }
}
