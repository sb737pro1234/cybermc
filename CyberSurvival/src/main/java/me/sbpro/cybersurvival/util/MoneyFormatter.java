package me.sbpro.cybersurvival.util;

import java.text.DecimalFormat;

public final class MoneyFormatter {

    private static final String MONEY_FORMAT = "#,##0";

    private MoneyFormatter() {
    }

    public static String format(double amount) {
        return new DecimalFormat(MONEY_FORMAT).format(amount);
    }
}
