package me.sbpro.cyberstaff.commands.sign;

import me.sbpro.cyberstaff.Messages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SignCommand implements CommandExecutor {

    private static final String PERMISSION = "cyber.staff.sign";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.PLAYER_ONLY);
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission(PERMISSION)) {
            player.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType().isAir()) {
            player.sendMessage(Messages.SIGN_NEEDS_ITEM);
            return true;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            player.sendMessage(Messages.SIGN_NO_LORE);
            return true;
        }

        List<Component> lore = meta.hasLore() ? new ArrayList<>(meta.lore()) : new ArrayList<>();

        if (isAlreadySigned(lore)) {
            player.sendMessage(Messages.SIGN_ALREADY_SIGNED);
            return true;
        }

        lore.add(Component.empty());                          // extra blank line
        lore.add(Messages.signedBy(player.getName()));        // signed line

        meta.lore(lore);
        item.setItemMeta(meta);

        player.sendMessage(Messages.SIGN_SUCCESS);
        return true;
    }

    // Returns true if any existing lore line matches the signed-line prefix.
    private boolean isAlreadySigned(List<Component> lore) {
        for (Component line : lore) {
            if (PlainTextComponentSerializer.plainText().serialize(line).startsWith(Messages.SIGNED_BY_PLAIN_PREFIX)) {
                return true;
            }
        }
        return false;
    }
}