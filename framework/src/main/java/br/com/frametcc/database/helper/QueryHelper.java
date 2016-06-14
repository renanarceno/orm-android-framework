package br.com.frametcc.database.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import br.com.frametcc.TCCApplication;
import br.com.frametcc.database.api.DBListener;
import br.com.frametcc.database.api.TableSpec;

public class QueryHelper<E> {

    private final TableSpec<E> table;
    private DBListener<E> listener;

    public QueryHelper(Class<E> entityType) {
        this.table = TCCApplication.getTableSpec(entityType);
    }

    public void setListener(DBListener<E> listener) {
        this.listener = listener;
    }

    public void insertOrUpdate(SQLiteDatabase db, E obj) {
        boolean update = existInDatabase(db, this.table.getPrimaryKeyColumns(), table.getPrimaryKeyValues(obj));
        if (update)
            this.update(db, obj);
        else
            this.insert(db, obj);
    }

    public void updateWhere(SQLiteDatabase db, E obj, String column) {
        final Object valueFromColumn = table.getValueFromColumn(obj, column);
        db.update(this.table.getTableName(), this.table.getInsertContentValues(obj), resolveSelection(column), new String[]{ValueAsString.getAsString(valueFromColumn)});
    }

    public boolean existInDatabase(SQLiteDatabase db, String column[], Object... value) {
        final Cursor query = db.query(table.getTableName(), column, resolveSelection(column), ValueAsString.getAsString(value), null, null, null);
        boolean exists = query.getCount() > 0;
        query.close();
        return exists;
    }

    public void insert(SQLiteDatabase db, E obj) {
        final ContentValues values = table.getInsertContentValues(obj);
        final Long insert = db.insert(table.getTableName(), null, values);
        table.setAutoIncrementPrimaryKey(obj, insert);
    }

    public void update(SQLiteDatabase db, E obj) {
        final ContentValues values = table.getInsertContentValues(obj);
        db.update(table.getTableName(), values, table.getPrimaryKeyWhereQuery(), table.getPrimaryKeyWhereValues(obj));
    }

    public List<E> listAll(SQLiteDatabase db) {
        return this.listAll(db, false);
    }

    public int countAll(SQLiteDatabase db) {
        Cursor query = db.query(table.getTableName(), null, null, null, null, null, null, null);
        int count = query.getCount();
        query.close();
        return count;
    }

    public List<E> listAll(SQLiteDatabase db, boolean distinct) {
        return this.listAll(db, distinct, null);
    }

    public List<E> listAll(SQLiteDatabase db, boolean distinct, String orderBy) {
        return this.listAll(db, distinct, orderBy, null);
    }

    public List<E> listAll(SQLiteDatabase db, boolean distinct, String orderBy, String limit) {
        final Cursor query = db.query(distinct, table.getTableName(), null, null, null, null, null, orderBy, limit);
        final List<E> list = table.cursorToObjectList(query, this.listener);
        query.close();
        return list;
    }

    public void delete(SQLiteDatabase db, E obj) {
        db.delete(table.getTableName(), table.getPrimaryKeyWhereQuery(), table.getPrimaryKeyWhereValues(obj));
    }

    public Long getMaxValue(SQLiteDatabase db, String column) {
        final Cursor query = db.query(this.table.getTableName(), new String[]{"MAX(" + column + ")"}, null, null, null, null, null);
        if (query.moveToFirst()) {
            long serverId = query.getLong(0);
            query.close();
            return serverId;
        }
        query.close();
        return null;
    }

    public Cursor rawQueryCursor(SQLiteDatabase db, String sql) {
        return db.rawQuery(sql, null);
    }

    public E rawQueryUnique(SQLiteDatabase db, String sql) {
        Cursor cursor = db.rawQuery(sql, null);
        E obj = this.table.cursorToObject(cursor, this.listener);
        cursor.close();
        return obj;
    }

    public List<E> rawQueryList(SQLiteDatabase db, String sql) {
        Cursor cursor = db.rawQuery(sql, null);
        List<E> list = this.table.cursorToObjectList(cursor, this.listener);
        cursor.close();
        return list;
    }

    public E getWhere(SQLiteDatabase db, String column, Object... value) {
        final Cursor cursor = queryWhere(db, new String[]{column}, null, null, null, "1", value);
        E obj = this.table.cursorToObject(cursor, this.listener);
        cursor.close();
        return obj;
    }

    public E getWhere(SQLiteDatabase db, String[] column, Object... value) {
        final Cursor cursor = queryWhere(db, column, null, null, null, "1", value);
        E obj = this.table.cursorToObject(cursor, this.listener);
        cursor.close();
        return obj;
    }

    public E queryUnique(SQLiteDatabase db, String query, Object... value) {
        final Cursor cursor = queryWhere(db, query, null, null, null, "1", value);
        E obj = table.cursorToObject(cursor, this.listener);
        cursor.close();
        return obj;
    }

    public List<E> queryList(SQLiteDatabase db, String query, Object... value) {
        final Cursor cursor = queryWhere(db, query, null, null, null, null, value);
        final List<E> obj = table.cursorToObjectList(cursor, this.listener);
        cursor.close();
        return obj;
    }

    public List<E> getWhereList(SQLiteDatabase db, String where, Object... value) {
        final Cursor cursor = queryWhere(db, new String[]{where}, null, null, null, null, value);
        final List<E> obj = table.cursorToObjectList(cursor, this.listener);
        cursor.close();
        return obj;
    }

    public List<E> getWhereList(SQLiteDatabase db, String where, String groupBy, String having, String orderBy, String limit, Object... value) {
        final Cursor cursor = queryWhere(db, new String[]{where}, groupBy, having, orderBy, limit, value);
        final List<E> obj = table.cursorToObjectList(cursor, this.listener);
        cursor.close();
        return obj;
    }

    public List<E> getWhereList(SQLiteDatabase db, String[] where, String groupBy, String having, String orderBy, String limit, Object... value) {
        final Cursor cursor = queryWhere(db, where, groupBy, having, orderBy, limit, value);
        final List<E> obj = table.cursorToObjectList(cursor, this.listener);
        cursor.close();
        return obj;
    }

    public List<E> rawQueryList(SQLiteDatabase db, String sql, String[] args) {
        Cursor cursor = db.rawQuery(sql, args);
        List<E> es = this.table.cursorToObjectList(cursor);
        cursor.close();
        return es;
    }

    private Cursor queryWhere(SQLiteDatabase db, String where, String groupBy, String having, String orderBy, String limit, Object... value) {
        return db.query(table.getTableName(), null, where, ValueAsString.getAsString(value), groupBy, having, orderBy, limit);
    }

    private Cursor queryWhere(SQLiteDatabase db, String[] where, String groupBy, String having, String orderBy, String limit, Object... value) {
        return db.query(table.getTableName(), null, resolveSelection(where), ValueAsString.getAsString(value), groupBy, having, orderBy, limit);
    }

    protected String resolveSelection(String... keyName) {
        StringBuilder selection = new StringBuilder("");
        for (String s : keyName)
            selection.append(s).append(" =?, ");

        String resp = selection.toString();
        int indexOf = resp.lastIndexOf(",");
        return resp.substring(0, indexOf);
    }
}