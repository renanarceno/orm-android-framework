package br.com.frametcc.database.api.adapter;

import android.content.ContentValues;
import android.database.Cursor;

public class NullTypeAdapter implements ContentValueAdapter<Object> {

    @Override
    public void putContentValue(ContentValues value, String columnName, Object o) {
        value.putNull(columnName);
    }

    @Override
    public Object getValueFromCursor(Cursor cursor, int index) {
        return null;
    }
}