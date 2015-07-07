package br.com.frametcc.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.frametcc.TCCApplication;
import br.com.frametcc.database.annotation.AnnotationHelper;
import br.com.frametcc.database.dao.DatabaseDAO;
import br.com.frametcc.database.helper.ContentValueHelper;
import br.com.frametcc.database.helper.CursorHelper;
import br.com.frametcc.database.helper.ValueAsString;

/**
 * Criado por Renan Arceno em 25/06/2015 - 15:25.
 */
public abstract class DAOHelper<E> implements DatabaseDAO<E> {

    public abstract Class<E> getEntityType();

    protected SQLiteDatabase getReadableDatabase() {
        return TCCApplication.getDbConnection().getReadableDatabase();
    }

    protected SQLiteDatabase getWritableDatabase() {
        return TCCApplication.getDbConnection().getWritableDatabase();
    }

    public synchronized void insertOrUpdate(E obj) {
        String tableName = AnnotationHelper.getTableName(obj);
        String[] keyName = AnnotationHelper.getPrimaryKeyName(obj);
        String[] values = AnnotationHelper.getPrimaryKeyValue(obj);
        ContentValues dados = ContentValueHelper.getValues(obj);
        SQLiteDatabase database = getWritableDatabase();
        boolean isToUpdate = false;
        if (values != null) {
            for (String str : values) {
                if (str != null) {
                    isToUpdate = true;
                    break;
                }
            }
        }
        if (isToUpdate) {
            this.update(dados, tableName, this.resolveSelection(keyName), values, database);
        } else {
            this.insert(obj, tableName, dados, database);
        }
    }

    protected void update(ContentValues dados, String tableName, String where, String[] args, SQLiteDatabase database) {
        database.beginTransaction();
        try {
            database.update(tableName, dados, where, args);
            database.setTransactionSuccessful();
        } catch (Exception ignore) {
            Log.e("DB", "DEU ERRO " + ignore.getCause());
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    protected void insert(E obj, String tableName, ContentValues dados, SQLiteDatabase database) {
        database.beginTransaction();
        long id;
        try {
            id = database.insert(tableName, null, dados);
            database.setTransactionSuccessful();
            AnnotationHelper.setPrimaryKey(obj, id);
        } catch (Exception ignore) {
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    @Override
    public void delete(E obj) {
        String[] keyName = AnnotationHelper.getPrimaryKeyName(obj);
        String selection = this.resolveSelection(keyName);
        String tableName = AnnotationHelper.getTableName(obj);

        String[] value = AnnotationHelper.getPrimaryKeyValue(obj);
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            database.delete(tableName, selection, value);
            database.setTransactionSuccessful();
        } catch (Exception ignore) {
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    @Override
    public List<E> listAll() {
        final SQLiteDatabase database = getReadableDatabase();
        List<E> list = new ArrayList<>();
        String tableName = AnnotationHelper.getTableName(getEntityType());
        database.beginTransaction();
        try {
            Cursor cursor = database.query(tableName, null, null, null, null, null, null);
            list = CursorHelper.cursorToList(cursor, getEntityType());
        } catch (Exception ignore) {
        } finally {
            database.endTransaction();
            database.close();
        }
        return list;
    }

    @Override
    public E getWhere(String columnName, Object value) {
        String tableName = AnnotationHelper.getTableName(getEntityType());
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(tableName, null, columnName + " = ? ", new String[]{ValueAsString.getAsString(value)}, null, null, null);
        E object = CursorHelper.cursorToObj(cursor, getEntityType());
        if (cursor != null)
            cursor.close();
        return object;
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