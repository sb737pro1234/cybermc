package me.sbpro.cyberkits.kit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import me.sbpro.cyberkits.Kits;

/** Stores the items in each kit, in kititems.yml (kits.&lt;id&gt;.&lt;editor slot&gt;: item). */
public final class KitItemManager {

    /** Number of item slots in the /kits editor (and the preview). */
    public static final int EDITOR_SIZE = 45;

    private final Plugin plugin;
    private final File file;
    private final Map<String, Map<Integer, ItemStack>> items = new HashMap<>();

    public KitItemManager(Plugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "kititems.yml");
        load();
    }

    /** Editor layout of the kit (EDITOR_SIZE slots, empty slots null). Items are copies. */
    public ItemStack[] layout(Kit kit) {
        ItemStack[] contents = new ItemStack[EDITOR_SIZE];
        items.getOrDefault(kit.id(), Map.of()).forEach((slot, item) -> contents[slot] = item.clone());
        return contents;
    }

    /** The items to give, in editor order. Items are copies. */
    public List<ItemStack> items(Kit kit) {
        List<ItemStack> list = new ArrayList<>();
        new TreeMap<>(items.getOrDefault(kit.id(), Map.of())).values().forEach(item -> list.add(item.clone()));
        return list;
    }

    public int count(Kit kit) {
        return items.getOrDefault(kit.id(), Map.of()).size();
    }

    /** Replaces the kit's items with the given editor contents and saves. */
    public void set(Kit kit, ItemStack[] contents) {
        Map<Integer, ItemStack> slots = new HashMap<>();
        for (int slot = 0; slot < Math.min(contents.length, EDITOR_SIZE); slot++) {
            ItemStack item = contents[slot];
            if (item != null && !item.isEmpty()) slots.put(slot, item.clone());
        }
        items.put(kit.id(), slots);
        save();
    }

    public void load() {
        items.clear();
        if (!file.exists()) return;

        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        for (Kit kit : Kits.KITS) {
            ConfigurationSection section = yaml.getConfigurationSection("kits." + kit.id());
            if (section == null) continue;

            Map<Integer, ItemStack> slots = new HashMap<>();
            for (String key : section.getKeys(false)) {
                try {
                    int slot = Integer.parseInt(key);
                    ItemStack item = section.getItemStack(key);
                    if (slot >= 0 && slot < EDITOR_SIZE && item != null && !item.isEmpty()) slots.put(slot, item);
                } catch (NumberFormatException e) {
                    plugin.getLogger().warning("Skipping invalid slot '" + key + "' for kit " + kit.id() + " in kititems.yml");
                }
            }
            items.put(kit.id(), slots);
        }
    }

    public void save() {
        YamlConfiguration yaml = new YamlConfiguration();
        items.forEach((id, slots) -> {
            yaml.createSection("kits." + id);
            slots.forEach((slot, item) -> yaml.set("kits." + id + "." + slot, item));
        });

        try {
            plugin.getDataFolder().mkdirs();
            yaml.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save kititems.yml: " + e.getMessage());
        }
    }
}
