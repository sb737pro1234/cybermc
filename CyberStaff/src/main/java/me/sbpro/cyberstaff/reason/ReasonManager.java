package me.sbpro.cyberstaff.reason;

import me.sbpro.cyberstaff.CyberStaff;
import me.sbpro.cyberstaff.config.PunishmentConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public final class ReasonManager {

    private final CyberStaff plugin;
    private final PunishmentConfig config;
    private final Map<String, PunishmentReason> reasons = new LinkedHashMap<>();

    public ReasonManager(CyberStaff plugin, PunishmentConfig config) {
        this.plugin = plugin;
        this.config = config;
        reload();
    }

    public void reload() {
        reasons.clear();
        ConfigurationSection section = config.getRawConfig().getConfigurationSection("reasons");
        if (section == null) {
            plugin.getLogger().warning("No reasons configured in punishments.yml.");
            return;
        }

        for (String id : section.getKeys(false)) {
            String name = section.getString(id + ".name");
            String tier = section.getString(id + ".tier");

            if (name == null || tier == null) {
                plugin.getLogger().severe("Invalid reason: " + id);
                continue;
            }

            if (config.getTier(tier).isEmpty()) {
                plugin.getLogger().severe("Reason " + id + " references missing tier " + tier);
                continue;
            }

            reasons.put(id.toLowerCase(Locale.ROOT), new PunishmentReason(id, name, tier));
        }
    }

    public Optional<PunishmentReason> find(String input) {
        String normalised = normalise(input);
        return reasons.values().stream()
                .filter(reason ->
                        reason.id().equalsIgnoreCase(input) ||
                        normalise(reason.displayName()).equals(normalised))
                .findFirst();
    }

    /** Reason id stored for staff-written reasons that are not in punishments.yml. */
    public static final String CUSTOM_ID = "custom";

    /** Matches the reason_name column size. */
    public static final int MAX_CUSTOM_LENGTH = 255;

    /**
     * Resolves the reason words typed after the player name.
     *
     * 1. The whole input matches a configured reason -> that reason.
     *    e.g. "Account Sharing"
     * 2. Otherwise the FIRST word must be a tier id and everything
     *    after it is a custom reason.
     *    e.g. "CHAT_1 being silly" -> reason "being silly", tier CHAT_1
     *
     * Empty if neither applies; {@link #problem} says why.
     */
    public Optional<PunishmentReason> resolve(String[] words) {
        if (words.length == 0) {
            return Optional.empty();
        }

        Optional<PunishmentReason> configured = find(String.join(" ", words));
        if (configured.isPresent()) {
            return configured;
        }

        if (!startsWithTier(words)) {
            return Optional.empty();
        }

        String text = customText(words);

        if (text.isEmpty() || text.length() > MAX_CUSTOM_LENGTH) {
            return Optional.empty();
        }

        return config.getTier(stripTierPrefix(words[0]))
                .map(tier -> new PunishmentReason(CUSTOM_ID, text, tier.id()));
    }

    /** Why {@link #resolve} returned empty for these words. */
    public enum Problem {
        /** Not a configured reason and no tier given first. */
        MISSING_TIER,
        /** Tier given but no custom reason after it. */
        MISSING_TEXT,
        /** Custom reason longer than the database column. */
        TOO_LONG
    }

    public Problem problem(String[] words) {
        if (!startsWithTier(words)) {
            return Problem.MISSING_TIER;
        }

        String text = customText(words);

        if (text.isEmpty()) {
            return Problem.MISSING_TEXT;
        }

        return Problem.TOO_LONG;
    }

    /**
     * Tiers are suggested as "#GAME_1" so the client's alphabetical sort
     * lists them above the reason names. Both "#GAME_1" and "GAME_1" work.
     */
    public static final String TIER_PREFIX = "#";

    private static String stripTierPrefix(String word) {
        return word.startsWith(TIER_PREFIX) ? word.substring(TIER_PREFIX.length()) : word;
    }

    private boolean startsWithTier(String[] words) {
        return words.length > 0 && config.getTier(stripTierPrefix(words[0])).isPresent();
    }

    private String customText(String[] words) {
        return String.join(" ", Arrays.copyOfRange(words, 1, words.length)).trim();
    }

    /** Configured tier ids, for error messages. */
    public List<String> tierIds() {
        return config.getTiers().stream().map(tier -> TIER_PREFIX + tier.id()).sorted().toList();
    }

    /**
     * Tab completions for the reason words typed after the player name.
     *
     * First word: configured reason names and tier ids.
     * After a tier: nothing (free-text custom reason).
     * Otherwise: configured reason names matching what has been typed.
     */
    public List<String> complete(String[] words) {
        if (words.length > 1 && startsWithTier(words)) {
            return List.of();
        }

        String typed = String.join(" ", words).toLowerCase(Locale.ROOT);

        List<String> suggestions = new ArrayList<>();

        reasons.values().stream()
                .map(PunishmentReason::displayName)
                .filter(name -> name.toLowerCase(Locale.ROOT).startsWith(typed))
                .sorted()
                .forEach(suggestions::add);

        if (words.length <= 1) {
            String tierTyped = stripTierPrefix(typed);
            config.getTiers().stream()
                    .map(tier -> tier.id())
                    .filter(id -> id.toLowerCase(Locale.ROOT).startsWith(tierTyped))
                    .sorted()
                    .map(id -> TIER_PREFIX + id)
                    .forEach(suggestions::add);
        }

        return suggestions;
    }

    public Collection<PunishmentReason> all() {
        return Collections.unmodifiableCollection(reasons.values());
    }

    public String normalise(String input) {
        return input.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", " ").trim();
    }
}
