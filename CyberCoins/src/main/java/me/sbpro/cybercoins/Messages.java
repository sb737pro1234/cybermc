package me.sbpro.cybercoins;

import me.sbpro.cybercoins.util.AmountFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.util.List;

/**
 * All player-facing CyberCoins messages, titles, colours and GUI text live here.
 *
 * Messages are Adventure Components. Main text is white and highlighted values use {@link #CUSTOM}.
 */
public final class Messages {

    private Messages() {
    }

    // =============================================================
    // Colours
    // =============================================================

    /**
     * Main CyberCoins highlight/title colour.
     * Change this one value to change the highlight colour everywhere.
     * Current colour: #FFD54A
     */
    public static final TextColor CUSTOM = TextColor.color(0xFFD54A);

    /** Shop item names come from shop.yml and use "&" colour codes. */
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.builder()
            .character('&')
            .hexColors()
            .build();

    // =============================================================
    // Prefix / general messages
    // =============================================================

    public static final Component PREFIX = Component.text()
            .append(Component.text("COINS", CUSTOM, TextDecoration.BOLD))
            .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
            .build();

    // Standard errors (unprefixed)
    public static final Component PLAYER_ONLY = Component.text(
            "You must be a player to use this command.",
            NamedTextColor.RED
    );

    public static final Component NO_PERMISSION = Component.text(
            "You do not have permission to use this command.",
            NamedTextColor.RED
    );

    public static Component invalidNumber() {
        return PREFIX
            .append(Component.text("Please enter a valid whole number.", NamedTextColor.RED));
    }

    public static Component invalidAmount() {
        return PREFIX
            .append(Component.text("The amount must be zero or greater.", NamedTextColor.RED));
    }

    public static Component playerNotFound() {
        return PREFIX
            .append(Component.text("That player could not be found.", NamedTextColor.RED));
    }

    public static Component unknownSubcommand() {
        return PREFIX
            .append(Component.text("Unknown subcommand.", NamedTextColor.RED));
    }

    public static Component reloadSuccess() {return PREFIX
            .append(Component.text("Configuration reloaded successfully.", NamedTextColor.GREEN));}

    // =============================================================
    // Reward messages
    // =============================================================
    public static Component rewardActionBar() {
        return rewardActionBar(1);
    }

