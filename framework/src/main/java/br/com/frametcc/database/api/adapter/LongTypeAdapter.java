package br.com.frametcc.database.api.adapter;

import android.content.ContentValues;
import android.database.Cursor;

public class LongTypeAdapter implements ContentValueAdapter<Long> {

    @Override
    public void putContentValue(ContentValues value, String columnName, Long o) {
        value.put(columnName, o);
    }

    @Override
    public Long getValueFromCursor(Cursor cursor, int index) {
        return cursor.getLong(index);
    }
}