package me.sbpro.cyberfastplace.commands;

import me.sbpro.cyberfastplace.Messages;
import me.sbpro.cyberfastplace.manager.FastPlaceManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FPCommand implements CommandExecutor {
    private final FastPlaceManager manager;
    private static final int[] VALID_TIERS = {16, 32, 64, 128};

    public FPCommand(FastPlaceManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.PLAYER_ONLY);
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            this.manager.disable(player.getUniqueId());
            player.sendMessage(Messages.FASTPLACE_DISABLED);
            return true;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage(Messages.USAGE);
            return true;
        }

        int maxAllowed = this.getMaxAmountForPlayer(player);
        if (maxAllowed <= 0) {
            player.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (amount < 1 || amount > maxAllowed) {
            player.sendMessage(Messages.usageMax(maxAllowed));
            return true;
        }

        this.manager.enable(player.getUniqueId(), amount);
        player.sendMessage(Messages.fastplaceEnabled(amount));
        return true;
    }

    private int getMaxAmountForPlayer(Player player) {
        for (int i = VALID_TIERS.length - 1; i >= 0; i--) {
            int tier = VALID_TIERS[i];
            if (player.hasPermission("cyber.fastplace.use." + tier)) {
                return tier;
            }
        }
        if (player.hasPermission("cyber.fastplace.use")) {
            return 16;
        }
        return 0;
    }
}
