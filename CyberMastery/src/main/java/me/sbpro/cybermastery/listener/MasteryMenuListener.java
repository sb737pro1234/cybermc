package me.sbpro.cybermastery.listener;

import me.sbpro.cybermastery.CyberMastery;
import me.sbpro.cybermastery.Messages;
import me.sbpro.cybermastery.data.PlayerData;
import me.sbpro.cybermastery.gui.CategoryMenuHolder;
import me.sbpro.cybermastery.gui.GroupMenuHolder;
import me.sbpro.cybermastery.gui.MainMenuHolder;
import me.sbpro.cybermastery.gui.MasteryMenuHolder;
import me.sbpro.cybermastery.gui.MenuFactory;
import me.sbpro.cybermastery.gui.TypeMenuHolder;
import me.sbpro.cybermastery.model.Category;
import me.sbpro.cybermastery.model.Group;
import me.sbpro.cybermastery.model.MasteryType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;

/** Cancels every click and drag in CyberMastery menus and handles the buttons. */
public final class MasteryMenuListener implements Listener {

    private final CyberMastery plugin;

    public MasteryMenuListener(CyberMastery plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (event.getView().getTopInventory().getHolder() instanceof MasteryMenuHolder) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory top = event.getView().getTopInventory();

        if (!(top.getHolder() instanceof MasteryMenuHolder holder)) {
            return;
        }

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        int slot = event.getRawSlot();

        // Clicks in the player's own inventory do nothing.
        if (slot < 0 || slot >= top.getSize()) {
            return;
        }

        switch (holder) {
            case MainMenuHolder ignored -> clickMain(player, top, slot);
            case CategoryMenuHolder category -> clickCategory(player, category, slot);
            case GroupMenuHolder group -> clickGroup(player, group, slot);
            case TypeMenuHolder type -> clickType(player, top, type, slot);
            default -> {
            }
        }
    }

    // =============================================================
    // /mastery
    // =============================================================

    private void clickMain(Player player, Inventory top, int slot) {
        Category category = Category.fromMenuSlot(slot);

        if (category != null) {
            player.playSound(player.getLocation(), Messages.CATEGORY_SOUND, Messages.SOUND_VOLUME, Messages.SOUND_PITCH);
            player.openInventory(MenuFactory.createCategoryMenu(plugin, player, category, 0));
            return;
        }

        if (slot == MenuFactory.MAIN_TOP_SLOT) {
            player.playSound(player.getLocation(), Messages.CATEGORY_SOUND, Messages.SOUND_VOLUME, Messages.SOUND_PITCH);
            player.openInventory(MenuFactory.createTopMasteryMenu(plugin, player));
            return;
        }

        if (slot == MenuFactory.MAIN_CLAIM_ALL_SLOT) {
            claimAll(player, plugin.getRegistry().allTypes());
            MenuFactory.fillMainMenu(plugin, player, top);
        }
    }

    // =============================================================
    // Category menu
    // =============================================================

    private void clickCategory(Player player, CategoryMenuHolder holder, int slot) {
        Category category = holder.category();
        int page = holder.page();

        if (slot < MenuFactory.PAGE_SIZE) {
            int index = page * MenuFactory.PAGE_SIZE + slot;

            if (category == Category.BLOCKS) {
                List<Group> groups = plugin.getRegistry().groups();
                if (index < groups.size()) {
                    player.openInventory(MenuFactory.createGroupMenu(plugin, player, groups.get(index), 0));
                }
            } else {
                List<MasteryType> types = plugin.getRegistry().types(category);
                if (index < types.size()) {
                    player.openInventory(MenuFactory.createTypeMenu(plugin, player, types.get(index), page));
                }
            }
            return;
        }

        int pages = MenuFactory.pages(MenuFactory.categoryEntries(plugin, category));

        switch (slot) {
            case MenuFactory.PREVIOUS_SLOT -> {
                if (page > 0) {
                    player.openInventory(MenuFactory.createCategoryMenu(plugin, player, category, page - 1));
                }
            }
            case MenuFactory.NEXT_SLOT -> {
                if (page < pages - 1) {
                    player.openInventory(MenuFactory.createCategoryMenu(plugin, player, category, page + 1));
                }
            }
            case MenuFactory.BACK_SLOT -> {
                playBackSound(player);
                player.openInventory(MenuFactory.createMainMenu(plugin, player));
            }
            default -> {
            }
        }
    }

