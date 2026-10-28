package me.sbpro.cyberchat.chatcolor;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import me.sbpro.cyberchat.Messages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChatColorMenu {

    public static void open(
            Player player,
            ChatColorManager manager
    ) {
        player.showDialog(
                createDialog(player, manager)
        );
    }

    private static Dialog createDialog(
            Player player,
            ChatColorManager manager
    ) {

        List<ActionButton> buttons = new ArrayList<>();

        PlayerChatColor current =
                manager.getColor(
                        player.getUniqueId()
                );

        /*
         * =========================
         * CHAT COLOURS
         * =========================
         */

        for (PlayerChatColor color :
                PlayerChatColor.values()) {

            boolean hasPermission =
                    player.hasPermission(
                            color.getPermission()
                    );

            boolean selected =
                    current == color;

            Component name = Messages.colorButton(
                    color.getDisplay(), color.getColor(), hasPermission, selected);

            ActionButton button =
                    ActionButton.create(

                            name,

                            hasPermission
                                    ? Messages.selectColorHint(color.getDisplay())
                                    : Messages.NO_PERMISSION_HINT,

                            250,

                            DialogAction.customClick(

                                    (response, audience) -> {

                                        if (!(audience
                                                instanceof Player clickedPlayer)) {
                                            return;
                                        }

                                        /*
                                         * Check the permission again
                                         * when the player clicks.
                                         */
                                        if (!clickedPlayer
                                                .hasPermission(
                                                        color.getPermission()
                                                )) {

                                            /*
                                             * Do NOT change the player's
                                             * current colour.
                                             */
                                            clickedPlayer.sendMessage(Messages.NO_PERMISSION_COLOR);

                                            clickedPlayer.playSound(
                                                    clickedPlayer
                                                            .getLocation(),
                                                    Sound.ENTITY_VILLAGER_NO,
                                                    1,
                                                    1
                                            );

                                            /*
                                             * Reopen the menu.
                                             *
                                             * Because we haven't changed
                                             * their colour, their original
                                             * colour remains selected.
                                             */
                                            open(
                                                    clickedPlayer,
                                                    manager
                                            );

                                            return;
                                        }

                                        /*
                                         * Player has permission.
                                         *
                                         * Change their colour.
                                         */
                                        manager.setColor(
                                                clickedPlayer
                                                        .getUniqueId(),
                                                color
                                        );

                                        /*
                                         * Build the success message
                                         * as an Adventure Component so
                                         * the colour name can actually
                                         * have the selected colour.
                                         */
                                        clickedPlayer.sendMessage(
                                                Messages.chatColorUpdated(color.getDisplay(), color.getColor())
                                        );

                                        clickedPlayer.playSound(
                                                clickedPlayer
                                                        .getLocation(),
                                                Sound.ENTITY_PLAYER_LEVELUP,
                                                1,
                                                1
                                        );

                                        /*
                                         * Reopen the menu with the
                                         * newly selected colour.
                                         */
                                        open(
                                                clickedPlayer,
                                                manager
                                        );
                                    },

                                    ClickCallback.Options.builder()
                                            .uses(
                                                    ClickCallback
                                                            .UNLIMITED_USES
                                            )
                                            .build()
                            )
                    );

            buttons.add(button);
        }

        /*
         * =========================
         * BOLD
         * =========================
         */

        boolean bold =
                manager.isBold(
                        player.getUniqueId()
                );

        boolean canUseBold =
                player.hasPermission(
                        "cyber.chatcolor.bold"
                );

        Component boldName = Messages.boldButton(canUseBold, bold);

        ActionButton boldButton =
                ActionButton.create(

                        boldName,

                        canUseBold
                                ? Messages.TOGGLE_BOLD_HINT
                                : Messages.NO_PERMISSION_HINT,

                        250,

                        DialogAction.customClick(

                                (response, audience) -> {

                                    if (!(audience
                                            instanceof Player clickedPlayer)) {
                                        return;
                                    }

                                    /*
                                     * Check the permission again
                                     * when clicked.
                                     */
                                    if (!clickedPlayer
                                            .hasPermission(
                                                    "cyber.chatcolor.bold"
                                            )) {

                                        clickedPlayer.sendMessage(Messages.NO_PERMISSION_BOLD);

                                        clickedPlayer.playSound(
                                                clickedPlayer
                                                        .getLocation(),
                                                Sound.ENTITY_VILLAGER_NO,
                                                1,
                                                1
                                        );

                                        open(
                                                clickedPlayer,
                                                manager
                                        );

                                        return;
                                    }

                                    /*
                                     * Toggle Bold.
                                     */
                                    boolean newValue =
                                            !manager.isBold(
                                                    clickedPlayer
                                                            .getUniqueId()
                                            );

                                    manager.setBold(
                                            clickedPlayer
                                                    .getUniqueId(),
                                            newValue
                                    );

                                    clickedPlayer.sendMessage(Messages.boldToggled(newValue));

                                    clickedPlayer.playSound(
                                            clickedPlayer
                                                    .getLocation(),
                                            Sound.ENTITY_EXPERIENCE_ORB_PICKUP,
                                            1,
                                            1
                                    );

                                    /*
                                     * Reopen the menu with the
                                     * updated Bold state.
                                     */
                                    open(
                                            clickedPlayer,
                                            manager
                                    );
                                },

                                ClickCallback.Options.builder()
                                        .uses(
                                                ClickCallback
                                                        .UNLIMITED_USES
                                        )
                                        .build()
                        )
                );

        buttons.add(boldButton);

        /*
         * =========================
         * DIALOG
         * =========================
         */

        return Dialog.create(builder -> builder
                .empty()
                .base(
                        DialogBase.builder(Messages.DIALOG_TITLE)
                                .canCloseWithEscape(true)
                                .build()
                )
                .type(
                        DialogType.multiAction(
                                buttons,
                                null,
                                2
                        )
                )
        );
    }
}