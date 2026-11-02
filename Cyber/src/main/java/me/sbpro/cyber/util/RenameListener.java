package me.sbpro.cyber.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RenameListener implements Listener {

    // Supports &0-&9, &a-&f, &k-&o, &r, and hex via &#RRGGBB
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.builder()
            .character('&')
            .hexColors()
            .build();

    /** Parses color codes and makes the name non-italic unless &o was used. */
    public static Component format(String text) {
        return LEGACY.deserialize(text)
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        ItemStack result = event.getResult();
        if (result == null || !result.hasItemMeta()) return;

        String renameText = event.getView().getRenameText();
        if (renameText == null || renameText.isBlank()) return;

        ItemMeta meta = result.getItemMeta();
        meta.displayName(format(renameText));
        result.setItemMeta(meta);
        event.setResult(result);
    }
}