    // =============================================================
    // Group menu
    // =============================================================

    private void clickGroup(Player player, GroupMenuHolder holder, int slot) {
        Group group = holder.group();
        int page = holder.page();

        if (slot < MenuFactory.PAGE_SIZE) {
            int index = page * MenuFactory.PAGE_SIZE + slot;
            if (index < group.types().size()) {
                player.openInventory(MenuFactory.createTypeMenu(plugin, player, group.types().get(index), page));
            }
            return;
        }

        int pages = MenuFactory.pages(group.types().size());

        switch (slot) {
            case MenuFactory.PREVIOUS_SLOT -> {
                if (page > 0) {
                    player.openInventory(MenuFactory.createGroupMenu(plugin, player, group, page - 1));
                }
            }
            case MenuFactory.NEXT_SLOT -> {
                if (page < pages - 1) {
                    player.openInventory(MenuFactory.createGroupMenu(plugin, player, group, page + 1));
                }
            }
            case MenuFactory.BACK_SLOT -> {
                playBackSound(player);
                int groupIndex = Math.max(0, plugin.getRegistry().groups().indexOf(group));
                player.openInventory(MenuFactory.createCategoryMenu(
                        plugin, player, Category.BLOCKS, groupIndex / MenuFactory.PAGE_SIZE));
            }
            default -> {
            }
        }
    }

    // =============================================================
    // Type menu
    // =============================================================

    private void clickType(Player player, Inventory top, TypeMenuHolder holder, int slot) {
        MasteryType type = holder.type();

        if (slot < MenuFactory.PAGE_SIZE) {
            int level = slot + 1;
            PlayerData data = plugin.getDataManager().get(player.getUniqueId());

            if (data == null || data.isClaimed(type.id(), level)) {
                return;
            }

            if (plugin.getService().claim(player, type, level)) {
                player.sendMessage(Messages.rewardClaimed(type, level));
                player.playSound(player.getLocation(), Messages.CLAIM_SOUND, Messages.SOUND_VOLUME, Messages.SOUND_PITCH);
                MenuFactory.fillTypeMenu(plugin, player, top, type);
            } else {
                player.sendMessage(Messages.levelNotReached());
                player.playSound(player.getLocation(), Messages.ERROR_SOUND, Messages.SOUND_VOLUME, Messages.SOUND_PITCH);
            }
            return;
        }

        switch (slot) {
            case MenuFactory.BACK_SLOT -> {
                playBackSound(player);
                if (type.group() != null) {
                    player.openInventory(MenuFactory.createGroupMenu(plugin, player, type.group(), holder.backPage()));
                } else {
                    player.openInventory(MenuFactory.createCategoryMenu(plugin, player, type.category(), holder.backPage()));
                }
            }
            case MenuFactory.TYPE_CLAIM_ALL_SLOT -> {
                claimAll(player, List.of(type));
                MenuFactory.fillTypeMenu(plugin, player, top, type);
            }
            default -> {
            }
        }
    }

    private static void playBackSound(Player player) {
        player.playSound(player.getLocation(), Messages.BACK_SOUND, Messages.SOUND_VOLUME, Messages.SOUND_PITCH);
    }

    private void claimAll(Player player, List<MasteryType> types) {
        int claimed = plugin.getService().claimAll(player, types);

        if (claimed > 0) {
            player.sendMessage(Messages.rewardsClaimed(claimed));
            player.playSound(player.getLocation(), Messages.CLAIM_SOUND, Messages.SOUND_VOLUME, Messages.SOUND_PITCH);
        } else {
            player.sendMessage(Messages.nothingToClaim());
            player.playSound(player.getLocation(), Messages.ERROR_SOUND, Messages.SOUND_VOLUME, Messages.SOUND_PITCH);
        }
    }
}
