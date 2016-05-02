package br.com.frametcc.database.api.adapter;

import android.content.ContentValues;
import android.database.Cursor;

public class DoubleTypeAdapter implements ContentValueAdapter<Double> {

    @Override
    public void putContentValue(ContentValues value, String columnName, Double o) {
        value.put(columnName, o);
    }

    @Override
    public Double getValueFromCursor(Cursor cursor, int index) {
        return cursor.getDouble(index);
    }
}