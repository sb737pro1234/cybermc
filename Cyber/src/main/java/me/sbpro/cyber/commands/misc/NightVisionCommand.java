package me.sbpro.cyber.commands.misc;

import me.sbpro.cyber.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NightVisionCommand implements CommandExecutor {

    /*
     * ============================
     *        COMMAND LOGIC
     * ============================
     */

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // /nightvision [PLAYER]
        if (args.length >= 1) {

            if (!sender.hasPermission("cyber.nightvision.others")) {
                sender.sendMessage(Messages.NO_PERMISSION_NIGHT_VISION_OTHERS);
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);

            if (target == null) {
                sender.sendMessage(Messages.PLAYER_NOT_ONLINE_PREFIXED);
                return true;
            }

            // Toggle night vision
            if (target.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {

                target.removePotionEffect(PotionEffectType.NIGHT_VISION);

                sender.sendMessage(
                        Messages.nightVisionDisabledOther(target.getName())
                );

            } else {

                target.addPotionEffect(new PotionEffect(
                        PotionEffectType.NIGHT_VISION,
                        PotionEffect.INFINITE_DURATION,
                        0,
                        false,
                        false,
                        false
                ));

                sender.sendMessage(
                        Messages.nightVisionEnabledOther(target.getName())
                );
            }

            return true;
        }

        // /nightvision
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Messages.PLAYER_ONLY);
            return true;
        }

        if (!player.hasPermission("cyber.nightvision.self")) {
            player.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        // Toggle own night vision
        if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {

            player.removePotionEffect(PotionEffectType.NIGHT_VISION);

            player.sendMessage(Messages.NIGHT_VISION_DISABLED);

        } else {

            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.NIGHT_VISION,
                    PotionEffect.INFINITE_DURATION,
                    0,
                    false,
                    false,
                    false
            ));

            player.sendMessage(Messages.NIGHT_VISION_ENABLED);
        }

        return true;
    }
}