package me.sbpro.cybermastery.util;

import me.sbpro.cybermastery.model.LevelCurve;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/** Number formatting in the CyberCoins style: 999, 1.5k, 2.25m, 3b, 4t, 5q. */
public final class AmountFormatter {

    private static final String[] SUFFIXES = {"", "k", "m", "b", "t", "q"};
    private static final ThreadLocal<DecimalFormat> FORMAT = ThreadLocal.withInitial(() ->
            new DecimalFormat("0.##", DecimalFormatSymbols.getInstance(Locale.ROOT)));

    private AmountFormatter() {
    }

    public static String format(long amount) {
        if (amount < 1000) {
            return Long.toString(amount);
        }

        double value = amount;
        int index = 0;

        while (value >= 1000.0 && index < SUFFIXES.length - 1) {
            value /= 1000.0;
            index++;
        }

        return FORMAT.get().format(value) + SUFFIXES[index];
    }

    /** Stored units (quarter-points) as points, e.g. 5 units = "1.25", 4000 units = "1k". */
    public static String formatUnits(long units) {
        if (units < 1000L * LevelCurve.UNITS_PER_POINT) {
            return FORMAT.get().format((double) units / LevelCurve.UNITS_PER_POINT);
        }
        return format(units / LevelCurve.UNITS_PER_POINT);
    }
}