    public static Component rewardActionBar(long amount) {
        return Component.text("You have received ", NamedTextColor.WHITE)
            .append(Component.text("+", CUSTOM))
            .append(Component.text(AmountFormatter.format(amount), CUSTOM))
            .append(Component.text(" coin", CUSTOM))
            .append(Component.text((amount == 1 ? "" : "s"), CUSTOM))
            .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component rewardChatMessage() {
        return rewardChatMessage(1);
    }

    public static Component rewardChatMessage(long amount) {
        return Component.text("-----------------------------------------------\n", NamedTextColor.DARK_GRAY)
            .append(PREFIX)
            .append(Component.text("You have received ", NamedTextColor.WHITE))
            .append(Component.text("+", CUSTOM))
            .append(Component.text(AmountFormatter.format(amount), CUSTOM))
            .append(Component.text(" coin", CUSTOM))
            .append(Component.text((amount == 1 ? "" : "s"), CUSTOM))
            .append(Component.text(". Do ", NamedTextColor.WHITE))
            .append(Component.text("/coins", CUSTOM))
            .append(Component.text(" for more info.\n", NamedTextColor.WHITE))
            .append(Component.text("-----------------------------------------------", NamedTextColor.DARK_GRAY));
    }

    // =============================================================
    // /coins menu
    // =============================================================

    public static Component coinsMenuTitle() {
        return Component.text("COINS", CUSTOM, TextDecoration.BOLD);
    }

    public static Component shopButtonName() {
        return Component.text("Coin Shop", CUSTOM, TextDecoration.BOLD);
    }

    public static List<Component> shopButtonLore() {
        return List.of(
                Component.text("Click to go to the shop.", NamedTextColor.WHITE)
        );
    }

    public static Component balanceItemName(long balance) {
        return Component.text("Coins: ", NamedTextColor.WHITE)
            .append(Component.text(AmountFormatter.format(balance), CUSTOM));
    }

    public static List<Component> balanceItemLore() {
        return List.of(
                Component.text("This is your current coin balance.", NamedTextColor.WHITE)
        );
    }

    public static Component infoItemName() {
        return Component.text("What are coins?", CUSTOM, TextDecoration.BOLD);
    }

    public static List<Component> infoItemLore() {
        return List.of(
                Component.text("Coins can be used to buy special items in", NamedTextColor.WHITE)
            .append(Component.text(" /coinshop", CUSTOM))
            .append(Component.text(".", NamedTextColor.WHITE)),
                Component.empty(),
                Component.text("Ways to get coins:", CUSTOM),
                Component.text("✦", CUSTOM)
            .append(Component.text(" Receive ", NamedTextColor.WHITE))
            .append(Component.text("1 coin", CUSTOM))
            .append(Component.text(" every 15 minutes through playtime.", NamedTextColor.WHITE)),
                Component.text("✦", CUSTOM)
            .append(Component.text(" Receive ", NamedTextColor.WHITE))
            .append(Component.text("5 coins", CUSTOM))
            .append(Component.text(" upon killing a player.", NamedTextColor.WHITE)),
                Component.text("✦", CUSTOM)
            .append(Component.text(" Receive ", NamedTextColor.WHITE))
            .append(Component.text("10 coins", CUSTOM))
            .append(Component.text(" upon completing quests.", NamedTextColor.WHITE))
        );
    }

    public static Component topButtonName() {
        return Component.text("Top Coins", CUSTOM, TextDecoration.BOLD);
    }

    public static List<Component> topButtonLore() {
        return List.of(
                Component.text("View the players with the most coins.", NamedTextColor.WHITE)
        );
    }

    // =============================================================
    // /coinshop menu
    // =============================================================

    public static Component shopMenuTitle() {
        return Component.text("COIN SHOP", CUSTOM, TextDecoration.BOLD);
    }

    public static List<Component> shopProductLore(long cost) {
        return List.of(
                Component.text("Cost: ", NamedTextColor.WHITE)
            .append(Component.text(AmountFormatter.format(cost), CUSTOM))
            .append(Component.text(" coins", NamedTextColor.WHITE)),
                Component.text("Click to purchase.", NamedTextColor.WHITE)
        );
    }

    public static Component shopEmptyItemName() {
        return Component.text("Not configured", NamedTextColor.WHITE);
    }

    public static List<Component> shopEmptyItemLore() {
        return List.of(
                Component.text("Set this item's display item using the command.", NamedTextColor.WHITE)
        );
    }

    public static Component purchased(String displayName, long cost) {
        return PREFIX
            .append(Component.text("Purchase successful! ", CUSTOM))
            .append(Component.text("You bought ", NamedTextColor.WHITE))
            .append(LEGACY.deserialize(displayName).colorIfAbsent(NamedTextColor.WHITE))
            .append(Component.text(" for ", NamedTextColor.WHITE))
            .append(Component.text(AmountFormatter.format(cost), CUSTOM))
            .append(Component.text(" coin", NamedTextColor.WHITE))
            .append(Component.text((cost == 1 ? "." : "s."), NamedTextColor.WHITE));
    }

    public static Component insufficientCoins(long balance, long cost) {
        return PREFIX
            .append(Component.text("You do not have enough coins.\n", NamedTextColor.RED))
            .append(Component.text("Balance: ", NamedTextColor.WHITE))
            .append(Component.text(AmountFormatter.format(balance), CUSTOM))
            .append(Component.text(" | Cost: ", NamedTextColor.WHITE))
            .append(Component.text(AmountFormatter.format(cost), CUSTOM));
    }

    public static final Sound INSUFFICIENT_COINS_SOUND = Sound.ENTITY_VILLAGER_NO;
    public static final float INSUFFICIENT_COINS_SOUND_VOLUME = 1.0f;
    public static final float INSUFFICIENT_COINS_SOUND_PITCH = 1.0f;

    public static Component purchaseCommandFailed() {
        return PREFIX
            .append(Component.text("This shop item could not be processed, so your coins were not taken.", NamedTextColor.RED));
    }

    // =============================================================
    // Admin command messages
    // =============================================================

    public static Component giveSuccess(String playerName, long amount) {
        return PREFIX
            .append(Component.text("Gave ", NamedTextColor.WHITE))
            .append(Component.text(AmountFormatter.format(amount), CUSTOM))
            .append(Component.text(" coin", NamedTextColor.WHITE))
            .append(Component.text((amount == 1 ? "" : "s"), NamedTextColor.WHITE))
            .append(Component.text(" to ", NamedTextColor.WHITE))
            .append(Component.text(playerName, CUSTOM))
            .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component takeSuccess(String playerName, long amount) {
        return PREFIX
            .append(Component.text("Took ", NamedTextColor.WHITE))
            .append(Component.text(AmountFormatter.format(amount), CUSTOM))
            .append(Component.text(" coin", NamedTextColor.WHITE))
            .append(Component.text((amount == 1 ? "" : "s"), NamedTextColor.WHITE))
            .append(Component.text(" from ", NamedTextColor.WHITE))
            .append(Component.text(playerName, CUSTOM))
            .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component setSuccess(String playerName, long amount) {
        return PREFIX
            .append(Component.text("Set ", NamedTextColor.WHITE))
            .append(Component.text(playerName, CUSTOM))
            .append(Component.text("'s balance to ", NamedTextColor.WHITE))
            .append(Component.text(AmountFormatter.format(amount), CUSTOM))
            .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component balanceMessage(String playerName, long amount) {
        return PREFIX
            .append(Component.text(playerName, CUSTOM))
            .append(Component.text(" has ", NamedTextColor.WHITE))
            .append(Component.text(AmountFormatter.format(amount), CUSTOM))
            .append(Component.text(" coin", NamedTextColor.WHITE))
            .append(Component.text((amount == 1 ? "." : "s."), NamedTextColor.WHITE));
    }

    public static Component setDisplaySuccess(String identifier) {
        return PREFIX
            .append(Component.text("Set the display item for ", NamedTextColor.WHITE))
            .append(Component.text(identifier, CUSTOM))
            .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component shopIdentifierMissing() {
        return PREFIX
            .append(Component.text("Please provide a shop identifier.", NamedTextColor.RED));
    }

    public static Component shopIdentifierUnknown(String identifier) {
        return PREFIX
            .append(Component.text("No shop item exists with identifier ", NamedTextColor.RED))
            .append(Component.text(identifier, CUSTOM))
            .append(Component.text(".", NamedTextColor.RED));
    }

    public static Component emptyHand() {
        return PREFIX
            .append(Component.text("You must hold the display item in your main hand.", NamedTextColor.RED));
    }

    public static Component usage(String usage) {
        return PREFIX
            .append(Component.text("Usage: ", NamedTextColor.RED))
            .append(Component.text(usage, CUSTOM));
    }

    // =============================================================
    // Top coins menu
    // =============================================================

    public static Component topCoinsMenuTitle() {
        return Component.text("TOP COINS", CUSTOM, TextDecoration.BOLD);
    }

    public static Component topCoinsPlayerName(String playerName, int position) {
        return Component.text(playerName, NamedTextColor.WHITE)
            .append(Component.text(" (", NamedTextColor.WHITE))
            .append(Component.text("#", CUSTOM))
            .append(Component.text(position, CUSTOM))
            .append(Component.text(")", NamedTextColor.WHITE));
    }

    public static Component topCoinsPlayerNameUnranked(String playerName) {
        return Component.text(playerName, NamedTextColor.WHITE);
    }

    public static List<Component> topCoinsPlayerLore(long balance) {
        return List.of(
                Component.text("Coins: ", NamedTextColor.WHITE)
            .append(Component.text(AmountFormatter.format(balance), CUSTOM))
        );
    }

    // =============================================================
// Purchase confirmation menu
// =============================================================

    public static Component confirmationMenuTitle() {
        return Component.text("CONFIRM PURCHASE", CUSTOM, TextDecoration.BOLD);
    }

    public static Component confirmPurchaseName() {
        return Component.text("Confirm", NamedTextColor.GREEN, TextDecoration.BOLD);
    }

    public static List<Component> confirmPurchaseLore(long cost) {
        return List.of(
                Component.text("Click to confirm your purchase.", NamedTextColor.WHITE),
                Component.text("Cost: ", NamedTextColor.WHITE)
            .append(Component.text(AmountFormatter.format(cost), CUSTOM))
            .append(Component.text(" coins", CUSTOM))
        );
    }

    public static Component cancelPurchaseName() {
        return Component.text("Cancel", NamedTextColor.RED, TextDecoration.BOLD);
    }

    public static List<Component> cancelPurchaseLore() {
        return List.of(
                Component.text("Click to return to the shop.", NamedTextColor.WHITE)
        );
    }

    public static Component shopBalanceItemName(long balance) {
        return Component.text("Your Coins", CUSTOM, TextDecoration.BOLD);
    }

    public static List<Component> shopBalanceItemLore(long balance) {
        return List.of(
                Component.text("Balance: ", CUSTOM)
            .append(Component.text(AmountFormatter.format(balance), NamedTextColor.WHITE))
            .append(Component.text(" coins", NamedTextColor.WHITE))
        );
    }

    // =============================================================
    // Player-to-player payments
    // =============================================================

    public static Component payReceivedActionBar(
            String playerName,
            long amount
    ) {
        return Component.text("You have received ", NamedTextColor.WHITE)
            .append(Component.text("+", CUSTOM))
            .append(Component.text(AmountFormatter.format(amount), CUSTOM))
            .append(Component.text(" coins ", CUSTOM))
            .append(Component.text("from ", NamedTextColor.WHITE))
            .append(Component.text(playerName, CUSTOM))
            .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component payReceivedChatMessage(
            String playerName,
            long amount
    ) {
        return Component.text("-----------------------------------------------\n", NamedTextColor.DARK_GRAY)
            .append(PREFIX)
            .append(Component.text("You have received ", NamedTextColor.WHITE))
            .append(Component.text("+", CUSTOM))
            .append(Component.text(AmountFormatter.format(amount), CUSTOM))
            .append(Component.text(" coins", CUSTOM))
            .append(Component.text(" from ", NamedTextColor.WHITE))
            .append(Component.text(playerName, CUSTOM))
            .append(Component.text(". Do ", NamedTextColor.WHITE))
            .append(Component.text("/coins", CUSTOM))
            .append(Component.text(" for more info.\n", NamedTextColor.WHITE))
            .append(Component.text("-----------------------------------------------", NamedTextColor.DARK_GRAY));
    }

    public static Component paySuccess(
            String playerName,
            long amount
    ) {
        return PREFIX
            .append(Component.text("You sent ", NamedTextColor.WHITE))
            .append(Component.text(AmountFormatter.format(amount), CUSTOM))
            .append(Component.text(" coins to ", NamedTextColor.WHITE))
            .append(Component.text(playerName, CUSTOM))
            .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component insufficientPayBalance(
            long balance,
            long amount
    ) {
        return PREFIX
            .append(Component.text("You do not have enough coins to make that payment.\n", NamedTextColor.RED))
            .append(Component.text("Balance: ", NamedTextColor.WHITE))
            .append(Component.text(AmountFormatter.format(balance), CUSTOM))
            .append(Component.text(" | Requested: ", NamedTextColor.WHITE))
            .append(Component.text(AmountFormatter.format(amount), CUSTOM));
    }

    public static Component invalidPayAmount() {
        return PREFIX
            .append(Component.text("The payment amount must be greater than zero.", NamedTextColor.RED));
    }

    public static Component paySelf() {
        return PREFIX
            .append(Component.text("You cannot pay yourself.", NamedTextColor.RED));
    }

    // =============================================================
// Bank notes
// =============================================================

    public static final Material BANK_NOTE_MATERIAL = Material.SUNFLOWER;

    public static Component bankNoteName() {
        return Component.text("Coin Bank Note", CUSTOM, TextDecoration.BOLD);
    }

    public static List<Component> bankNoteLore(
            long amount,
            String withdrawnBy
    ) {
        return List.of(
                Component.text("Amount: ", NamedTextColor.WHITE)
            .append(Component.text(AmountFormatter.format(amount), CUSTOM)),

                Component.text("Withdrawn by: ", NamedTextColor.WHITE)
            .append(Component.text(withdrawnBy, CUSTOM))
        );
    }

    public static Component withdrawSuccess(long amount) {
        return PREFIX
            .append(Component.text("Withdrew ", NamedTextColor.WHITE))
            .append(Component.text(AmountFormatter.format(amount), CUSTOM))
            .append(Component.text(" coins into a bank note.", NamedTextColor.WHITE));
    }

    public static Component invalidWithdrawAmount() {
        return PREFIX
            .append(Component.text("The withdrawal amount must be greater than zero.", NamedTextColor.RED));
    }

    public static Component insufficientWithdrawBalance(
            long balance,
            long amount
    ) {
        return PREFIX
            .append(Component.text("You do not have enough coins to withdraw that amount.\n", NamedTextColor.RED))
            .append(Component.text("Balance: ", NamedTextColor.WHITE))
            .append(Component.text(AmountFormatter.format(balance), CUSTOM))
            .append(Component.text(" | Requested: ", NamedTextColor.WHITE))
            .append(Component.text(AmountFormatter.format(amount), CUSTOM));
    }

    public static Component bankNoteInventoryFull() {
        return PREFIX
            .append(Component.text("Your inventory is full, so the withdrawal was cancelled.", NamedTextColor.RED));
    }

    public static Component bankNoteRedeemed(long amount) {
        return PREFIX
            .append(Component.text("You redeemed a bank note worth ", NamedTextColor.WHITE))
            .append(Component.text(AmountFormatter.format(amount), CUSTOM))
            .append(Component.text(" coins.", NamedTextColor.WHITE));
    }

    public static Component bankNoteAlreadyUsed() {
        return PREFIX
            .append(Component.text("This bank note has already been used.", NamedTextColor.RED));
    }

    public static Component bankNoteError() {
        return PREFIX
            .append(Component.text("This bank note could not be redeemed. No coins were added.", NamedTextColor.RED));
    }

    // =============================================================
// Player kill rewards
// =============================================================

    public static Component playerKillRewardActionBar(
            String victimName,
            long amount
    ) {
        return Component.text("You have received ", NamedTextColor.WHITE)
            .append(Component.text("+", CUSTOM))
            .append(Component.text(AmountFormatter.format(amount), CUSTOM))
            .append(Component.text(" coins", CUSTOM))
            .append(Component.text(" for killing ", NamedTextColor.WHITE))
            .append(Component.text(victimName, CUSTOM))
            .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component playerKillRewardChatMessage(
            String victimName,
            long amount
    ) {
        return Component.text("-----------------------------------------------\n", NamedTextColor.DARK_GRAY)
            .append(PREFIX)
            .append(Component.text("You have received ", NamedTextColor.WHITE))
            .append(Component.text("+", CUSTOM))
            .append(Component.text(AmountFormatter.format(amount), CUSTOM))
            .append(Component.text(" coins", CUSTOM))
            .append(Component.text(" for killing ", NamedTextColor.WHITE))
            .append(Component.text(victimName, CUSTOM))
            .append(Component.text(". \n", NamedTextColor.WHITE))
            .append(Component.text("-----------------------------------------------", NamedTextColor.DARK_GRAY));
    }
}