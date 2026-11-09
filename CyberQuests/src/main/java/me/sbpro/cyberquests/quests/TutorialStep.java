package me.sbpro.cyberquests.quests;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * One step of the Tutorial (the early-game progression quests).
 *
 * @param quest        what the player has to do (reuses the normal quest matching)
 * @param icon         item shown in the Tutorial menu while this step is the current one
 * @param rewardItem   item given on completion
 * @param rewardAmount how many of the reward item are given
 * @param rewardLabel  reward as shown to the player, e.g. "3 Oak Logs"
 */
public record TutorialStep(QuestDefinition quest, Material icon, Material rewardItem, int rewardAmount, String rewardLabel) {

    public ItemStack createReward() {
        return new ItemStack(rewardItem, rewardAmount);
    }
}
