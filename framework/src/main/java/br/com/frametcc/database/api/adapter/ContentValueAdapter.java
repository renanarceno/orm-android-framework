package br.com.frametcc.database.api.adapter;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.HashMap;
import java.util.Map;

public interface ContentValueAdapter<TYPE> {

    void putContentValue(ContentValues value, String columnName, TYPE o);

    TYPE getValueFromCursor(Cursor cursor, int index);

}
