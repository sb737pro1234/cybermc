package me.sbpro.cybermastery.util;

/** Maths for progress bars. How they look is in Messages#progressBar. */
public final class ProgressBar {

    private ProgressBar() {
    }

    /** Number of filled segments for a fraction (0.0 to 1.0). */
    public static int filled(double fraction, int length) {
        double clamped = Math.max(0.0, Math.min(1.0, fraction));
        int filled = (int) Math.floor(clamped * length);
        // Show at least one segment once there is any progress, and only a full bar at 100%.
        if (clamped > 0 && filled == 0) {
            return 1;
        }
        if (clamped < 1.0 && filled == length) {
            return length - 1;
        }
        return filled;
    }

    /** Whole percent, never showing 100% before it is complete. */
    public static int percent(double fraction) {
        double clamped = Math.max(0.0, Math.min(1.0, fraction));
        int percent = (int) Math.floor(clamped * 100);
        return clamped < 1.0 ? Math.min(percent, 99) : 100;
    }

    public static double fraction(long value, long max) {
        return max <= 0 ? 0.0 : (double) value / max;
    }
}
