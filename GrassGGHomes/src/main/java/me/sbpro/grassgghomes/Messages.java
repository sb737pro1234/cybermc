package me.sbpro.grassgghomes;

public final class Messages {
    private Messages() {}

    public static final String PREFIX = "§2§lHOMES §8»§f ";
    public static final String NO_PERMISSION = "§2§lHOMES §8»§c No permission.";
    public static final String PLAYER_ONLY = "§cOnly players can use this command.";
    public static String usage(String usage) { return PREFIX + "§cUsage: §f" + usage; }
    public static String homeMissing(String name) { return PREFIX + "Home §2" + name + " §fdoes not exist."; }
    public static String playerMissing(String name) { return PREFIX + "Player §2" + name + " §fhas never played or cannot be found."; }
}
