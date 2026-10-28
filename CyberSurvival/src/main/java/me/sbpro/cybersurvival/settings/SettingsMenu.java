package me.sbpro.cybersurvival.settings;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import me.sbpro.cybersurvival.CyberSurvival;
import me.sbpro.cyberteleport.CyberTeleport;
import me.sbpro.cyberteleport.settings.TeleportPreferencesManager;
import me.sbpro.cyberteleport.message.Messages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class SettingsMenu {

    private static final int BUTTON_WIDTH = 180;

    /*
     * ============================================================
     * MAIN SETTINGS MENU
     * ============================================================
     */

    public static void open(
            CyberSurvival plugin,
            Player player
    ) {
        /*
         * Opening the dialog one tick later helps avoid occasional
         * client/server timing hitches when the command is executed.
         */
        plugin.getServer().getScheduler().runTask(
                plugin,
                () -> player.showDialog(createMainDialog(plugin))
        );
    }

    private static Dialog createMainDialog(
            CyberSurvival plugin
    ) {

        DialogBase base = DialogBase.builder(
                        SettingsMessages.SETTINGS_SETTINGS
                )
                .canCloseWithEscape(true)
                .pause(false)
                .afterAction(DialogBase.DialogAfterAction.NONE)
                .body(List.of(
                        DialogBody.plainMessage(
                                Component.text(
                                        "Choose a category to manage your settings."
                                ),
                                300
                        )
                ))
                .build();

        ActionButton chatButton = ActionButton.create(
                SettingsMessages.SETTINGS_CHAT,
                SettingsMessages.SETTINGS_CHAT_AND_COMMUNICATION_SETTINGS,
                BUTTON_WIDTH,
                openDialogAction(
                        player -> player.showDialog(
                                createChatDialog(plugin, player)
                        )
                )
        );

        ActionButton visualsButton = ActionButton.create(
                SettingsMessages.SETTINGS_VISUALS,
                SettingsMessages.SETTINGS_VISUAL_AND_DISPLAY_SETTINGS,
                BUTTON_WIDTH,
                openDialogAction(
                        player -> player.showDialog(
                                createVisualsDialog(plugin, player)
                        )
                )
        );

        ActionButton privacyButton = ActionButton.create(
                SettingsMessages.SETTINGS_PRIVACY,
                SettingsMessages.SETTINGS_PRIVACY_AND_VISIBILITY_SETTINGS,
                BUTTON_WIDTH,
                openDialogAction(
                        player -> player.showDialog(
                                createPrivacyDialog(plugin, player)
                        )
                )
        );

        ActionButton miscButton = ActionButton.create(
                SettingsMessages.SETTINGS_MISC,
                SettingsMessages.SETTINGS_MISCELLANEOUS_OPTIONS,
                BUTTON_WIDTH,
                openDialogAction(
                        player -> player.showDialog(
                                createMiscDialog(plugin, player)
                        )
                )
        );

        ActionButton closeButton = createCloseButton();

        return createDialog(
                base,
                List.of(
                        chatButton,
                        visualsButton,
                        privacyButton,
                        miscButton
                ),
                closeButton
        );
    }

    /*
     * ============================================================
     * CHAT
     * ============================================================
     */

    private static Dialog createChatDialog(
            CyberSurvival plugin,
            Player player
    ) {

        DialogBase base = createBase(
                "Chat",
                "Manage your chat-related settings."
        );

        ActionButton chatVisibilityButton = ActionButton.create(
                SettingsMessages.SETTINGS_CHAT_VISIBILITY
                        .append(
                                plugin.getChatToggleManager()
                                        .isChatDisabled(
                                                player.getUniqueId()
                                        )
                                        ? Component.text(
                                        "Disabled",
                                        NamedTextColor.RED
                                )
                                        : Component.text(
                                        "Enabled",
                                        NamedTextColor.GREEN
                                )
                        ),
                SettingsMessages.SETTINGS_TOGGLE_YOUR_CHAT_VISIBILITY,
                BUTTON_WIDTH,
                categoryToggleAction(
                        plugin,
                        clickedPlayer ->
                                plugin.getChatToggleManager()
                                        .toggleChat(
                                                clickedPlayer.getUniqueId()
                                        ),
                        clickedPlayer ->
                                createChatDialog(
                                        plugin,
                                        clickedPlayer
                                )
                )
        );

        return createDialog(
                base,
                List.of(chatVisibilityButton),
                createBackButton(plugin, player)
        );
    }

    /*
     * ============================================================
     * VISUALS
     * ============================================================
     */

    private static Dialog createVisualsDialog(
            CyberSurvival plugin,
            Player player
    ) {

        DialogBase base = createBase(
                "Visuals",
                "Manage your visual and display settings."
        );

        ActionButton nightVisionButton = ActionButton.create(
                SettingsMessages.SETTINGS_NIGHT_VISION
                        .append(
                                plugin.getNightVisionManager()
                                        .isNightVisionEnabled(
                                                player.getUniqueId()
                                        )
                                        ? Component.text(
                                        "Enabled",
                                        NamedTextColor.GREEN
                                )
                                        : Component.text(
                                        "Disabled",
                                        NamedTextColor.RED
                                )
                        ),
                SettingsMessages.SETTINGS_TOGGLE_YOUR_NIGHT_VISION,
                BUTTON_WIDTH,
                categoryToggleAction(
                        plugin,
                        clickedPlayer ->
                                plugin.getNightVisionManager()
                                        .toggleNightVision(
                                                clickedPlayer
                                        ),
                        clickedPlayer ->
                                createVisualsDialog(
                                        plugin,
                                        clickedPlayer
                                )
                )
        );

        ActionButton scoreboardButton = ActionButton.create(
                SettingsMessages.SETTINGS_SCOREBOARD
                        .append(
                                plugin.getScoreboardManager()
                                        .isScoreboardEnabled(
                                                player.getUniqueId()
                                        )
                                        ? Component.text(
                                        "Enabled",
                                        NamedTextColor.GREEN
                                )
                                        : Component.text(
                                        "Disabled",
                                        NamedTextColor.RED
                                )
                        ),
                SettingsMessages.SETTINGS_TOGGLE_YOUR_SCOREBOARD,
                BUTTON_WIDTH,
                categoryToggleAction(
                        plugin,
                        clickedPlayer ->
                                plugin.getScoreboardManager()
                                        .toggleScoreboard(
                                                clickedPlayer
                                        ),
                        clickedPlayer ->
                                createVisualsDialog(
                                        plugin,
                                        clickedPlayer
                                )
                )
        );

        ActionButton glowButton = ActionButton.create(
                SettingsMessages.SETTINGS_GLOW
                        .append(
                                plugin.getGlowManager()
                                        .isGlowEnabled(
                                                player.getUniqueId()
                                        )
                                        ? Component.text(
                                        "Enabled",
                                        NamedTextColor.GREEN
                                )
                                        : Component.text(
                                        "Disabled",
                                        NamedTextColor.RED
                                )
                        ),
                SettingsMessages.SETTINGS_TOGGLE_YOUR_PLAYER_GLOW,
                BUTTON_WIDTH,
                categoryToggleAction(
                        plugin,
                        clickedPlayer ->
                                plugin.getGlowManager()
                                        .toggleGlow(
                                                clickedPlayer
                                        ),
                        clickedPlayer ->
                                createVisualsDialog(
                                        plugin,
                                        clickedPlayer
                                )
                )
        );

        ActionButton bossBarButton = ActionButton.create(
                SettingsMessages.SETTINGS_BOSSBAR
                        .append(
                                plugin.getBossBarManager()
                                        .isBossBarEnabled(
                                                player.getUniqueId()
                                        )
                                        ? Component.text(
                                        "Enabled",
                                        NamedTextColor.GREEN
                                )
                                        : Component.text(
                                        "Disabled",
                                        NamedTextColor.RED
                                )
                        ),
                SettingsMessages.SETTINGS_TOGGLE_YOUR_BOSSBAR,
                BUTTON_WIDTH,
                categoryToggleAction(
                        plugin,
                        clickedPlayer ->
                                plugin.getBossBarManager()
                                        .toggleBossBar(
                                                clickedPlayer
                                        ),
                        clickedPlayer ->
                                createVisualsDialog(
                                        plugin,
                                        clickedPlayer
                                )
                )
        );

        return createDialog(
                base,
                List.of(
                        nightVisionButton,
                        scoreboardButton,
                        glowButton,
                        bossBarButton
                ),
                createBackButton(plugin, player)
        );
    }

    /*
     * ============================================================
     * PRIVACY
     * ============================================================
     */

    /** Green "Enabled" / red "Disabled" state label. */
    private static Component stateOf(boolean enabled) {
        return enabled
                ? Component.text("Enabled", NamedTextColor.GREEN)
                : Component.text("Disabled", NamedTextColor.RED);
    }

    private static Dialog createPrivacyDialog(
            CyberSurvival plugin,
            Player player
    ) {

        DialogBase base = createBase(
                "Privacy",
                "Manage your privacy and visibility settings."
        );

        CyberTeleport teleportPlugin = getTeleportPlugin();

        if (teleportPlugin == null) {
            return createDialog(
                    base,
                    List.of(
                            ActionButton.create(
                                    Component.text(
                                            "Teleport Settings Unavailable"
                                    ),
                                    Component.text(
                                            "CyberTeleport is not currently available."
                                    ),
                                    BUTTON_WIDTH,
                                    null
                            )
                    ),
                    createBackButton(plugin, player)
            );
        }

        TeleportPreferencesManager preferences = teleportPlugin.getTeleportPreferencesManager();

        ActionButton tpaButton = ActionButton.create(
                SettingsMessages.SETTINGS_TPA.append(stateOf(!preferences.isTpaDisabled(player))),
                SettingsMessages.SETTINGS_TPA_DESCRIPTION,
                BUTTON_WIDTH,
                privacyToggleAction(
                        plugin,
                        clickedPlayer -> clickedPlayer.sendMessage(
                                me.sbpro.cyberteleport.message.Messages.tpaToggled(
                                        preferences.toggleTpa(clickedPlayer)
                                )
                        )
                )
        );

        ActionButton tpaHereButton = ActionButton.create(
                SettingsMessages.SETTINGS_TPAHERE.append(stateOf(!preferences.isTpaHereDisabled(player))),
                SettingsMessages.SETTINGS_TPAHERE_DESCRIPTION,
                BUTTON_WIDTH,
                privacyToggleAction(
                        plugin,
                        clickedPlayer -> clickedPlayer.sendMessage(
                                me.sbpro.cyberteleport.message.Messages.tpaHereToggled(
                                        preferences.toggleTpaHere(clickedPlayer)
                                )
                        )
                )
        );

        ActionButton autoAcceptButton = ActionButton.create(
                SettingsMessages.SETTINGS_TPAUTO.append(stateOf(preferences.isAutoAcceptTpa(player))),
                SettingsMessages.SETTINGS_TPAUTO_DESCRIPTION,
                BUTTON_WIDTH,
                privacyToggleAction(
                        plugin,
                        clickedPlayer -> clickedPlayer.sendMessage(
                                preferences.toggleAutoAcceptTpa(clickedPlayer)
                                        ? me.sbpro.cyberteleport.message.Messages.tpautoEnabled()
                                        : me.sbpro.cyberteleport.message.Messages.tpautoDisabled()
                        )
                )
        );

        return createDialog(
                base,
                List.of(
                        tpaButton,
                        tpaHereButton,
                        autoAcceptButton
                ),
                createBackButton(plugin, player)
        );
    }

    private static CyberTeleport getTeleportPlugin() {

        Plugin plugin = Bukkit.getPluginManager()
                .getPlugin("CyberTeleport");

        if (plugin instanceof CyberTeleport teleportPlugin) {
            return teleportPlugin;
        }

        return null;
    }

    /*
     * ============================================================
     * MISC
     * ============================================================
     */

    private static Dialog createMiscDialog(
            CyberSurvival plugin,
            Player player
    ) {

        DialogBase base = createBase(
                "Misc",
                "Miscellaneous server options."
        );

        ActionButton enderChestButton = ActionButton.create(
                SettingsMessages.SETTINGS_ENDER_CHEST,
                SettingsMessages.SETTINGS_OPEN_YOUR_ENDER_CHEST,
                BUTTON_WIDTH,
                commandAction("enderchest")
        );

        ActionButton craftingButton = ActionButton.create(
                SettingsMessages.SETTINGS_CRAFTING_TABLE,
                SettingsMessages.SETTINGS_OPEN_THE_CRAFTING_TABLE,
                BUTTON_WIDTH,
                commandAction("craft")
        );

        return createDialog(
                base,
                List.of(
                        enderChestButton,
                        craftingButton
                ),
                createBackButton(plugin, player)
        );
    }

    /*
     * ============================================================
     * DIALOG HELPERS
     * ============================================================
     */

    private static DialogBase createBase(
            String title,
            String description
    ) {

        return DialogBase.builder(
                        Component.text(title)
                )
                .canCloseWithEscape(true)
                .pause(false)
                .afterAction(DialogBase.DialogAfterAction.NONE)
                .body(List.of(
                        DialogBody.plainMessage(
                                Component.text(description),
                                300
                        )
                ))
                .build();
    }

    private static Dialog createDialog(
            DialogBase base,
            List<ActionButton> buttons,
            ActionButton exitButton
    ) {

        return Dialog.create(factory -> {

            var builder = factory.empty();

            builder.base(base);

            builder.type(
                    DialogType.multiAction(
                            buttons,
                            exitButton,
                            2
                    )
            );
        });
    }

    /*
     * ============================================================
     * ACTIONS
     * ============================================================
     */

    private static DialogAction categoryToggleAction(
            CyberSurvival plugin,
            Consumer<Player> action,
            Function<Player, Dialog> dialogSupplier
    ) {

        return DialogAction.customClick(
                (response, audience) -> {

                    if (!(audience instanceof Player player)) {
                        return;
                    }

                    action.accept(player);

                    plugin.getServer().getScheduler().runTask(
                            plugin,
                            () -> player.showDialog(
                                    dialogSupplier.apply(player)
                            )
                    );
                },
                ClickCallback.Options.builder()
                        .uses(ClickCallback.UNLIMITED_USES)
                        .build()
        );
    }

    private static DialogAction privacyToggleAction(
            CyberSurvival plugin,
            Consumer<Player> action
    ) {

        return DialogAction.customClick(
                (response, audience) -> {

                    if (!(audience instanceof Player player)) {
                        return;
                    }

                    action.accept(player);

                    plugin.getServer().getScheduler().runTask(
                            plugin,
                            () -> player.showDialog(
                                    createPrivacyDialog(
                                            plugin,
                                            player
                                    )
                            )
                    );
                },
                ClickCallback.Options.builder()
                        .uses(ClickCallback.UNLIMITED_USES)
                        .build()
        );
    }

    private static DialogAction openDialogAction(
            Consumer<Player> action
    ) {

        return DialogAction.customClick(
                (response, audience) -> {

                    if (!(audience instanceof Player player)) {
                        return;
                    }

                    action.accept(player);
                },
                ClickCallback.Options.builder()
                        .uses(ClickCallback.UNLIMITED_USES)
                        .build()
        );
    }

    private static DialogAction commandAction(
            String command
    ) {

        return DialogAction.customClick(
                (response, audience) -> {

                    if (!(audience instanceof Player player)) {
                        return;
                    }

                    player.performCommand(command);
                },
                ClickCallback.Options.builder()
                        .uses(ClickCallback.UNLIMITED_USES)
                        .build()
        );
    }

    /*
     * ============================================================
     * NAVIGATION BUTTONS
     * ============================================================
     */

    private static ActionButton createBackButton(
            CyberSurvival plugin,
            Player player
    ) {

        return ActionButton.create(
                SettingsMessages.SETTINGS_BACK,
                SettingsMessages.SETTINGS_RETURN_TO_SETTINGS,
                BUTTON_WIDTH,
                openDialogAction(
                        clickedPlayer ->
                                clickedPlayer.showDialog(
                                        createMainDialog(plugin)
                                )
                )
        );
    }

    private static ActionButton createCloseButton() {

        return ActionButton.create(
                SettingsMessages.SETTINGS_CLOSE,
                SettingsMessages.SETTINGS_CLOSE_THE_SETTINGS_MENU,
                BUTTON_WIDTH,
                DialogAction.customClick(
                        (response, audience) -> {

                            if (!(audience instanceof Player player)) {
                                return;
                            }

                            player.closeDialog();
                        },
                        ClickCallback.Options.builder()
                                .uses(ClickCallback.UNLIMITED_USES)
                                .build()
                )
        );
    }
}