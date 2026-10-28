package me.sbpro.cyberteleport.message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import me.sbpro.cyberteleport.request.TeleportRequestType;
import org.bukkit.entity.Player;

public final class Messages {

    public static final TextColor CUSTOM = TextColor.color(0x00A8FF);

    public static final Component PREFIX = Component.text()
            .append(Component.text("TELEPORT", CUSTOM, TextDecoration.BOLD))
            .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
            .build();

    // Standard errors (unprefixed)
    public static final Component PLAYER_ONLY = Component.text(
            "You must be a player to use this command.",
            NamedTextColor.RED
    );

    public static final Component NO_PERMISSION = Component.text(
            "You do not have permission to use this command.",
            NamedTextColor.RED
    );

    // Menus
    public static final Component CONFIRMATION_TITLE = Component.text("Confirm Teleport", NamedTextColor.WHITE);
    public static final Component CONFIRM_CANCEL = Component.text("Cancel", NamedTextColor.RED);
    public static final Component CONFIRM_ACCEPT = Component.text("Confirm", NamedTextColor.GREEN);

    // Action bar
    public static final Component TPAUTO_ACTION_BAR = Component.text("TPAUTO IS ENABLED", CUSTOM, TextDecoration.BOLD);

    private Messages() {
    }

    private static Component error(String text) {
        return PREFIX.append(Component.text(text, NamedTextColor.RED));
    }

    private static Component info(String text) {
        return PREFIX.append(Component.text(text, NamedTextColor.WHITE));
    }

    // ---------------------------------------------------------------
    // Teleporting
    // ---------------------------------------------------------------

    public static Component teleportingIn(int seconds) {
        String secondText = seconds == 1 ? "second" : "seconds";

        return PREFIX
                .append(Component.text("Teleporting in ", NamedTextColor.WHITE))
                .append(Component.text(seconds, CUSTOM))
                .append(Component.text(" " + secondText + ".", NamedTextColor.WHITE));
    }

    public static Component teleportStarted() {
        return info("Teleport started.");
    }

    public static Component teleportSuccessful() {
        return info("Teleport successful.");
    }

    public static Component teleportCancelled() {
        return error("Teleport cancelled.");
    }

    public static Component teleportCancelledMovement() {
        return error("Teleport cancelled due to movement.");
    }

    public static Component teleportedPlayersToYou(int count) {
        return PREFIX
                .append(Component.text("Teleported ", NamedTextColor.WHITE))
                .append(Component.text(count, CUSTOM))
                .append(Component.text(" players to you.", NamedTextColor.WHITE));
    }

    public static Component confirmationAction(String action) {
        return Component.text("This will " + action, CUSTOM);
    }

    // ---------------------------------------------------------------
    // Requests
    // ---------------------------------------------------------------

