package me.sbpro.cybermastery.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.sbpro.cybermastery.CyberMastery;
import me.sbpro.cybermastery.data.PlayerData;
import me.sbpro.cybermastery.data.TotalsCache;
import me.sbpro.cybermastery.model.Category;
import me.sbpro.cybermastery.model.LevelCurve;
import me.sbpro.cybermastery.model.MasteryType;
import org.bukkit.OfflinePlayer;

import java.util.Locale;

/**
 * %cybermastery_total%            every type level added up (e.g. 44)
 * %cybermastery_total_max%        highest possible total (types x 45)
 * %cybermastery_blocks% (slaying, farming, foraging, fishing)  levels in that category
 * %cybermastery_level_<type>%     one type's level
 *
 * Plain numbers with no colour. Works for offline players.
 * Unknown players give 0, unknown parameters give null.
 */
public final class MasteryPlaceholderExpansion extends PlaceholderExpansion {

    private final CyberMastery plugin;

    public MasteryPlaceholderExpansion(CyberMastery plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "cybermastery";
    }

    @Override
    public String getAuthor() {
        return "SBPro";
    }

    @Override
    public String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        String param = params.toLowerCase(Locale.ROOT);

        // Fixed maximum (no player needed).
        if (param.equals("total_max")) {
            return String.valueOf((long) plugin.getRegistry().allTypes().size() * LevelCurve.MAX_LEVEL);
        }

        // %cybermastery_level_<type>%
        if (param.startsWith("level_")) {
            MasteryType type = plugin.getRegistry().type(param.substring("level_".length()));
            if (type == null) {
                return null;
            }
            if (player == null) {
                return "0";
            }
            PlayerData data = plugin.getDataManager().getAnyone(player.getUniqueId());
            return String.valueOf(plugin.getService().level(data, type));
        }

        Category category = Category.fromKey(param);
        boolean total = param.equals("total");

        if (category == null && !total) {
            return null;
        }

        if (player == null) {
            return "0";
        }

        // Online players: live values. Offline players: totals.yml.
        PlayerData online = plugin.getDataManager().get(player.getUniqueId());

        if (online != null) {
            return category != null
                    ? String.valueOf(plugin.getService().levelSum(online, category))
                    : String.valueOf(plugin.getService().totalLevels(online));
        }

        TotalsCache.Totals totals = plugin.getTotals().get(player.getUniqueId());
        if (totals == null) {
            return "0";
        }

        return category != null
                ? String.valueOf(totals.category(category))
                : String.valueOf(totals.total());
    }
}
