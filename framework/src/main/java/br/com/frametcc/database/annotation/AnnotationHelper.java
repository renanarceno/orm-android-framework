package br.com.frametcc.database.annotation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AnnotationHelper {

    public static String getTableName(Object obj) {
        return getTableName(obj.getClass());
    }

    public static String getTableName(Class<?> clazz) {
        Table tableName = clazz.getAnnotation(Table.class);
        if (tableName == null || "".equals(tableName.value())) {
            throw new RuntimeException("Annotation " + Table.class.getSimpleName() + " não existe na classe: " + clazz.getSimpleName() + ".");
        }
        return tableName.value();
    }

    public static String[] getPrimaryKeyValue(Object obj) {
        List<Field> fields = getPrimaryKeyFields(obj);
        List<String> strs = new ArrayList<String>();
        for (Field f : fields) {
            try {
                Object object = f.get(obj);
                strs.add(object == null ? null : object.toString());
            } catch (Exception e) {
                throw new RuntimeException("Atributo anotado com " + Column.class.getSimpleName() + " precisa ser público.\n" + e.getMessage());
            }
        }
        String[] gambi = new String[strs.size()];
        gambi = strs.toArray(gambi);
        return gambi;
    }

    public static String[] getPrimaryKeyName(Object obj) {
        List<Field> fields = getPrimaryKeyFields(obj);
        List<String> pk = new ArrayList<>();
        for (Field f : fields) {
            Column annotation = f.getAnnotation(Column.class);
            boolean primaryKey = annotation.isPrimaryKey() || annotation.isAutoIncrementPrimaryKey();
            String columnName = annotation.name();
            if (primaryKey) {
                pk.add(columnName);
            }
        }
        String[] gambi = new String[pk.size()];
        gambi = pk.toArray(gambi);
        return gambi;
    }

    public static List<Field> getPrimaryKeyFields(Object obj) {
        Field[] fields = obj.getClass().getFields();
        List<Field> pkField = new ArrayList<>();
        for (Field f : fields) {
            Column annotation;
            if (f.isAnnotationPresent(Column.class)) {
                annotation = f.getAnnotation(Column.class);
                if (annotation.isPrimaryKey() || annotation.isAutoIncrementPrimaryKey()) {
                    pkField.add(f);
                }
            }
        }
        if (pkField.size() == 0) {
            throw new RuntimeException("Nenhum atributo na classe " + obj.getClass().getSimpleName() + " anotado com " + Column.class.getSimpleName() + " tem o atributo isPrimaryKey");
        }
        return pkField;
    }

    public static void setPrimaryKey(Object obj, long id) {
        final List<Field> fields = getPrimaryKeyFields(obj);
        for (Field f : fields) {
            if (f.getAnnotation(Column.class).isAutoIncrementPrimaryKey()) {
                try {
                    f.set(obj, id);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
