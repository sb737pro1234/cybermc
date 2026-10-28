package me.sbpro.cyberenchants.gui;

import me.sbpro.cyberenchants.Messages;
import me.sbpro.cyberenchants.settings.UpgradeSettings;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class EnchantUpgradeGUI {

    private EnchantUpgradeGUI() {
    }

    public static void open(Player player) {
        UpgradeHolder holder = new UpgradeHolder();
        Inventory inventory = Bukkit.createInventory(holder, 27, Messages.UPGRADE_MENU_TITLE);
        holder.setInventory(inventory);
        inventory.setItem(4, namedItem(Material.NETHERITE_PICKAXE, Messages.MENU_INFO_NAME, List.of(Messages.MENU_INFO_LORE)));
        inventory.setItem(11, namedItem(Material.RED_STAINED_GLASS_PANE, Messages.PLACE_ITEM_NAME, List.of(Messages.PLACE_ITEM_LORE)));
        inventory.setItem(15, namedItem(Material.RED_STAINED_GLASS_PANE, Messages.PLACE_UPGRADE_NAME, List.of(Messages.PLACE_UPGRADE_LORE)));
        player.openInventory(inventory);
    }

    public static void refresh(UpgradeHolder holder) {
        ItemStack target = holder.getTarget();
        ItemStack upgrade = holder.getUpgrade();

        if (target == null) {
            holder.getInventory().setItem(11, namedItem(Material.RED_STAINED_GLASS_PANE, Messages.PLACE_ITEM_NAME, List.of(Messages.PLACE_ITEM_LORE_EMPTY)));
        } else {
            holder.getInventory().setItem(11, target);
        }

        if (upgrade == null) {
            holder.getInventory().setItem(15, namedItem(Material.RED_STAINED_GLASS_PANE, Messages.PLACE_UPGRADE_NAME, List.of(Messages.PLACE_UPGRADE_LORE_EMPTY)));
        } else {
            holder.getInventory().setItem(15, upgrade);
        }
    }

    public static void openSelection(Player player, ItemStack target, ItemStack upgrade) {
        List<Enchantment> eligible = getEligibleEnchantments(target);
        Inventory inventory = Bukkit.createInventory(new SelectionHolder(target, upgrade), 27, Messages.ENCHANT_SELECT_TITLE);

        int slot = 10;
        for (Enchantment enchantment : eligible) {
            if (slot == 17) slot = 19;

            int current = target.getEnchantmentLevel(enchantment);
            int next = current + 1;
            ItemStack display = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta meta = display.getItemMeta();
            meta.displayName(Messages.enchantOption(formatEnchantment(enchantment), current));
            meta.lore(Messages.enchantOptionLore(formatEnchantment(enchantment), next));
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            display.setItemMeta(meta);
            inventory.setItem(slot, display);
            slot++;
        }
        player.openInventory(inventory);
    }

    public static void openConfirm(Player player, ItemStack target, ItemStack upgrade, Enchantment enchantment) {
        Inventory inventory = Bukkit.createInventory(new ConfirmationHolder(target, upgrade, enchantment), 27, Messages.CONFIRM_TITLE);
        inventory.setItem(16, pane(Material.LIME_STAINED_GLASS_PANE, Messages.CONFIRM_NAME, Messages.CONFIRM_LORE));
        inventory.setItem(10, pane(Material.RED_STAINED_GLASS_PANE, Messages.CANCEL_NAME, Messages.CANCEL_LORE));

        ItemStack display = target.clone();
        ItemMeta meta = display.getItemMeta();
        int current = target.getEnchantmentLevel(enchantment);
        int next = current + 1;
        meta.displayName(Messages.upgradeDisplayName(formatEnchantment(enchantment), current, next));
        meta.lore(List.of(Messages.upgradeDisplayLore(formatEnchantment(enchantment), current, next)));
        display.setItemMeta(meta);
        inventory.setItem(13, display);
        player.openInventory(inventory);
    }

    private static ItemStack pane(Material material, Component name, Component lore) {
        return namedItem(material, name, List.of(lore));
    }

    private static ItemStack namedItem(Material material, Component name, List<Component> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(name);
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static List<Enchantment> getEligibleEnchantments(ItemStack item) {
        List<Enchantment> eligible = new ArrayList<>();
        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta()) return eligible;

        for (Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
            Enchantment enchantment = entry.getKey();
            int level = entry.getValue();
            if (!UpgradeSettings.UPGRADEABLE_ENCHANTMENTS.contains(enchantment)) continue;
            if (enchantment.getMaxLevel() < 2) continue;
            if (level != enchantment.getMaxLevel()) continue;
            if (!enchantment.canEnchantItem(item)) continue;
            eligible.add(enchantment);
        }
        return eligible;
    }

    public static String formatEnchantment(Enchantment enchantment) {
        String[] words = enchantment.getKey().getKey().replace('_', ' ').split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (word.isEmpty()) continue;
            if (!result.isEmpty()) result.append(' ');
            result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }
        return result.toString();
    }

    public static final class UpgradeHolder implements InventoryHolder {
        private Inventory inventory;
        private ItemStack target;
        private ItemStack upgrade;
        private boolean transitioning;

        public Inventory getInventory() { return inventory; }
        public void setInventory(Inventory inventory) { this.inventory = inventory; }
        public ItemStack getTarget() { return target; }
        public void setTarget(ItemStack target) { this.target = target; }
        public ItemStack getUpgrade() { return upgrade; }
        public void setUpgrade(ItemStack upgrade) { this.upgrade = upgrade; }
        public boolean isTransitioning() { return transitioning; }
        public void setTransitioning(boolean transitioning) { this.transitioning = transitioning; }
    }

    public static final class SelectionHolder implements InventoryHolder {
        private final ItemStack target;
        private final ItemStack upgrade;
        private boolean transitioning;
        public SelectionHolder(ItemStack target, ItemStack upgrade) { this.target = target.clone(); this.upgrade = upgrade.clone(); }
        public ItemStack getTarget() { return target; }
        public ItemStack getUpgrade() { return upgrade; }
        public boolean isTransitioning() { return transitioning; }
        public void setTransitioning(boolean transitioning) { this.transitioning = transitioning; }
        public Inventory getInventory() { return null; }
    }

    public static final class ConfirmationHolder implements InventoryHolder {
        private final ItemStack target;
        private final ItemStack upgrade;
        private final Enchantment enchantment;
        private boolean transitioning;
        public ConfirmationHolder(ItemStack target, ItemStack upgrade, Enchantment enchantment) { this.target = target.clone(); this.upgrade = upgrade.clone(); this.enchantment = enchantment; }
        public ItemStack getTarget() { return target; }
        public ItemStack getUpgrade() { return upgrade; }
        public Enchantment getEnchantment() { return enchantment; }
        public boolean isTransitioning() { return transitioning; }
        public void setTransitioning(boolean transitioning) { this.transitioning = transitioning; }
        public Inventory getInventory() { return null; }
    }
}
