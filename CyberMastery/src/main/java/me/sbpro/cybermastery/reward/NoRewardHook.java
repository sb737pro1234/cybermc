package me.sbpro.cybermastery.reward;

import me.sbpro.cybermastery.model.MasteryType;
import org.bukkit.entity.Player;

/** Default hook until rewards are added: claiming just marks the level as claimed. */
public final class NoRewardHook implements RewardHook {

    @Override
    public void onClaim(Player player, MasteryType type, int level) {
        // No rewards yet.
    }
}
