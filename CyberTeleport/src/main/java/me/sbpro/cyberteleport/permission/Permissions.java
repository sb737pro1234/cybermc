package me.sbpro.cyberteleport.permission;

public final class Permissions {

    public static final String BASE = "cyber.teleport";

    public static final String TP = "cyber.teleport.tp";
    public static final String TP_COORDINATES = "cyber.teleport.tp.coordinates";
    public static final String TP_OTHERS = "cyber.teleport.tp.others";
    public static final String TPALL = "cyber.teleport.tpall";
    public static final String TPALL_BYPASS = "cyber.teleport.tpall.bypass";
    public static final String TPHERE = "cyber.teleport.tphere";
    public static final String SPECTATE = "cyber.teleport.spectate";

    public static final String TPA = "cyber.teleport.tpa";
    public static final String TPAHERE = "cyber.teleport.tpahere";
    public static final String TPACCEPT = "cyber.teleport.tpaccept";
    public static final String TPDENY = "cyber.teleport.tpdeny";
    public static final String TPACANCEL = "cyber.teleport.tpacancel";
    /** Permission of /tpatoggle (previously /tptoggle, node kept for compatibility). */
    public static final String TPTOGGLE = "cyber.teleport.tptoggle";
    public static final String TPAHERETOGGLE = "cyber.teleport.tpaheretoggle";
    public static final String TPAUTO = "cyber.teleport.tpauto";

    private Permissions() {
    }

    public static String time(int seconds) {
        return "cyber.teleport.time." + seconds;
    }
}