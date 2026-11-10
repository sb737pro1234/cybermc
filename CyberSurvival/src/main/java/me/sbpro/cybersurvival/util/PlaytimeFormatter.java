package me.sbpro.cybersurvival.util;

public final class PlaytimeFormatter {

    private PlaytimeFormatter() {
    }

    /**
     * Converts a playtime in ticks (20 ticks = 1 second) into a readable string such as
     * "3d 4h 12m". Zero-value larger units are skipped and minutes are always shown.
     */
    public static String format(long ticks) {
        long totalMinutes = Math.max(0, ticks) / 20L / 60L;
        long days = totalMinutes / (60L * 24L);
        long hours = (totalMinutes / 60L) % 24L;
        long minutes = totalMinutes % 60L;

        StringBuilder builder = new StringBuilder();

        if (days > 0) {
            builder.append(days).append('d').append(' ');
        }

        if (days > 0 || hours > 0) {
            builder.append(hours).append('h').append(' ');
        }

        builder.append(minutes).append('m');

        return builder.toString();
    }

    /**
     * Compact form for placeholders: "5m", "1h 10m" or "2d 5h".
     * Once days are shown, minutes are dropped (rounded down); zero hours are hidden.
     */
    public static String formatShort(long ticks) {
        long totalMinutes = Math.max(0, ticks) / 20L / 60L;
        long days = totalMinutes / (60L * 24L);
        long hours = (totalMinutes / 60L) % 24L;
        long minutes = totalMinutes % 60L;

        if (days > 0) {
            return hours > 0 ? days + "d " + hours + "h" : days + "d";
        }

        if (hours > 0) {
            return hours + "h " + minutes + "m";
        }

        return minutes + "m";
    }
}
