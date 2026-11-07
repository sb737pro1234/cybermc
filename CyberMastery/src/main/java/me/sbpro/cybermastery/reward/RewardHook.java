package me.sbpro.cybermastery.reward;

import me.sbpro.cybermastery.model.MasteryType;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

/**
 * Where level rewards will plug in later.
 *
 * Levelling up never gives rewards automatically. A reward is given when the
 * player claims that level in the type menu (or with Claim All), so nothing is
 * lost if they claim later. Set a custom hook with CyberMastery#setRewardHook.
 *
 * To react to level-ups themselves (broadcasts, stats), listen to
 * {@link me.sbpro.cybermastery.event.MasteryLevelUpEvent} instead.
 */
public interface RewardHook {

    /** Called once when a player claims the reward of a level they have reached. */
    void onClaim(Player player, MasteryType type, int level);

    /**
     * The "Reward:" line shown on a level in the type menu.
     * Return null to show "Coming soon".
     */
    default Component describe(MasteryType type, int level) {
        return null;
    }
}
