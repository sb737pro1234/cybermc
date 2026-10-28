package me.sbpro.cyber.commands.misc;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import me.sbpro.cyber.Messages;
import me.sbpro.cyber.util.LegacyText;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StaffCommand implements CommandExecutor {

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        LuckPerms luckPerms = LuckPermsProvider.get();

        List<StaffEntry> staffMembers = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {

            if (!player.hasPermission("cyber.staff")) {
                continue;
            }

            if (sender instanceof Player viewer
                    && !viewer.canSee(player)) {
                continue;
            }

            User user = luckPerms.getUserManager()
                    .getUser(player.getUniqueId());

            if (user == null) {
                continue;
            }

            Group group = luckPerms.getGroupManager()
                    .getGroup(user.getPrimaryGroup());

            int weight = 0;

            if (group != null) {
                weight = group.getWeight().orElse(0);
            }

            String prefix = user.getCachedData()
                    .getMetaData()
                    .getPrefix();

            if (prefix == null) {
                prefix = "";
            }

            staffMembers.add(
                    new StaffEntry(
                            player.getName(),
                            prefix,
                            weight
                    )
            );
        }

        staffMembers.sort(
                Comparator.comparingInt(
                        StaffEntry::weight
                ).reversed()
        );

        sender.sendMessage(Messages.staffOnlineHeader(staffMembers.size()));

        sender.sendMessage(Component.empty());

        if (staffMembers.isEmpty()) {

            sender.sendMessage(Messages.NO_STAFF_ONLINE);

            return true;
        }

        for (StaffEntry entry : staffMembers) {

            sender.sendMessage(
                    LegacyText.deserialize(entry.prefix())
                            .append(Component.text(entry.name()))
            );
        }

        return true;
    }

    private record StaffEntry(
            String name,
            String prefix,
            int weight
    ) {
    }
}