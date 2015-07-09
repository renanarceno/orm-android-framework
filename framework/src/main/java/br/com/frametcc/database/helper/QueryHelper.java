package br.com.frametcc.database.helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import br.com.frametcc.TCCApplication;
import br.com.frametcc.database.api.TableSpec;

public class QueryHelper<E> {

    private TableSpec<E> table;

    public QueryHelper(Class<E> entityType) {
        this.table = TCCApplication.getTableSpec(entityType);
    }

    public List<E> listAll(SQLiteDatabase db) {
        return this.listAll(db, table.getTableName(), false);
    }

    public List<E> listAll(SQLiteDatabase db, String tableName, boolean distinct) {
        return this.listAll(db, tableName, distinct, null);
    }

    public List<E> listAll(SQLiteDatabase db, String tableName, boolean distinct, String orderBy) {
        return this.listAll(db, tableName, distinct, orderBy, null);
    }

    public List<E> listAll(SQLiteDatabase db, String tableName, boolean distinct, String orderBy, String limit) {
        final Cursor query = db.query(distinct, tableName, null, null, null, null, null, orderBy, limit);
        final List<E> list = CursorHelper.cursorToList(query, table.getEntity());
        query.close();
        return list;
    }

    public void delete(SQLiteDatabase db, E obj) {
        db.delete(table.getTableName(), table.getPrimaryKeyWhereQuery(), table.getPrimaryKeyWhereValues(obj));
    }

    public E getWhere(SQLiteDatabase db, String column, Object value) {
        final Cursor cursor = queryWhere(db, column, value);
        E obj = CursorHelper.cursorToObj(cursor, table.getEntity());
        cursor.close();
        return obj;
    }

    public List<E> getWhereList(SQLiteDatabase db, String column, Object value) {
        final Cursor cursor = queryWhere(db, column, value);
        final List<E> obj = CursorHelper.cursorToList(cursor, table.getEntity());
        cursor.close();
        return obj;
    }

    private Cursor queryWhere(SQLiteDatabase db, String column, Object value) {
        return db.query(table.getTableName(), null, column + " = ? ", new String[]{ValueAsString.getAsString(value)}, null, null, null);
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