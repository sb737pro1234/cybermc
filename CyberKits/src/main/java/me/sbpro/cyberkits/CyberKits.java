package me.sbpro.cyberkits;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import me.sbpro.cyberkits.commands.KitsCommand;
import me.sbpro.cyberkits.kit.CooldownManager;
import me.sbpro.cyberkits.kit.FirstJoinListener;
import me.sbpro.cyberkits.kit.Kit;
import me.sbpro.cyberkits.kit.KitClaimer;
import me.sbpro.cyberkits.kit.KitItemManager;
import me.sbpro.cyberkits.menu.KitEditorHolder;
import me.sbpro.cyberkits.menu.KitEditorSelectHolder;
import me.sbpro.cyberkits.menu.KitMenuListener;
import me.sbpro.cyberkits.menu.KitPreviewHolder;
import me.sbpro.cyberkits.menu.KitsMenuHolder;

public final class CyberKits extends JavaPlugin {

    private KitItemManager kitItems;
    private CooldownManager cooldowns;
    private KitClaimer claimer;

    @Override
    public void onEnable() {
        kitItems = new KitItemManager(this);
        cooldowns = new CooldownManager(this);
        claimer = new KitClaimer(kitItems, cooldowns);

        KitsCommand kitsCommand = new KitsCommand(this);
        getCommand("kits").setExecutor(kitsCommand);
        getCommand("kits").setTabCompleter(kitsCommand);

        getServer().getPluginManager().registerEvents(new KitMenuListener(this), this);
        getServer().getPluginManager().registerEvents(new FirstJoinListener(this), this);

        // Keep cooldown timers in open /kits menus counting down
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getOpenInventory().getTopInventory().getHolder(false) instanceof KitsMenuHolder menu) {
                    menu.render();
                }
            }
        }, 20L, 20L);
    }

    @Override
    public void onDisable() {
        // Save any kit still open in the editor, then close menus without the close listener saving twice
        HandlerList.unregisterAll(this);
        for (Player player : Bukkit.getOnlinePlayers()) {
            Object holder = player.getOpenInventory().getTopInventory().getHolder(false);
            if (holder instanceof KitEditorHolder editor) saveEditor(player, editor);
            if (holder instanceof KitsMenuHolder || holder instanceof KitPreviewHolder
                    || holder instanceof KitEditorSelectHolder || holder instanceof KitEditorHolder) {
                player.closeInventory();
            }
        }

        if (cooldowns != null) cooldowns.save();
    }

    /** /kits reload: re-reads kititems.yml and cooldowns.yml from disk. */
    public void reload() {
        kitItems.load();
        cooldowns.load();
    }

    public void openMenu(Player player) {
        player.openInventory(new KitsMenuHolder(this, player).getInventory());
    }

    public void openPreview(Player player, Kit kit) {
        player.openInventory(new KitPreviewHolder(kit, kitItems).getInventory());
    }

    public void openEditorSelect(Player player) {
        player.openInventory(new KitEditorSelectHolder(kitItems).getInventory());
    }

    public void openEditor(Player player, Kit kit) {
        player.openInventory(new KitEditorHolder(kit, kitItems).getInventory());
        player.sendMessage(Messages.editorOpened(kit));
    }

    public void saveEditor(HumanEntity player, KitEditorHolder editor) {
        kitItems.set(editor.kit(), editor.getInventory().getContents());
        player.sendMessage(Messages.editorSaved(editor.kit(), kitItems.count(editor.kit())));
    }

    public KitItemManager kitItems() {
        return kitItems;
    }

    public CooldownManager cooldowns() {
        return cooldowns;
    }

    public KitClaimer claimer() {
        return claimer;
    }
}
