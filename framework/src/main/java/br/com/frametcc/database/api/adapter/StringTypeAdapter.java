package br.com.frametcc.database.api.adapter;

import android.content.ContentValues;
import android.database.Cursor;

public class StringTypeAdapter implements ContentValueAdapter<String> {

    @Override
    public void putContentValue(ContentValues value, String columnName, String o) {
        value.put(columnName, o);
    }

    @Override
    public String getValueFromCursor(Cursor cursor, int index) {
        return cursor.getString(index);
    }
}