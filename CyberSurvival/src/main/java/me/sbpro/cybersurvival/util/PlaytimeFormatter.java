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
}
