package br.com.frametcc.database.helper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import br.com.frametcc.database.annotation.Column;
import br.com.frametcc.database.annotation.Table;

public class ReflectionHelper {

    public static List<Field> getAnnotationFields(Class clazz, Class<? extends Annotation> annotation) {
        List<Field> list = new ArrayList<>();
        final Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields)
            if (f.isAnnotationPresent(annotation))
                list.add(f);
        return list;
    }

    public static Object getValue(Field field, Object object) {
        try {
            field.setAccessible(true);
            Object foreignObj = field.get(object);
            final String ref = field.getAnnotation(Column.class).foreignKeyRef();
            if (foreignObj != null && !ref.equals("")) {
                final Class<?> foreignClass = foreignObj.getClass();
                if (foreignClass.isAnnotationPresent(Table.class))
                    for (Field foreignField : foreignClass.getDeclaredFields()) {
                        final Column annotation = foreignField.getAnnotation(Column.class);
                        if (annotation != null && ((annotation.isPrimaryKey() || annotation.isAutoIncrementPrimaryKey()) && annotation.name().equals(ref)))
                            return foreignField.get(foreignObj);
                    }
            } else {
                return foreignObj;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setFieldValue(Field f, Object obj, Object value) {
        try {
            f.setAccessible(true);
            f.set(obj, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Object getNewInstance(Class clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}