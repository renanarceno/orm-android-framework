package br.com.frametcc.database.api;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.frametcc.database.annotation.AnnotationHelper;
import br.com.frametcc.database.annotation.Column;
import br.com.frametcc.database.helper.ReflectionHelper;
import br.com.frametcc.database.helper.ValueAsString;

public class TableSpec<E> {

    private Class<E> entity;
    private String tableName;
    private Map<String, Field> primaryKeyMap;

    private TableSpec(Class<E> entity, String tableName, Map<String, Field> primaryKeyMap) {
        this.entity = entity;
        this.tableName = tableName;
        this.primaryKeyMap = primaryKeyMap;
    }

    @SuppressWarnings("all")
    public static <E> TableSpec<E> createInstance(Class<E> entity) {
        return extractSpecs(entity);
    }

    @SuppressWarnings("all")
    private static TableSpec extractSpecs(Class entity) {
        String tableName = AnnotationHelper.getTableName(entity);
        final List<Field> fields = ReflectionHelper.getAnnotationFields(entity, Column.class);
        Map<String, Field> pkMap = new HashMap<>();
        for (Field f : fields) {
            final Column column = f.getAnnotation(Column.class);
            if (column.isAutoIncrementPrimaryKey() || column.isPrimaryKey())
                pkMap.put(column.name(), f);
        }
        return new TableSpec(entity, tableName, pkMap);
    }

    public String getPrimaryKeyWhereQuery() {
        return resolveSelection(getPrimaryKeyColumns());
    }

    public String[] getPrimaryKeyWhereValues(E obj) {
        return getPrimaryKeyValues(obj);
    }

    public String[] getPrimaryKeyColumns() {
        return (String[]) primaryKeyMap.keySet().toArray();
    }

    public String[] getPrimaryKeyValues(E obj) {
        String[] columns = getPrimaryKeyColumns();
        String[] values = new String[columns.length];
        for (int i = 0; i < columns.length; i++) {
            final Field field = primaryKeyMap.get(columns[i]);
            final Object value = ReflectionHelper.getValue(field, obj);
            values[i] = ValueAsString.getAsString(value);
        }
        return values;
    }

    public String getTableName() {
        return tableName;
    }

    public Class<E> getEntity() {
        return entity;
    }

    public Map<String, Field> getPrimaryKeyMap() {
        return primaryKeyMap;
    }

    protected String resolveSelection(String[] keyName) {
        StringBuilder selection = new StringBuilder("");
        for (String s : keyName) {
            selection.append(s);
            selection.append(" =?, ");
        }
        String resp = selection.toString();
        int indexOf = resp.lastIndexOf(",");
        return resp.substring(0, indexOf);
    }

}