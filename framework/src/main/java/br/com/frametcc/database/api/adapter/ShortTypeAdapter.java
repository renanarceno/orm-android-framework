package br.com.frametcc.database.api.adapter;

import android.content.ContentValues;
import android.database.Cursor;

public class ShortTypeAdapter implements ContentValueAdapter<Short> {

    @Override
    public void putContentValue(ContentValues value, String columnName, Short o) {
        value.put(columnName, o);
    }

    @Override
    public Short getValueFromCursor(Cursor cursor, int index) {
        return cursor.getShort(index);
    }
}