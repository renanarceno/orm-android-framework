package br.com.frametcc.database.api.adapter;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class TypeAdapter {

    private static TypeAdapter singleton;

    public synchronized static TypeAdapter getInstance() {
        if (singleton == null)
            singleton = new TypeAdapter();
        return singleton;
    }

    private Map<Class, ContentValueAdapter> adapters = new HashMap<>();

    private TypeAdapter() {
        this.adapters.put(byte[].class, new ByteArrayTypeAdapter());
        this.adapters.put(Date.class, new DateTypeAdapter());
        this.adapters.put(Boolean.class, new BooleanTypeAdapter());
        this.adapters.put(Byte.class, new ByteTypeAdapter());
        this.adapters.put(Double.class, new DoubleTypeAdapter());
        this.adapters.put(Float.class, new FloatTypeAdapter());
        this.adapters.put(Integer.class, new IntegerTypeAdapter());
        this.adapters.put(Long.class, new LongTypeAdapter());
        this.adapters.put(Short.class, new ShortTypeAdapter());
        this.adapters.put(String.class, new StringTypeAdapter());
        this.adapters.put(null, new NullTypeAdapter());
    }

    public void registerTypeAdapter(Class clazz, ContentValueAdapter adapter) {
        this.adapters.put(clazz, adapter);
    }

    @SuppressWarnings("all")
    public void setValue(ContentValues values, Class clazz, String columnName, Object value) {
        ContentValueAdapter adapter = this.adapters.get(clazz);
        if (adapter == null)
            throw new NullPointerException("Adapter to class '" + clazz + "' not registered in " + getClass().getSimpleName());
        if (value != null)
            adapter.putContentValue(values, columnName, value);
        else
            values.putNull(columnName);
    }

    public Object getValue(Class clazz, int columnIndex, Cursor cursor) {
        ContentValueAdapter adapter = this.adapters.get(clazz);
        if (adapter == null)
            throw new NullPointerException("Adapter to class '" + clazz + "' not registered in " + getClass().getSimpleName());
        return adapter.getValueFromCursor(cursor, columnIndex);
    }

    public ContentValueAdapter getAdapter(Class clazz) {
        return this.adapters.get(clazz);
    }

}