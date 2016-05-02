package br.com.frametcc.database.api.adapter;

import android.content.ContentValues;
import android.database.Cursor;

public class BooleanTypeAdapter implements ContentValueAdapter<Boolean> {

    @Override
    public void putContentValue(ContentValues value, String columnName, Boolean o) {
        if (o != null)
            value.put(columnName, o ? 1 : 0);
        else
            value.putNull(columnName);
    }

    @Override
    public Boolean getValueFromCursor(Cursor cursor, int index) {
        return cursor.getInt(index) == 1;
    }
}