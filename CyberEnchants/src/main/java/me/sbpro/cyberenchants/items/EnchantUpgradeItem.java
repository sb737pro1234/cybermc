package me.sbpro.cyberenchants.items;

import me.sbpro.cyberenchants.CyberEnchants;
import me.sbpro.cyberenchants.Messages;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public final class EnchantUpgradeItem {

    private EnchantUpgradeItem() {
    }

    public static ItemStack create(CyberEnchants plugin) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Messages.ENCHANT_UPGRADE_NAME);
        meta.lore(Messages.ENCHANT_UPGRADE_LORE);
        meta.getPersistentDataContainer().set(
                plugin.getCustomItemKey(),
                PersistentDataType.STRING,
                "enchant_upgrade"
        );

        item.setItemMeta(meta);
        return item;
    }

    public static boolean isEnchantUpgrade(CyberEnchants plugin, ItemStack item) {
        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta()) {
            return false;
        }

        String value = item.getItemMeta().getPersistentDataContainer().get(
                plugin.getCustomItemKey(),
                PersistentDataType.STRING
        );

        return "enchant_upgrade".equals(value);
    }
}
