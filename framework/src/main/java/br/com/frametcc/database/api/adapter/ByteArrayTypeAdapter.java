package br.com.frametcc.database.api.adapter;

import android.content.ContentValues;
import android.database.Cursor;

public class ByteArrayTypeAdapter implements ContentValueAdapter<byte[]> {

    @Override
    public void putContentValue(ContentValues values, String columnName, byte[] o) {
        values.put(columnName, o);
    }

    @Override
    public byte[] getValueFromCursor(Cursor cursor, int index) {
        return cursor.getBlob(index);
    }
}