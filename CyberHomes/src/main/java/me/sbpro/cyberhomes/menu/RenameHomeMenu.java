package me.sbpro.cyberhomes.menu;

import me.sbpro.cyberhomes.Messages;
import me.sbpro.cyberhomes.CyberHomes;
import me.sbpro.cyberhomes.model.Home;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RenameHomeMenu {

    private final CyberHomes plugin;

    private final Player viewer;

    private final Player target;

    private final int homeNumber;

    public RenameHomeMenu(
            CyberHomes plugin,
            Player viewer,
            Player target,
            int homeNumber
    ) {

        this.plugin = plugin;
        this.viewer = viewer;
        this.target = target;
        this.homeNumber = homeNumber;
    }

    public void open() {

        Home home =
                plugin.getHomesManager().getHome(
                        target.getUniqueId(),
                        homeNumber
                );

        if (home == null) {

            viewer.sendMessage(Messages.HOME_NOT_EXIST);

            return;
        }

        String currentName =
                home.getName() == null || home.getName().isBlank()
                        ? "Home " + homeNumber
                        : home.getName();

        new AnvilGUI.Builder()

                .plugin(plugin)

                .jsonTitle(GsonComponentSerializer.gson().serialize(Messages.RENAME_TITLE))

                .itemLeft(new ItemStack(Material.NAME_TAG))

                .text(currentName)

                .onClick((slot, snapshot) -> {

                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return java.util.Collections.emptyList();
                    }

                    String newName =
                            snapshot.getText().trim();

                    if (newName.isBlank()) {

                        return java.util.List.of(
                                AnvilGUI.ResponseAction.replaceInputText(currentName)
                        );
                    }

                    if (newName.length() > 24) {

                        return java.util.List.of(
                                AnvilGUI.ResponseAction.replaceInputText(
                                        newName.substring(0, 24)
                                )
                        );
                    }

                    plugin.getHomesManager().renameHome(
                            target.getUniqueId(),
                            homeNumber,
                            newName
                    );

                    return java.util.List.of(

                            AnvilGUI.ResponseAction.close(),

                            AnvilGUI.ResponseAction.run(() -> {

                                viewer.sendMessage(Messages.homeRenamed(newName));

                                new HomesMenu(
                                        plugin,
                                        viewer,
                                        target
                                ).open();
                            })
                    );

                })

                .open(viewer);
    }

}