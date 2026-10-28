package me.sbpro.cyber.color;

import me.sbpro.cyber.util.LegacyText;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AnvilColorListener implements Listener {

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {

        if (event.getResult() == null) {
            return;
        }

        String renameText = event.getView().getRenameText();

        if (renameText == null || renameText.isEmpty()) {
            return;
        }

        if (!renameText.contains("&")) {
            return;
        }

        ItemStack result = event.getResult().clone();
        ItemMeta meta = result.getItemMeta();

        if (meta == null) {
            return;
        }

        meta.displayName(LegacyText.deserialize(renameText));
        result.setItemMeta(meta);

        event.setResult(result);
    }
}
