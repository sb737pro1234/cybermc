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

    public Collection<PunishmentReason> all() {
        return Collections.unmodifiableCollection(reasons.values());
    }

    public String normalise(String input) {
        return input.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", " ").trim();
    }
}
