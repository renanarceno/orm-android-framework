package br.com.frametcc.database.api.adapter;

import android.content.ContentValues;
import android.database.Cursor;

public class IntegerTypeAdapter implements ContentValueAdapter<Integer> {

    @Override
    public void putContentValue(ContentValues value, String columnName, Integer o) {
        value.put(columnName, o);
    }

    @Override
    public Integer getValueFromCursor(Cursor cursor, int index) {
        return cursor.getInt(index);
    }
}