package me.sbpro.cyberquests.quests;

import me.sbpro.cyberquests.CyberQuests;
import me.sbpro.cyberquests.Tutorial;
import me.sbpro.cyberquests.Messages;
import me.sbpro.cyberquests.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Runs the Tutorial: one step at a time, item rewards, and unlocking the Quest Board at the end.
 * All feedback is sent to the player only.
 */
public class TutorialManager {
    private final CyberQuests plugin;
    private final QuestManager quests;

    public TutorialManager(CyberQuests plugin, QuestManager quests) {
        this.plugin = plugin;
        this.quests = quests;
    }

    /** Called by {@link QuestManager#addProgress} while the player's Quest Board is still locked. */
    void addProgress(Player player, PlayerData data, QuestType type, String target, int amount) {
        TutorialStep step = Tutorial.get(data.getTutorialStep());
        if (step == null) return;
        QuestDefinition quest = step.quest();
        if (quest.getType() != type || !quest.matches(target)) return;

        int progress = Math.min(quest.getRequiredAmount(), data.getTutorialProgress() + amount);
        if (progress == data.getTutorialProgress()) return;
        data.setTutorialProgress(progress);

        if (progress >= quest.getRequiredAmount()) {
            complete(player, data, step);
        } else {
            plugin.getBossBars().showTutorial(player, quest, progress);
            quests.markDirty(data);
        }
    }

    /** Shows the permanent Tutorial boss bar for the current step, or removes it once the Tutorial is finished. */
    public void refreshBossBar(Player player) {
        PlayerData data = quests.getData(player.getUniqueId());
        TutorialStep step = Tutorial.get(data.getTutorialStep());
        if (step == null) plugin.getBossBars().hideTutorial(player);
        else plugin.getBossBars().showTutorial(player, step.quest(), data.getTutorialProgress());
    }

    private void complete(Player player, PlayerData data, TutorialStep step) {
        giveReward(player, step);
        player.sendMessage(Messages.tutorialQuestCompleted(step.quest().getTitle(), step.rewardLabel()));
        advance(player, data);
        quests.saveNow(data);
    }

    /** Moves to the next step; tells the player what is next, or unlocks the Quest Board after the last step. */
    private void advance(Player player, PlayerData data) {
        data.setTutorialStep(data.getTutorialStep() + 1);
        data.setTutorialProgress(0);
        player.sendActionBar(Messages.tutorialActionBar(data.getTutorialStep(), Tutorial.size()));
        TutorialStep next = Tutorial.get(data.getTutorialStep());
        if (next != null) {
            player.sendMessage(Messages.tutorialNextQuest(next.quest().getTitle()));
            player.playSound(Messages.TUTORIAL_STEP_SOUND);
        } else {
            unlockBoard(player, data);
        }
        refreshBossBar(player);
    }

    private void unlockBoard(Player player, PlayerData data) {
        quests.fillNewSlots(data);
        player.sendMessage(Messages.QUEST_BOARD_UNLOCKED);
        player.playSound(Messages.TUTORIAL_FINISHED_SOUND);
    }

    /** Puts the reward in the inventory; anything that does not fit is dropped at the player's feet. */
    private void giveReward(Player player, TutorialStep step) {
        for (ItemStack leftover : player.getInventory().addItem(step.createReward()).values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), leftover);
        }
    }

    // Admin tools

    /** Skips the current step without a reward. Returns the skipped step, or null if the Tutorial was already finished. */
    public TutorialStep skipStep(Player target) {
        PlayerData data = quests.getData(target.getUniqueId());
        TutorialStep step = Tutorial.get(data.getTutorialStep());
        if (step == null) return null;
        advance(target, data);
        quests.saveNow(data);
        return step;
    }

    /** Finishes the whole Tutorial without rewards and unlocks the Quest Board. Returns false if it was already finished. */
    public boolean finish(Player target) {
        PlayerData data = quests.getData(target.getUniqueId());
        if (data.isBoardUnlocked()) return false;
        data.setTutorialStep(Tutorial.size());
        data.setTutorialProgress(0);
        unlockBoard(target, data);
        refreshBossBar(target);
        quests.saveNow(data);
        return true;
    }

    /** Sends the player back to the first step. Their Quest Board slots are kept but locked again. */
    public void reset(Player target) {
        PlayerData data = quests.getData(target.getUniqueId());
        plugin.getBossBars().hide(target);
        data.setTutorialStep(0);
        data.setTutorialProgress(0);
        refreshBossBar(target);
        quests.saveNow(data);
    }
}
