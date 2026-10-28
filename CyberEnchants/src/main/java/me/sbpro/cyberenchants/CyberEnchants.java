package me.sbpro.cyberenchants;

import me.sbpro.cyberenchants.commands.EnchantItemCommand;
import me.sbpro.cyberenchants.listeners.EnchantUpgradeListener;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class CyberEnchants extends JavaPlugin {

    private static CyberEnchants instance;
    private NamespacedKey customItemKey;

    @Override
    public void onEnable() {
        instance = this;
        customItemKey = new NamespacedKey(this, "custom_enchant_item");

        EnchantItemCommand enchantItemCommand = new EnchantItemCommand(this);
        getCommand("enchantitem").setExecutor(enchantItemCommand);
        getCommand("enchantitem").setTabCompleter(enchantItemCommand);

        getServer().getPluginManager().registerEvents(new EnchantUpgradeListener(this), this);

        getLogger().info("CyberEnchants enabled.");
    }

    @Override
    public void onDisable() {
        // Close any open upgrade GUIs so reserved upgrade items are handed
        // back to players instead of being lost on reload/shutdown.
        for (org.bukkit.entity.Player player : getServer().getOnlinePlayers()) {
            Object holder = player.getOpenInventory().getTopInventory().getHolder();
            if (holder instanceof me.sbpro.cyberenchants.gui.EnchantUpgradeGUI.SelectionHolder
                    || holder instanceof me.sbpro.cyberenchants.gui.EnchantUpgradeGUI.ConfirmationHolder) {
                player.closeInventory();
            }
        }
    }

    public static CyberEnchants getInstance() {
        return instance;
    }

    public NamespacedKey getCustomItemKey() {
        return customItemKey;
    }
}
