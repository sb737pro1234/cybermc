package me.sbpro.cybercoins;

import me.sbpro.cybercoins.banknote.BankNoteManager;
import me.sbpro.cybercoins.command.CoinCommand;
import me.sbpro.cybercoins.data.CoinManager;
import me.sbpro.cybercoins.listener.BankNoteListener;
import me.sbpro.cybercoins.listener.CoinMenuListener;
import me.sbpro.cybercoins.listener.PlayerKillListener;
import me.sbpro.cybercoins.placeholder.CoinsPlaceholderExpansion;
import me.sbpro.cybercoins.shop.ShopManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class CyberCoins extends JavaPlugin {

    private CoinManager coinManager;
    private ShopManager shopManager;
    private BankNoteManager bankNoteManager;

    @Override
    public void onEnable() {

        saveResource("shop.yml", false);

        this.coinManager = new CoinManager(this);
        this.shopManager = new ShopManager(this);
        this.bankNoteManager = new BankNoteManager(this);

        CoinCommand coinCommand = new CoinCommand(this);

        Objects.requireNonNull(
                getCommand("coins"),
                "coins command missing from plugin.yml"
        ).setExecutor(coinCommand);

        Objects.requireNonNull(
                getCommand("coins"),
                "coins command missing from plugin.yml"
        ).setTabCompleter(coinCommand);

        Objects.requireNonNull(
                getCommand("coinshop"),
                "coinshop command missing from plugin.yml"
        ).setExecutor(coinCommand);

        Objects.requireNonNull(
                getCommand("coinshop"),
                "coinshop command missing from plugin.yml"
        ).setTabCompleter(coinCommand);

        Bukkit.getPluginManager().registerEvents(
                new CoinMenuListener(this),
                this
        );

        Bukkit.getPluginManager().registerEvents(
                new BankNoteListener(this),
                this
        );

        Bukkit.getPluginManager().registerEvents(
                new PlayerKillListener(this),
                this
        );

        Objects.requireNonNull(
                getCommand("withdrawcoins"),
                "withdrawcoins command missing from plugin.yml"
        ).setExecutor(coinCommand);

        Objects.requireNonNull(
                getCommand("withdrawcoins"),
                "withdrawcoins command missing from plugin.yml"
        ).setTabCompleter(coinCommand);

        Objects.requireNonNull(
                getCommand("paycoins"),
                "paycoins command missing from plugin.yml"
        ).setExecutor(coinCommand);

        Objects.requireNonNull(
                getCommand("paycoins"),
                "paycoins command missing from plugin.yml"
        ).setTabCompleter(coinCommand);

        Objects.requireNonNull(
                getCommand("coinstop"),
                "coinstop command missing from plugin.yml"
        ).setExecutor(coinCommand);

        Objects.requireNonNull(
                getCommand("coinstop"),
                "coinstop command missing from plugin.yml"
        ).setTabCompleter(coinCommand);

        if (Bukkit.getPluginManager()
                .isPluginEnabled("PlaceholderAPI")) {

            new CoinsPlaceholderExpansion(this).register();

            getLogger().info(
                    "PlaceholderAPI expansion registered: %cybercoins%."
            );
        }

        startGlobalRewardTimer();

        getLogger().info("CyberCoins enabled.");
    }

    @Override
    public void onDisable() {

        if (coinManager != null) {
            coinManager.save();
        }

        if (bankNoteManager != null) {
            bankNoteManager.save();
        }

        getLogger().info("CyberCoins disabled.");
    }

    private void startGlobalRewardTimer() {

        long interval = 15L * 60L * 20L;

        Bukkit.getScheduler().runTaskTimer(
                this,
                () -> {

                    Bukkit.getOnlinePlayers().forEach(player -> {

                        coinManager.addCoins(
                                player.getUniqueId(),
                                1L,
                                false
                        );

                        player.sendActionBar(
                                Messages.rewardActionBar()
                        );

                        player.sendMessage(
                                Messages.rewardChatMessage()
                        );
                    });

                    coinManager.save();

                },
                interval,
                interval
        );
    }

    public CoinManager getCoinManager() {
        return coinManager;
    }

    public ShopManager getShopManager() {
        return shopManager;
    }

    public BankNoteManager getBankNoteManager() {
        return bankNoteManager;
    }
}