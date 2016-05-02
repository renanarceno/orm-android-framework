package br.com.frametcc.database.api.adapter;

import android.content.ContentValues;
import android.database.Cursor;

public class FloatTypeAdapter implements ContentValueAdapter<Float> {

    @Override
    public void putContentValue(ContentValues value, String columnName, Float o) {
        value.put(columnName, o);
    }

    @Override
    public Float getValueFromCursor(Cursor cursor, int index) {
        return cursor.getFloat(index);
    }
}