package me.sbpro.cyberhub.items.selector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.kyori.adventure.text.Component;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import me.sbpro.cyberhub.Messages;

/** The 45-slot server selector menu. */
public final class ServerSelectorHolder implements InventoryHolder {

    private static final int SIZE = 45;

    /**
     * What a menu slot does when clicked.
     * @param server proxy server to send the player to, or null for none
     * @param displayName name shown in the "Sending you to..." message
     * @param close whether to close the menu on click
     */
    public record Entry(String server, String displayName, boolean close) {}

    private final Inventory inventory;
    private final Map<Integer, Entry> entries = new HashMap<>();

    public ServerSelectorHolder() {
        this.inventory = Bukkit.createInventory(this, SIZE, Messages.SELECTOR_TITLE);

        add(11, Material.OAK_SAPLING, Messages.SELECTOR_SURVIVAL_NAME, Messages.SELECTOR_SURVIVAL_LORE,
                new Entry("survival", "Survival", true));

        add(15, Material.COMPASS, Messages.SELECTOR_HUB_NAME, Messages.SELECTOR_HUB_LORE,
                new Entry("hub", "Hub", false));

        add(29, Material.BARRIER, Messages.SELECTOR_UNAVAILABLE_NAME, List.of(),
                new Entry(null, null, true));

        add(33, Material.BARRIER, Messages.SELECTOR_UNAVAILABLE_NAME, List.of(),
                new Entry(null, null, true));
    }

    private void add(int slot, Material material, Component name, List<Component> lore, Entry entry) {
        ItemStack item = new ItemStack(material);

        item.editMeta(meta -> {
            meta.displayName(name);
            if (!lore.isEmpty()) meta.lore(lore);
        });

        inventory.setItem(slot, item);
        entries.put(slot, entry);
    }

    public Entry entry(int slot) {
        return entries.get(slot);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
