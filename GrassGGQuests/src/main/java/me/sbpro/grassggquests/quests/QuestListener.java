package me.sbpro.grassggquests.quests;

import me.sbpro.grassggquests.GrassGGQuests;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class QuestListener implements Listener {
    private final GrassGGQuests plugin;
    private final QuestManager questManager;
    private final PlacedBlockTracker tracker;

    public QuestListener(GrassGGQuests plugin, QuestManager questManager, PlacedBlockTracker tracker) {
        this.plugin = plugin;
        this.questManager = questManager;
        this.tracker = tracker;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material type = block.getType();

        // Mining quests only count natural blocks. Player-placed blocks are consumed here and never count.
        boolean placedByPlayer = tracker.isTrackedMaterial(type) && tracker.consume(block);
        if (!placedByPlayer) questManager.addProgress(player, QuestType.BREAK_BLOCK, type.name(), 1);

        // Farming quests: only fully grown crops count. Replanting is allowed, so placement is not tracked here.
        if (block.getBlockData() instanceof Ageable ageable && ageable.getAge() >= ageable.getMaximumAge()) {
            questManager.addProgress(player, QuestType.HARVEST_CROP, type.name(), 1);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        questManager.addProgress(event.getPlayer(), QuestType.PLACE_BLOCK, event.getBlock().getType().name(), 1);
    }

    @EventHandler(ignoreCancelled = true)
    public void onKill(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer != null) questManager.addProgress(killer, QuestType.KILL_ENTITY, event.getEntityType().name(), 1);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCraft(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        ItemStack result = event.getCurrentItem();
        if (result == null || result.getType().isAir()) result = event.getRecipe().getResult();
        if (result.getType().isAir()) return;

        final Material type = result.getType();
        final int perCraft = result.getAmount();

        if (event.isShiftClick()) {
            // Shift-click crafts as many times as the ingredients and free inventory space allow.
            // Count what actually arrived in the inventory, capped by what the ingredients could make.
            int crafts = Integer.MAX_VALUE;
            for (ItemStack ingredient : event.getInventory().getMatrix()) {
                if (ingredient != null && !ingredient.getType().isAir()) crafts = Math.min(crafts, ingredient.getAmount());
            }
            if (crafts == Integer.MAX_VALUE) return;
            final int maxCrafted = crafts * perCraft;
            final int before = countItems(player, type);
            Bukkit.getScheduler().runTask(plugin, () -> {
                if (!player.isOnline()) return;
                int crafted = Math.min(maxCrafted, countItems(player, type) - before);
                if (crafted > 0) questManager.addProgress(player, QuestType.CRAFT_ITEM, type.name(), crafted);
            });
            return;
        }

        // A normal click only crafts if the result can actually be picked up.
        ClickType click = event.getClick();
        if (click == ClickType.NUMBER_KEY) {
            ItemStack target = player.getInventory().getItem(event.getHotbarButton());
            if (target != null && !target.getType().isAir()) return;
        } else if (click != ClickType.DROP && click != ClickType.CONTROL_DROP) {
            ItemStack cursor = event.getCursor();
            if (cursor != null && !cursor.getType().isAir()) {
                if (!cursor.isSimilar(result) || cursor.getAmount() + perCraft > cursor.getMaxStackSize()) return;
            }
        }
        questManager.addProgress(player, QuestType.CRAFT_ITEM, type.name(), perCraft);
    }

    private int countItems(Player player, Material type) {
        int total = 0;
        for (ItemStack stack : player.getInventory().getStorageContents()) {
            if (stack != null && stack.getType() == type) total += stack.getAmount();
        }
        return total;
    }

    /** Smelting quests count items the player takes out of a furnace, smoker or blast furnace. */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onFurnaceExtract(FurnaceExtractEvent event) {
        questManager.addProgress(event.getPlayer(), QuestType.SMELT_ITEM, event.getItemType().name(), event.getItemAmount());
    }

    @EventHandler(ignoreCancelled = true)
    public void onFish(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;
        if (!(event.getCaught() instanceof Item item)) return;
        questManager.addProgress(event.getPlayer(), QuestType.FISH_ITEM, item.getItemStack().getType().name(), item.getItemStack().getAmount());
    }

    @EventHandler(ignoreCancelled = true)
    public void onEat(PlayerItemConsumeEvent event) {
        questManager.addProgress(event.getPlayer(), QuestType.EAT_ITEM, event.getItem().getType().name(), 1);
    }
}
