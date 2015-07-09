package br.com.frametcc.database.helper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
            return field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <E extends Annotation> E getFieldAnnotation(Field field, Class<E> annotation) {
        E an = null;
        if (field.isAnnotationPresent(annotation))
            an = field.getAnnotation(annotation);
        return an;
    }

}