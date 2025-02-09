package dk.sommer.coins.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class NumUtils {

    private static final Locale locale = new Locale("da", "DK");
    private static final NumberFormat currencyFormat = NumberFormat.getNumberInstance(locale);

    public static String formatted(Number num){
        return currencyFormat.format(num);
    }
}
