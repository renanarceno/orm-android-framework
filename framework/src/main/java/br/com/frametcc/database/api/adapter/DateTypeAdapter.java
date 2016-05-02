package br.com.frametcc.database.api.adapter;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

public class DateTypeAdapter implements ContentValueAdapter<Date> {

    @Override
    public void putContentValue(ContentValues value, String columnName, Date o) {
        value.put(columnName, o.getTime());
    }

    @Override
    public Date getValueFromCursor(Cursor cursor, int index) {
        String date = cursor.getString(index);
        if (date == null)
            return null;
        return new Date(Long.parseLong(date));
    }
}
