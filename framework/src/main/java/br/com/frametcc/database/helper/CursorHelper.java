package br.com.frametcc.database.helper;

import android.database.Cursor;
import android.support.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.frametcc.database.annotation.Column;
import br.com.frametcc.database.annotation.Table;

public class CursorHelper {

    @Nullable
    public static <E> E cursorToObj(Cursor cursor, Class<E> type) {
        if(!cursor.moveToFirst()) {
            return null;
        }
        return getValue(cursor, type);
    }

    public static <E> List<E> cursorToList(Cursor cursor, Class<E> type, PopulateListener<E> listener) {
        List<E> list = new ArrayList<E>();
        if(!cursor.moveToFirst()) {
            return list;
        }
        do {
            E value = getValue(cursor, type);
            if(listener != null) {
                listener.onPopulate(value);
            }
            list.add(value);
        } while(cursor.moveToNext());
        return list;
    }

    public static <E> List<E> cursorToList(Cursor cursor, Class<E> type) {
        List<E> list = new ArrayList<E>();
        if(!cursor.moveToFirst()) {
            return list;
        }
        do {
            E value = getValue(cursor, type);
            list.add(value);
        } while(cursor.moveToNext());
        return list;
    }

    private static <E> E getValue(Cursor cursor, Class<E> type) {
        E obj;
        try {
            obj = type.newInstance();
            Field[] fields = type.getFields();
            for(Field f : fields) {
                if(f.isAnnotationPresent(Column.class)) {
                    Column annotation = f.getAnnotation(Column.class);
                    String columnName = annotation.name();
                    int index = cursor.getColumnIndex(columnName);
                    if(annotation.isForeignKey()) {
                        setForeignValue(obj, f, cursor, index);
                    } else {
                        setFromRightType(cursor, obj, index, f);
                    }
                }
            }
        } catch(Exception e) {
            throw new RuntimeException("Problema ao converter dados do cursor para objeto do tipo: " + type.getSimpleName());
        }
        return obj;
    }

    private static <E> void setForeignValue(E obj, Field field, Cursor cursor, int index) throws IllegalAccessException, InstantiationException {
        if(cursor.isNull(index)) {
            field.set(obj, null);
        } else {
            if(field.getType().isAnnotationPresent(Table.class)) {
                Object o = field.getType().newInstance();
                for(Field f : o.getClass().getDeclaredFields()) {
                    Column annotation = f.getAnnotation(Column.class);
                    if(annotation != null && annotation.isPrimaryKey()) {
                        setFromRightType(cursor, o, index, f);
                        field.set(obj, o);
                    }
                }
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <E> void setFromRightType(Cursor cursor, E obj, int index, Field f) throws IllegalAccessException, IllegalArgumentException {
        if(cursor.isNull(index)) {
            f.set(obj, null);
        } else {
            Class<?> type = f.getType();
            if(type == byte[].class) {
                f.set(obj, cursor.getBlob(index));
            } else if(type == Boolean.class) {
                f.set(obj, cursor.getInt(index) != 0);
            } else if(type.getSuperclass() == Number.class) {
                if(type == Double.class) {
                    f.set(obj, cursor.getDouble(index));
                } else if(type == Float.class) {
                    f.set(obj, cursor.getFloat(index));
                } else if(type == Long.class) {
                    f.set(obj, cursor.getLong(index));
                } else if(type == Integer.class) {
                    f.set(obj, cursor.getInt(index));
                } else if(type == Short.class) {
                    f.set(obj, cursor.getShort(index));
                }
            } else if(type == String.class) {
                f.set(obj, cursor.getString(index));
            } else if(type == Date.class) {
                Long data = Long.parseLong(cursor.getString(index));
                f.set(obj, new Date(data));
            } else if(type.isEnum()) {
                f.set(obj, Enum.valueOf((Class<Enum>) type, cursor.getString(index)));
            }
        }
    }

    public interface PopulateListener<E> {

        public void onPopulate(E obj);

    }

}