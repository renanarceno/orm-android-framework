package br.com.frametcc.database.helper;

import java.util.Date;

import br.com.frametcc.database.api.ToStringValue;

public class ValueAsString {

    public static String[] getAsString(Object[] o) {
        String[] objs = new String[o.length];
        for (int i = 0; i < o.length; i++)
            objs[i] = getAsString(o[i]);
        return objs;
    }

    public static String getAsString(Object o) {
        if (o instanceof ToStringValue)
            return ((ToStringValue) o).getStringValue();
        if (o instanceof Date)
            return String.valueOf(((Date) o).getTime());
        if (o instanceof Boolean)
            return ((Boolean) o) ? "1" : "0";
        return String.valueOf(o);
    }

}
