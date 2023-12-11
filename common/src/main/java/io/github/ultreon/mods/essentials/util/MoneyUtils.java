package io.github.ultreon.mods.essentials.util;

import java.text.DecimalFormat;

public class MoneyUtils {
    private static final DecimalFormat df = new DecimalFormat();
    public static final char DOLLAR = '$';
    public static final char EURO = '€';
    public static final char POUND = '£';
    public static final char YEN = '¥';
    public static final char SIGN = DOLLAR;

    static {
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
    }

    public static String format(double amount) {
        return SIGN + df.format(amount).replaceAll(",00", ",-");
    }
}
