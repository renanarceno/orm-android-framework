package br.com.frametcc.database.helper;

import android.content.ContentValues;

import java.lang.reflect.Field;
import java.util.Date;

import br.com.frametcc.database.annotation.Column;
import br.com.frametcc.database.annotation.Table;

public class ContentValueHelper {

    public static ContentValues getValues(Object obj) {
        if (obj == null) {
            return null;
        }

        if (!obj.getClass().isAnnotationPresent(Table.class)) {
            throw new RuntimeException("Nenhuma anotação do tipo " + Table.class.getSimpleName() + " presente na classe " + obj.getClass().getSimpleName());
        }

        Class<Column> annotationClass = Column.class;
        ContentValues dados = new ContentValues();
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field f : fields) {
            if (f.isAnnotationPresent(annotationClass)) {
                Column coluna = f.getAnnotation(annotationClass);
                String col = coluna.name();
                try {
                    if (coluna.isForeignKey()) {
                        setForeignValue(dados, col, f.getType(), f.get(obj));
                    } else {
                        setValue(dados, col, f.getType(), f.get(obj));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return dados;
    }

    private static void setForeignValue(ContentValues dados, String col, Class<?> type, Object obj) throws IllegalAccessException, InstantiationException {
        if (type.isAnnotationPresent(Table.class)) {
            Field[] typeDeclaredFields = type.getDeclaredFields();
            if (obj == null)
                obj = type.newInstance();
            for (Field f : typeDeclaredFields) {
                Column annotation = f.getAnnotation(Column.class);
                if (annotation != null && annotation.isPrimaryKey()) {
                    setValue(dados, col, type, f.get(obj));
                    return;
                }
            }
        }
    }

    private static void setValue(ContentValues dados, String col, Class<?> type, Object value) {
        if (value == null) {
            dados.putNull(col);
        } else if (type.getSuperclass() == Number.class) {
            setNumber(dados, col, type, value);
        } else if (type == Boolean.class) {
            dados.put(col, (Boolean) value ? 1 : 0);
        } else if (type.isEnum()) {
            dados.put(col, ((Enum<?>) value).name());
        } else if (type == Date.class) {
            dados.put(col, ((Date) value).getTime());
        } else if (type == byte[].class) {
            dados.put(col, (byte[]) value);
        } else {
            dados.put(col, value.toString().trim());
        }
    }

    private static void setNumber(ContentValues dados, String col, Class<?> type, Object value) {
        if (type == Integer.class) {
            dados.put(col, (Integer) value);
        } else if (type == Long.class) {
            dados.put(col, (Long) value);
        } else if (type == Double.class) {
            dados.put(col, (Double) value);
        } else if (type == Float.class) {
            dados.put(col, (Float) value);
        } else if (type == Short.class) {
            dados.put(col, (Short) value);
        } else if (type == Byte.class) {
            dados.put(col, (Byte) value);
        }
    }
}
