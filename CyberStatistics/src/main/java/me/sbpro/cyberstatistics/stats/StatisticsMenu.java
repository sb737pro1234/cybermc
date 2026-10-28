package me.sbpro.cyberstatistics.stats;

import io.papermc.paper.dialog.Dialog;
import me.sbpro.cyberstatistics.Messages;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.DialogBase.DialogAfterAction;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;

public class StatisticsMenu {

    private static final int BUTTON_WIDTH = 180;

    private static final LegacyComponentSerializer LEGACY_PREFIX = LegacyComponentSerializer.builder()
            .character('&')
            .hexColors()
            .build();

    public static void open(Player viewer, Player target) {
        viewer.showDialog(createMainDialog(viewer, target));
    }

    private static Dialog createMainDialog(Player viewer, Player target) {
        DialogBase base = DialogBase.builder(Messages.statisticsTitle(viewer.equals(target), target.getName()))
                .canCloseWithEscape(true)
                .pause(false)
                .afterAction(DialogAfterAction.NONE)
                .body(List.of(
                        DialogBody.plainMessage(
                                createPlayerSummary(target),
                                350
                        )
                ))
                .build();

        return createDialog(base, List.of(
                categoryButton("Combat", "Player kills, mob kills and deaths",
                        p -> p.showDialog(createCombatDialog(viewer, target))),
                categoryButton("Mining", "Mining and block-breaking statistics",
                        p -> p.showDialog(createMiningDialog(viewer, target))),
                categoryButton("Building", "Building and crafting statistics",
                        p -> p.showDialog(createBuildingDialog(viewer, target))),
                categoryButton("Exploration", "Travel and exploration statistics",
                        p -> p.showDialog(createExplorationDialog(viewer, target))),
                categoryButton("Farming", "Farming and animal statistics",
                        p -> p.showDialog(createFarmingDialog(viewer, target))),
                categoryButton("Fishing", "Fishing statistics",
                        p -> p.showDialog(createFishingDialog(viewer, target))),
                categoryButton("General", "General player statistics",
                        p -> p.showDialog(createGeneralDialog(viewer, target)))
        ), createCloseButton());
    }

    private static Dialog createCombatDialog(Player viewer, Player target) {
        DialogBase base = createBase("Combat", target);
        return createStatisticDialog(base, "Combat", List.of(
                Messages.statLine("Player Kills", formatNumber(target.getStatistic(Statistic.PLAYER_KILLS))),
                Messages.statLine("Mob Kills", formatNumber(target.getStatistic(Statistic.MOB_KILLS))),
                Messages.statLine("Deaths", formatNumber(target.getStatistic(Statistic.DEATHS)))), createBackButton(viewer, target));
    }

    private static Dialog createMiningDialog(Player viewer, Player target) {
        DialogBase base = createBase("Mining", target);
        return createStatisticDialog(base, "Mining", List.of(
                Messages.statLine("Blocks Broken", formatNumber(StatisticsFormatter.getBlocksBroken(target))),
                Messages.statLine("Ore Blocks Mined", formatNumber(StatisticsFormatter.getOreBlocksMined(target))),
                Messages.statLine("Tools Broken", formatNumber(StatisticsFormatter.getToolsBroken(target)))), createBackButton(viewer, target));
    }

    private static Dialog createBuildingDialog(Player viewer, Player target) {
        DialogBase base = createBase("Building", target);
        return createStatisticDialog(base, "Building", List.of(
                Messages.statLine("Blocks Placed", formatNumber(StatisticsFormatter.getBlocksPlaced(target))),
                Messages.statLine("Items Crafted", formatNumber(StatisticsFormatter.getItemsCrafted(target)))), createBackButton(viewer, target));
    }

    private static Dialog createExplorationDialog(Player viewer, Player target) {
        DialogBase base = createBase("Exploration", target);
        return createStatisticDialog(base, "Exploration", List.of(
                Messages.statLine("Distance Travelled", StatisticsFormatter.formatDistance(StatisticsFormatter.getTotalDistance(target))),
                Messages.statLine("Jumps", formatNumber(target.getStatistic(Statistic.JUMP)))), createBackButton(viewer, target));
    }

    private static Dialog createFarmingDialog(Player viewer, Player target) {
        DialogBase base = createBase("Farming", target);
        return createStatisticDialog(base, "Farming", List.of(
                Messages.statLine("Crops Harvested", formatNumber(StatisticsFormatter.getCropsHarvested(target))),
                Messages.statLine("Animals Bred", formatNumber(target.getStatistic(Statistic.ANIMALS_BRED)))), createBackButton(viewer, target));
    }

    private static Dialog createFishingDialog(Player viewer, Player target) {
        DialogBase base = createBase("Fishing", target);
        return createStatisticDialog(base, "Fishing", List.of(
                Messages.statLine("Fish Caught", formatNumber(target.getStatistic(Statistic.FISH_CAUGHT)))), createBackButton(viewer, target));
    }

    private static Dialog createGeneralDialog(Player viewer, Player target) {
        DialogBase base = createBase("General", target);
        return createStatisticDialog(base, "General", List.of(
                Messages.statLine("Playtime", StatisticsFormatter.formatPlaytime(target.getStatistic(Statistic.PLAY_ONE_MINUTE))),
                Messages.statLine("Times Slept", formatNumber(target.getStatistic(Statistic.SLEEP_IN_BED))),
                Messages.statLine("Jumps", formatNumber(target.getStatistic(Statistic.JUMP)))), createBackButton(viewer, target));
    }

