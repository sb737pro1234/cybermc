package me.sbpro.cyber.commands.gamemode;

import me.sbpro.cyber.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class GameModeCommand implements CommandExecutor, TabCompleter {

    private static final Map<UUID, PendingGamemodeChange> pending = new HashMap<>();

    public static Map<UUID, PendingGamemodeChange> getPending() {
        return pending;
    }

    protected GamemodeType getForcedGamemode() {
        return null;
    }

    protected boolean isAliasCommand() {
        return false;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Messages.PLAYER_ONLY);
            return true;
        }

        GamemodeType type;

        if (isAliasCommand()) {

            type = getForcedGamemode();

        } else {

            if (args.length < 1) {
                player.sendMessage(Messages.USAGE_GAMEMODE);
                return true;
            }

            type = GamemodeType.fromString(args[0]);

            if (type == null) {
                player.sendMessage(Messages.UNKNOWN_GAMEMODE);
                return true;
            }

        }

        if (type == null) {
            player.sendMessage(Messages.UNKNOWN_GAMEMODE);
            return true;
        }

        if (!player.hasPermission("cyber.gamemode." + type.getName())) {
            player.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        // Self
        int expectedArguments = isAliasCommand() ? 0 : 1;

        if (args.length == expectedArguments) {

            player.setGameMode(type.getGameMode());

            player.sendMessage(Messages.gamemodeChangedSelf(type.getDisplayName()));

            return true;
        }

        // Others

        if (!player.hasPermission("cyber.gamemode.others")) {
            player.sendMessage(Messages.NO_PERMISSION_GAMEMODE_OTHERS);
            return true;
        }

        String targetName = isAliasCommand()
                ? args[0]
                : args[1];

        Player target = Bukkit.getPlayer(targetName);

        if (target == null) {
            player.sendMessage(Messages.ADMIN_PLAYER_NOT_ONLINE);
            return true;
        }

        GamemodeConfirmHolder holder = new GamemodeConfirmHolder();
        Inventory menu = Bukkit.createInventory(holder, 27,
                Messages.GAMEMODE_CONFIRM_TITLE);
        holder.setInventory(menu);

        ItemStack cancel = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemStack icon = new ItemStack(type.getIcon());
        ItemStack confirm = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);

        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.displayName(Messages.CANCEL);
        cancelMeta.lore(List.of(Messages.CANCEL_LORE));
        cancel.setItemMeta(cancelMeta);

        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.displayName(Messages.CONFIRM);
        confirmMeta.lore(List.of(
                Messages.GAMEMODE_LORE_THIS_WILL_CHANGE,
                Messages.gamemodeLoreTarget(target.getName()),
                Messages.gamemodeLoreMode(type.getDisplayName())
        ));
        confirm.setItemMeta(confirmMeta);

        ItemMeta iconMeta = icon.getItemMeta();
        iconMeta.displayName(Messages.gamemodeSwitchName(target.getName(), type.getDisplayName()));
        icon.setItemMeta(iconMeta);

        menu.setItem(10, cancel);
        menu.setItem(13, icon);
        menu.setItem(16, confirm);

        pending.put(player.getUniqueId(),
                new PendingGamemodeChange(target, type));

        player.openInventory(menu);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,
                                      Command command,
                                      String alias,
                                      String[] args) {

        // Alias commands (/gmc, /gms, etc.)
        if (isAliasCommand()) {

            if (args.length == 1) {
                return Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                        .sorted()
                        .toList();
            }

            return Collections.emptyList();
        }

        // /gamemode
        if (args.length == 1) {
            return Arrays.asList(
                            "creative",
                            "survival",
                            "adventure",
                            "spectator"
                    ).stream()
                    .filter(mode -> mode.startsWith(args[0].toLowerCase()))
                    .toList();
        }

        if (args.length == 2) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .sorted()
                    .toList();
        }

        return Collections.emptyList();
    }

}