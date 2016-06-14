package br.com.frametcc.database.api;

import android.content.ContentValues;
import android.database.Cursor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.frametcc.database.annotation.AnnotationHelper;
import br.com.frametcc.database.annotation.Column;
import br.com.frametcc.database.annotation.OneToMany;
import br.com.frametcc.database.api.adapter.IntegerTypeAdapter;
import br.com.frametcc.database.helper.ReflectionHelper;
import br.com.frametcc.database.helper.ValueAsString;

public class TableSpec<E> {

	private final Class<E> entity;
	private final String tableName;
	private final Map<String, Field> primaryKeyMap;
	private final Map<String, Field> columnsMap;
	private final Map<String, Field> foreignKeyMap;
	private final TableObjectParser<E> objectParser;

	private TableSpec(Class<E> entity, String tableName, Map<String, Field> primaryKeyMap, Map<String, Field> columnsMap, Map<String, Field> foreignKeyMap) {
		this.objectParser = new TableObjectParser<E>();
		this.entity = entity;
		this.tableName = tableName;
		this.primaryKeyMap = primaryKeyMap;
		this.columnsMap = columnsMap;
		this.foreignKeyMap = foreignKeyMap;
	}

	@SuppressWarnings("all")
	public static <E> TableSpec<E> createInstance(Class<E> entity) {
		return extractSpecs(entity);
	}

	@SuppressWarnings("all")
	private static TableSpec extractSpecs(Class entity) {
		String tableName = AnnotationHelper.getTableName(entity);
		List<Field> fields = ReflectionHelper.getAnnotationFields(entity, Column.class);
		Map<String, Field> pkMap = new HashMap<>();
		Map<String, Field> colMap = new HashMap<>();
		Map<String, Field> foreignKeyMap = new HashMap<>();
		Column column;

		for (Field f : fields) {
			column = f.getAnnotation(Column.class);
			if (column.isAutoIncrementPrimaryKey() || column.isPrimaryKey())
				pkMap.put(column.name(), f);
			else if (!column.foreignKeyRef().equals(""))
				foreignKeyMap.put(column.name(), f);
			colMap.put(column.name(), f);
		}
		return new TableSpec(entity, tableName, pkMap, colMap, foreignKeyMap);
	}

	public Map<String, Field> getForeignKeyMap() {
		return foreignKeyMap;
	}

	public Map<String, Field> getPrimaryKeyMap() {
		return primaryKeyMap;
	}

	public String getPrimaryKeyWhereQuery() {
		return resolveSelection(getPrimaryKeyColumns());
	}

	public String[] getPrimaryKeyWhereValues(E obj) {
		return getPrimaryKeyValues(obj);
	}

	public String[] getPrimaryKeyColumns() {
		final Object[] objects = primaryKeyMap.keySet().toArray();
		String[] strings = new String[objects.length];
		for (int i = 0; i < objects.length; i++) {
			strings[i] = (String) objects[i];
		}
		return strings;
	}

	public String[] getPrimaryKeyValues(E obj) {
		String[] columns = getPrimaryKeyColumns();
		String[] values = new String[columns.length];
		for (int i = 0; i < columns.length; i++) {
			final Field field = primaryKeyMap.get(columns[i]);
			final Object value = ReflectionHelper.getValue(field, obj);
			if (value != null)
				values[i] = ValueAsString.getAsString(value);
		}
		return values;
	}

	public void setAutoIncrementPrimaryKey(E obj, Long id) {
		for (Field f : primaryKeyMap.values()) {
			ReflectionHelper.setFieldValue(f, obj, id);
		}
	}

	public String getTableName() {
		return tableName;
	}

	public Class<E> getEntity() {
		return entity;
	}

	public ContentValues getInsertContentValues(E obj) {
		return this.objectParser.getInsertContentValues(obj, this.columnsMap);
	}

	public E cursorToObject(Cursor cursor) {
		return this.cursorToObject(cursor, null);
	}

	public E cursorToObject(Cursor cursor, DBListener<E> listener) {
		if (!cursor.moveToFirst())
			return null;
		return this.objectParser.getObjectFromCursor(cursor, listener, getEntity(), this.columnsMap);
	}

	public List<E> cursorToObjectList(Cursor cursor, DBListener<E> listener) {
		if (!cursor.moveToFirst())
			return null;
		return this.objectParser.getObjectListFromCursor(cursor, listener, getEntity(), this.columnsMap);
	}

	public List<E> cursorToObjectList(Cursor cursor) {
		return this.cursorToObjectList(cursor, null);
	}

	public Object getValueFromColumn(E obj, String column) {
		return ReflectionHelper.getValue(this.columnsMap.get(column), obj);
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