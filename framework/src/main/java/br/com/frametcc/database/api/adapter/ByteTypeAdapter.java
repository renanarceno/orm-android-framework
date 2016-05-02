package br.com.frametcc.database.api.adapter;

import android.content.ContentValues;
import android.database.Cursor;

public class ByteTypeAdapter implements ContentValueAdapter<Byte> {

    @Override
    public void putContentValue(ContentValues value, String columnName, Byte o) {
        value.put(columnName, o);
    }

    @Override
    public Byte getValueFromCursor(Cursor cursor, int index) {
        return Byte.parseByte(cursor.getString(index));
    }
}