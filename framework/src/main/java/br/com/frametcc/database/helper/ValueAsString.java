package br.com.frametcc.database.helper;

import java.util.Date;

/**
 * Criado por Renan Arceno em 25/06/2015 - 17:38.
 */
public class ValueAsString {

    public static String getAsString(Object o) {
        if (o instanceof Date)
            return String.valueOf(((Date) o).getTime());
        if (o instanceof Boolean)
            return ((Boolean) o) ? "1" : "0";
        return String.valueOf(o);
    }

}