    public static Component requestSent(Player target) {
        return PREFIX
                .append(Component.text("Teleport request sent to ", NamedTextColor.WHITE))
                .append(Component.text(target.getName(), CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component requestAlreadySent(Player target) {
        return error("You have already sent a teleport request to " + target.getName() + ".");
    }

    public static Component requestExpiredTo(Player target) {
        return error("Your teleport request to " + target.getName() + " has expired.");
    }

    public static Component requestExpiredFrom(Player requester) {
        return error("The teleport request from " + requester.getName() + " has expired.");
    }

    public static Component requestReceived(Player sender) {
        return PREFIX
                .append(Component.text(sender.getName(), CUSTOM))
                .append(Component.text(" has requested to teleport to you.", NamedTextColor.WHITE));
    }

    public static Component requestAccepted(Player sender) {
        return PREFIX
                .append(Component.text("Teleport request from ", NamedTextColor.WHITE))
                .append(Component.text(sender.getName(), CUSTOM))
                .append(Component.text(" accepted.", NamedTextColor.WHITE));
    }

    public static Component requestDenied(Player sender) {
        return error("Teleport request from " + sender.getName() + " denied.");
    }

    public static Component requestCancelled() {
        return error("Teleport request cancelled.");
    }

    public static Component requestExpired() {
        return error("Teleport request expired.");
    }

    public static Component noPendingRequest() {
        return error("You do not have a pending teleport request.");
    }

    public static Component noPendingRequestFrom(String requesterName) {
        return error("You do not have a pending teleport request from " + requesterName + ".");
    }

    public static Component noOutgoingRequests() {
        return error("You do not have any outgoing teleport requests.");
    }

    public static Component playerNotFound() {
        return error("Player not found.");
    }

    public static Component playerOffline(Player player) {
        return error(player.getName() + " is no longer online.");
    }

    public static Component playerNoLongerOnline() {
        return error("That player is no longer online.");
    }

    public static Component requestsBlocked(Player target, TeleportRequestType type) {
        return error(target.getName() + (type == TeleportRequestType.TPA
                ? " is not accepting requests to teleport to them."
                : " is not accepting requests to be teleported to you."));
    }

    public static Component requestSentToTeleportToYou(Player target) {
        return PREFIX
                .append(Component.text("Teleport request sent to ", NamedTextColor.WHITE))
                .append(Component.text(target.getName(), CUSTOM))
                .append(Component.text(" to teleport to you.", NamedTextColor.WHITE));
    }

    public static Component requestReceivedToYou(Player requester) {
        return PREFIX
                .append(Component.text(requester.getName(), CUSTOM))
                .append(Component.text(" has requested you to teleport to them.", NamedTextColor.WHITE));
    }

    public static Component requestCancelledByRequester(Player requester) {
        return error("Teleport request from " + requester.getName() + " was cancelled.");
    }

    public static Component requestAutoAccepted(Player target) {
        return PREFIX
                .append(Component.text("Your teleport request to ", NamedTextColor.WHITE))
                .append(Component.text(target.getName(), CUSTOM))
                .append(Component.text(" was automatically accepted.", NamedTextColor.WHITE));
    }

    // ---------------------------------------------------------------
    // Settings
    // ---------------------------------------------------------------

    public static Component tpautoEnabled() {
        return PREFIX
                .append(Component.text("TPAuto has been ", NamedTextColor.WHITE))
                .append(Component.text("enabled", CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component tpautoDisabled() {
        return PREFIX
                .append(Component.text("TPAuto has been ", NamedTextColor.WHITE))
                .append(Component.text("disabled", CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    /** /tpatoggle: players asking you to teleport to them (/tpahere). */
    public static Component tpaToggled(boolean disabled) {
        return PREFIX
                .append(Component.text("TPA has been ", NamedTextColor.WHITE))
                .append(Component.text(disabled ? "disabled" : "enabled", CUSTOM))
                .append(Component.text(". Players ", NamedTextColor.WHITE))
                .append(Component.text(disabled ? "can no longer" : "can now", CUSTOM))
                .append(Component.text(" request that you teleport to them.", NamedTextColor.WHITE));
    }

    /** /tpaheretoggle: players asking to teleport to you (/tpa). */
    public static Component tpaHereToggled(boolean disabled) {
        return PREFIX
                .append(Component.text("TPAHere has been ", NamedTextColor.WHITE))
                .append(Component.text(disabled ? "disabled" : "enabled", CUSTOM))
                .append(Component.text(". Players ", NamedTextColor.WHITE))
                .append(Component.text(disabled ? "can no longer" : "can now", CUSTOM))
                .append(Component.text(" request to teleport to you.", NamedTextColor.WHITE));
    }

    // ---------------------------------------------------------------
    // Spectating
    // ---------------------------------------------------------------

    public static Component spectating(Player target) {
        return PREFIX
                .append(Component.text("You are now spectating ", NamedTextColor.WHITE))
                .append(Component.text(target.getName(), CUSTOM))
                .append(Component.text(".", NamedTextColor.WHITE));
    }

    public static Component spectateStopped() {
        return info("You are no longer spectating.");
    }

    public static Component notSpectating() {
        return error("You are not currently spectating a player.");
    }

    public static Component cannotSpectateSelf() {
        return error("You cannot spectate yourself.");
    }

    // ---------------------------------------------------------------
    // Errors and usage
    // ---------------------------------------------------------------

    public static Component invalidUsage(String usage) {
        return PREFIX
                .append(Component.text("Usage: ", NamedTextColor.RED))
                .append(Component.text(usage, CUSTOM));
    }

    public static Component invalidCoordinates() {
        return error("Invalid coordinates.");
    }

    public static Component selfTargeting() {
        return error("You cannot teleport to yourself.");
    }

    public static Component cannotRequestSelf() {
        return error("You cannot send a teleport request to yourself.");
    }

    public static Component cannotTeleportPlayerToThemselves() {
        return error("You cannot teleport a player to themselves.");
    }

    public static Component selectorMustSelectOnePlayer() {
        return error("That selector must select exactly one player.");
    }

    public static Component invalidSelector() {
        return error("Invalid selector.");
    }

    public static Component noPlayersToTeleport() {
        return error("There are no players to teleport.");
    }

    public static Component noPermissionTeleportOthers() {
        return error("You do not have permission to teleport other players.");
    }

    public static Component noPermissionCoordinates() {
        return error("You do not have permission to use coordinate teleportation.");
    }
}
