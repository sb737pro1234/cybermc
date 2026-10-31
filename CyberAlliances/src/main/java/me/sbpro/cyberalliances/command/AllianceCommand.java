package me.sbpro.cyberalliances.command;

import me.sbpro.cyberalliances.Messages;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import me.sbpro.cyberalliances.ChatToggleService;
import me.sbpro.cyberalliances.CyberAlliances;
import me.sbpro.cyberalliances.InviteService;
import me.sbpro.cyberalliances.data.AllianceStorage;
import me.sbpro.cyberalliances.gui.AllianceInfoGui;
import me.sbpro.cyberalliances.gui.AllianceLevelGui;
import me.sbpro.cyberalliances.gui.DeleteAllianceGui;
import me.sbpro.cyberalliances.model.Alliance;
import me.sbpro.cyberalliances.service.AllianceBuffService;
import me.sbpro.cyberalliances.service.AllianceXpService;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public final class AllianceCommand
implements CommandExecutor,
TabCompleter {
    private static final int MAX_MEMBERS = 5;
    private static final Pattern HEX_PATTERN = Pattern.compile("(?i)(#[0-9a-f]{6}|&x(&[0-9a-f]){6})");
    private static final Pattern AMPERSAND_COLOR_PATTERN = Pattern.compile("(?i)&[0-9a-fk-or]");
    private static final Pattern VALID_NAME_PATTERN = Pattern.compile("[A-Za-z0-9 _-]+");
    private final CyberAlliances plugin;
    private final AllianceStorage allianceStorage;
    private final ChatToggleService chatToggleService;
    private final InviteService inviteService;
    private final AllianceBuffService buffService;
    private final AllianceXpService xpService;

    public AllianceCommand(CyberAlliances plugin, AllianceStorage allianceStorage, ChatToggleService chatToggleService, InviteService inviteService, AllianceBuffService buffService, AllianceXpService xpService) {
        this.plugin = plugin;
        this.allianceStorage = allianceStorage;
        this.chatToggleService = chatToggleService;
        this.inviteService = inviteService;
        this.buffService = buffService;
        this.xpService = xpService;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.PLAYER_ONLY);
            return true;
        }
        Player player = (Player)sender;
        if (args.length == 0) {
            this.openAllianceInfo(player);
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "create": {
                this.handleCreate(player, args);
                break;
            }
            case "info": {
                this.handleInfo(player, args);
                break;
            }
            case "delete": {
                this.handleDelete(player);
                break;
            }
            case "chat": {
                this.handleChat(player);
                break;
            }
            case "invite": {
                this.handleInvite(player, args);
                break;
            }
            case "accept": {
                this.handleAccept(player);
                break;
            }
            case "kick": {
                this.handleKick(player, args);
                break;
            }
            case "leave": {
                this.handleLeave(player);
                break;
            }
            case "top": {
                this.handleTop(player);
                break;
            }
            case "level": {
                this.handleLevel(player);
                break;
            }
            default: {
                player.sendMessage(Messages.USAGE);
            }
        }
        return true;
    }

    private void handleCreate(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Messages.CREATE_USAGE);
            return;
        }
        if (this.allianceStorage.getAllianceByPlayer(player.getUniqueId()).isPresent()) {
            player.sendMessage(Messages.ALREADY_IN_ALLIANCE);
            return;
        }
        String name = String.join((CharSequence)" ", Arrays.copyOfRange(args, 1, args.length)).trim();
        if (name.length() < 3 || name.length() > 24) {
            player.sendMessage(Messages.NAME_LENGTH);
            return;
        }
        if (HEX_PATTERN.matcher(name).find() || AMPERSAND_COLOR_PATTERN.matcher(name).find() || !VALID_NAME_PATTERN.matcher(name).matches()) {
            player.sendMessage(Messages.INVALID_NAME);
            return;
        }
        if (this.allianceStorage.getAllianceByName(name).isPresent()) {
            player.sendMessage(Messages.ALLIANCE_EXISTS);
            return;
        }
        if (this.allianceStorage.createAlliance(name, player.getUniqueId())) {
            this.buffService.refreshPlayer(player);
            player.sendMessage(Messages.created(name));
        }
    }

    private void handleInfo(Player player, String[] args) {
        if (args.length < 2) {
            this.openAllianceInfo(player);
            return;
        }
        String input = String.join((CharSequence)" ", Arrays.copyOfRange(args, 1, args.length)).trim();
        Optional<Alliance> allianceOptional = this.findAlliance(input);
        if (allianceOptional.isEmpty()) {
            player.sendMessage(Messages.notFound(input));
            return;
        }
        Alliance alliance = allianceOptional.get();
        OfflinePlayer owner = Bukkit.getOfflinePlayer((UUID)alliance.getOwner());
        String ownerName = owner.getName() == null ? alliance.getOwner().toString() : owner.getName();
        String members = alliance.getMembers().stream().map(Bukkit::getOfflinePlayer).map(offline -> offline.getName() == null ? offline.getUniqueId().toString() : offline.getName()).sorted(String.CASE_INSENSITIVE_ORDER).reduce((left, right) -> left + ", " + right).orElse(ownerName);
        for (Component line : Messages.allianceInfo(alliance.getName(), ownerName, members)) {
            player.sendMessage(line);
        }
    }

    private void openAllianceInfo(Player player) {
        Optional<Alliance> allianceOptional = this.allianceStorage.getAllianceByPlayer(player.getUniqueId());
        if (allianceOptional.isEmpty()) {
            player.sendMessage(Messages.NO_ALLIANCE);
            return;
        }
        AllianceInfoGui.open(this.plugin, player, allianceOptional.get());
    }

    private Optional<Alliance> findAlliance(String input) {
        Optional<Alliance> byName = this.allianceStorage.getAllianceByName(input);
        if (byName.isPresent()) {
            return byName;
        }
        Player online = Bukkit.getPlayerExact((String)input);
        if (online != null) {
            return this.allianceStorage.getAllianceByPlayer(online.getUniqueId());
        }
        OfflinePlayer cached = Bukkit.getOfflinePlayerIfCached((String)input);
        if (cached != null && (cached.getName() != null || cached.hasPlayedBefore())) {
            return this.allianceStorage.getAllianceByPlayer(cached.getUniqueId());
        }
        return Optional.empty();
    }

    private void handleDelete(Player player) {
        Optional<Alliance> allianceOptional = this.allianceStorage.getAllianceByPlayer(player.getUniqueId());
        if (allianceOptional.isEmpty()) {
            player.sendMessage(Messages.NO_ALLIANCE);
            return;
        }
        Alliance alliance = allianceOptional.get();
        if (!alliance.getOwner().equals(player.getUniqueId())) {
            player.sendMessage(Messages.OWNER_ONLY_DELETE);
            return;
        }
        DeleteAllianceGui.open(this.plugin, player, alliance.getName());
        player.sendMessage(Messages.DELETE_OPENED);
    }

    private void handleChat(Player player) {
        if (this.allianceStorage.getAllianceByPlayer(player.getUniqueId()).isEmpty()) {
            player.sendMessage(Messages.NO_ALLIANCE);
            return;
        }
        boolean enabled = this.chatToggleService.toggle(player.getUniqueId());
        player.sendMessage((enabled ? Messages.CHAT_ENABLED : Messages.CHAT_DISABLED));
    }

    private void handleInvite(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Messages.INVITE_USAGE);
            return;
        }
        Optional<Alliance> allianceOptional = this.allianceStorage.getAllianceByPlayer(player.getUniqueId());
        if (allianceOptional.isEmpty()) {
            player.sendMessage(Messages.NO_ALLIANCE);
            return;
        }
        Alliance alliance = allianceOptional.get();
        if (!alliance.getOwner().equals(player.getUniqueId())) {
            player.sendMessage(Messages.OWNER_ONLY_INVITE);
            return;
        }
        if (alliance.getMembers().size() >= 5) {
            player.sendMessage(Messages.ALLIANCE_FULL);
            return;
        }
        Player target = Bukkit.getPlayerExact((String)args[1]);
        if (target == null) {
            player.sendMessage(Messages.TARGET_NOT_ONLINE);
            return;
        }
        if (this.allianceStorage.getAllianceByPlayer(target.getUniqueId()).isPresent()) {
            player.sendMessage(Messages.TARGET_ALREADY_IN_ALLIANCE);
            return;
        }
        this.inviteService.setInvite(target.getUniqueId(), alliance.getName());
        player.sendMessage(Messages.inviteSent(target.getName()));
        target.sendMessage(Messages.inviteReceived(alliance.getName(), player.getName()));
    }

    private void handleAccept(Player player) {
        if (this.allianceStorage.getAllianceByPlayer(player.getUniqueId()).isPresent()) {
            player.sendMessage(Messages.ALREADY_IN_ALLIANCE);
            this.inviteService.clearInvite(player.getUniqueId());
            return;
        }
        Optional<String> inviteOptional = this.inviteService.getInvite(player.getUniqueId());
        if (inviteOptional.isEmpty()) {
            player.sendMessage(Messages.NO_PENDING_INVITE);
            return;
        }
        Optional<Alliance> allianceOptional = this.allianceStorage.getAllianceByName(inviteOptional.get());
        if (allianceOptional.isEmpty()) {
            this.inviteService.clearInvite(player.getUniqueId());
            player.sendMessage(Messages.INVITE_EXPIRED);
            return;
        }
        Alliance alliance = allianceOptional.get();
        if (alliance.getMembers().size() >= 5) {
            this.inviteService.clearInvite(player.getUniqueId());
            player.sendMessage(Messages.ALLIANCE_FULL);
            return;
        }
        if (this.allianceStorage.addMember(alliance.getName(), player.getUniqueId())) {
            this.inviteService.clearInvite(player.getUniqueId());
            this.buffService.refreshPlayer(player);
            player.sendMessage(Messages.joined(alliance.getName()));
            for (UUID memberId : this.allianceStorage.getAllianceByName(alliance.getName()).orElse(alliance).getMembers()) {
                Player member = Bukkit.getPlayer((UUID)memberId);
                if (member == null || member.getUniqueId().equals(player.getUniqueId())) continue;
                member.sendMessage(Messages.memberJoined(player.getName()));
            }
        } else {
            player.sendMessage(Messages.INVITE_EXPIRED);
        }
    }

    private void handleKick(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Messages.KICK_USAGE);
            return;
        }
        Optional<Alliance> allianceOptional = this.allianceStorage.getAllianceByPlayer(player.getUniqueId());
        if (allianceOptional.isEmpty()) {
            player.sendMessage(Messages.NO_ALLIANCE);
            return;
        }
        Alliance alliance = allianceOptional.get();
        if (!alliance.getOwner().equals(player.getUniqueId())) {
            player.sendMessage(Messages.OWNER_ONLY_KICK);
            return;
        }
        Player target = Bukkit.getPlayerExact((String)args[1]);
        if (target == null) {
            player.sendMessage(Messages.TARGET_NOT_ONLINE);
            return;
        }
        if (target.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(Messages.CANNOT_KICK_SELF);
            return;
        }
        Optional<Alliance> targetAlliance = this.allianceStorage.getAllianceByPlayer(target.getUniqueId());
        if (targetAlliance.isEmpty() || !targetAlliance.get().getName().equalsIgnoreCase(alliance.getName())) {
            player.sendMessage(Messages.PLAYER_NOT_IN_YOUR_ALLIANCE);
            return;
        }
        if (targetAlliance.get().getOwner().equals(target.getUniqueId())) {
            player.sendMessage(Messages.CANNOT_KICK_OWNER);
            return;
        }
        if (this.allianceStorage.removeMember(alliance.getName(), target.getUniqueId())) {
            this.chatToggleService.disable(target.getUniqueId());
            this.buffService.clearPlayer(target);
            target.sendMessage(Messages.kickedTarget(alliance.getName()));
            player.sendMessage(Messages.kickedByOwner(target.getName()));
            for (UUID memberId : this.allianceStorage.getAllianceByName(alliance.getName()).orElse(alliance).getMembers()) {
                Player member = Bukkit.getPlayer((UUID)memberId);
                if (member == null || member.getUniqueId().equals(player.getUniqueId())) continue;
                member.sendMessage(Messages.kickedBroadcast(target.getName()));
            }
        }
    }

    private void handleLeave(Player player) {
        Optional<Alliance> allianceOptional = this.allianceStorage.getAllianceByPlayer(player.getUniqueId());
        if (allianceOptional.isEmpty()) {
            player.sendMessage(Messages.NO_ALLIANCE);
            return;
        }

        Alliance alliance = allianceOptional.get();

        if (alliance.getOwner().equals(player.getUniqueId())) {
            player.sendMessage(Messages.OWNER_ONLY_LEAVE);
            return;
        }

        String allianceName = alliance.getName();
        if (!this.allianceStorage.removeMember(allianceName, player.getUniqueId())) {
            player.sendMessage(Messages.LEAVE_FAILED);
            return;
        }

        this.chatToggleService.disable(player.getUniqueId());
        this.buffService.clearPlayer(player);

        player.sendMessage(Messages.left(allianceName));

        Optional<Alliance> updatedAlliance = this.allianceStorage.getAllianceByName(allianceName);
        if (updatedAlliance.isPresent()) {
            for (UUID memberId : updatedAlliance.get().getMembers()) {
                Player member = Bukkit.getPlayer(memberId);
                if (member != null) {
                    member.sendMessage(Messages.memberLeft(player.getName()));
                }
            }
        }
    }

    private void handleTop(Player player) {
        List<Alliance> topAlliances = this.xpService.getTopAlliances();
        if (topAlliances.isEmpty()) {
            player.sendMessage(Messages.TOP_EMPTY);
            return;
        }
        for (Component line : Messages.TOP_HEADER) {
            player.sendMessage(line);
        }
        int position = 1;
        for (Alliance alliance : topAlliances.stream().limit(10L).toList()) {
            int currentPosition = position++;
            player.sendMessage(Messages.topEntry(currentPosition, alliance.getName(), alliance.getLevel(), Math.round(alliance.getXp())));
        }
    }

    private void handleLevel(Player player) {
        Optional<Alliance> allianceOptional = this.allianceStorage.getAllianceByPlayer(player.getUniqueId());
        if (allianceOptional.isEmpty()) {
            player.sendMessage(Messages.NO_ALLIANCE);
            return;
        }
        AllianceLevelGui.openMain(this.plugin, player, allianceOptional.get());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return List.of();
        }

        if (args.length == 1) {
            String input = args[0].toLowerCase();
            return List.of("create", "info", "delete", "chat", "invite", "accept", "kick", "leave", "top", "level")
                    .stream()
                    .filter(option -> option.startsWith(input))
                    .toList();
        }

        String subcommand = args[0].toLowerCase();
        if (args.length == 2 && (subcommand.equals("invite") || subcommand.equals("kick"))) {
            String input = args[1].toLowerCase();
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(input))
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .toList();
        }

        if (args.length >= 2 && subcommand.equals("info")) {
            String input = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length)).toLowerCase();
            return this.allianceStorage.getAlliances().stream()
                    .map(Alliance::getName)
                    .filter(name -> name.toLowerCase().startsWith(input))
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .toList();
        }

        return List.of();
    }
}