    private static Dialog createStatisticDialog(
            DialogBase base,
            String categoryTitle,
            List<Component> statistics,
            ActionButton backButton
    ) {

        // Title: BOLD + UNDERLINED only
        Component categoryTitleComponent = Messages.categoryHeading(categoryTitle);

        // Statistics: explicitly NOT bold or underlined
        Component statisticText = Component.text("")
                .append(categoryTitleComponent)
                .append(Component.text("\n\n"));

        for (int i = 0; i < statistics.size(); i++) {

            statisticText = statisticText.append(
                    statistics.get(i)
                            .decoration(TextDecoration.BOLD, false)
                            .decoration(TextDecoration.UNDERLINED, false)
            );

            if (i < statistics.size() - 1) {
                statisticText = statisticText.append(Component.text("\n"));
            }
        }

        final Component finalStatisticText = statisticText;

        return Dialog.create(factory -> {

            var builder = factory.empty();

            builder.base(
                    DialogBase.builder(base.title())
                            .canCloseWithEscape(true)
                            .pause(false)
                            .afterAction(DialogAfterAction.NONE)
                            .body(List.of(
                                    DialogBody.plainMessage(
                                            finalStatisticText,
                                            350
                                    )
                            ))
                            .build()
            );

            builder.type(
                    DialogType.multiAction(
                            List.of(backButton),
                            null,
                            1
                    )
            );
        });
    }

    private static Dialog createDialog(
            DialogBase base,
            List<ActionButton> buttons,
            ActionButton exitButton
    ) {
        return Dialog.create(factory -> {
            var builder = factory.empty();
            builder.base(base);
            builder.type(DialogType.multiAction(buttons, exitButton, 2));
        });
    }

    private static DialogBase createBase(String category, Player target) {
        return DialogBase.builder(Messages.categoryTitle(category))
                .canCloseWithEscape(true)
                .pause(false)
                .afterAction(DialogAfterAction.NONE)
                .body(List.of(DialogBody.plainMessage(Messages.categoryDescription(category, target.getName()), 350)))
                .build();
    }

    private static ActionButton categoryButton(
            String title,
            String description,
            Consumer<Player> action
    ) {
        return ActionButton.create(
                Messages.categoryButton(title),
                Messages.categoryButtonHint(description),
                BUTTON_WIDTH,
                DialogAction.customClick(
                        (response, audience) -> {
                            if (audience instanceof Player player) {
                                action.accept(player);
                            }
                        },
                        ClickCallback.Options.builder()
                                .uses(ClickCallback.UNLIMITED_USES)
                                .build()
                )
        );
    }

    private static ActionButton createBackButton(Player viewer, Player target) {
        return ActionButton.create(
                Messages.BACK_BUTTON,
                Messages.BACK_BUTTON_HINT,
                BUTTON_WIDTH,
                DialogAction.customClick(
                        (response, audience) -> {
                            if (audience instanceof Player player) {
                                player.showDialog(createMainDialog(viewer, target));
                            }
                        },
                        ClickCallback.Options.builder()
                                .uses(ClickCallback.UNLIMITED_USES)
                                .build()
                )
        );
    }

    private static ActionButton createCloseButton() {
        return ActionButton.create(
                Messages.CLOSE_BUTTON,
                Messages.CLOSE_BUTTON_HINT,
                BUTTON_WIDTH,
                null
        );
    }

    private static String formatNumber(long number) {
        return StatisticsFormatter.formatNumber(number);
    }

    private static Component createPlayerSummary(Player player) {

        Component summary = Messages.summaryLine("Username", Messages.summaryValue(player.getName()))
                .appendNewline()
                .append(Messages.summaryLine("Rank", getPrefixComponent(player)))
                .appendNewline()
                .append(Messages.summaryLine("Balance", Messages.summaryValue(getBalance(player))))
                .appendNewline()
                .append(Messages.summaryLine("Playtime", Messages.summaryValue(
                        StatisticsFormatter.formatPlaytime(player.getStatistic(Statistic.PLAY_ONE_MINUTE)))))
                .appendNewline()
                .append(Messages.summaryLine("First Join", Messages.summaryValue(
                        StatisticsFormatter.formatDate(player.getFirstPlayed()))));

        return summary;
    }

    private static String getBalance(Player player) {
        try {
            var registration = Bukkit.getServicesManager().getRegistration(Economy.class);
            if (registration == null) return "Unknown";

            return "$" + StatisticsFormatter.formatNumber(
                    Math.round(registration.getProvider().getBalance((OfflinePlayer) player))
            );
        } catch (Exception ex) {
            return "Unknown";
        }
    }

    private static Component getPrefixComponent(Player player) {

        try {

            var registration = Bukkit.getServicesManager()
                    .getRegistration(LuckPerms.class);

            if (registration == null) {
                return Messages.UNKNOWN;
            }

            LuckPerms api = registration.getProvider();

            User user = api.getUserManager()
                    .getUser(player.getUniqueId());

            if (user == null) {
                return Messages.UNKNOWN;
            }

            CachedMetaData meta = user.getCachedData().getMetaData();

            String prefix = meta.getPrefix();

            if (prefix == null || prefix.isEmpty()) {
                return Messages.UNKNOWN;
            }

            return parsePrefix(prefix);

        } catch (Exception ex) {

            return Messages.UNKNOWN;
        }
    }
    private static Component parsePrefix(String prefix) {

        // MiniMessage prefixes, including gradients
        if (prefix.contains("<") && prefix.contains(">")) {

            try {

                return MiniMessage.miniMessage().deserialize(prefix);

            } catch (Exception ignored) {
                // Fall through to legacy parsing
            }
        }

        // LuckPerms stores prefixes as legacy "&" text (including &#RRGGBB hex); deserialise at the boundary.
        return LEGACY_PREFIX.deserialize(prefix);
    }
}
