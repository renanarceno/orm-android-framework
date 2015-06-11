package br.com.frametcc.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import br.com.frametcc.database.annotation.AnnotationHelper;
import br.com.frametcc.database.api.AutoIncrementId;
import br.com.frametcc.database.dao.DatabaseDAO;
import br.com.frametcc.database.helper.ContentValueHelper;
import br.com.frametcc.database.helper.CursorHelper;

public abstract class DBHelper<E> extends SQLiteOpenHelper implements DatabaseDAO<E> {
    protected static final String DB_TAG = "DATABASE";
    private static final int DATABASE_VERSION = 1;
    private static final String NOME_DATABASE = "bitboard2.db";

    private static final String DATABASE_DDL_INICIAL_PATH = "database/inicial/ddl";
    private static final String DATABASE_DDL_UPDATE_PATH = "database/update/ddl";

    private Context context;

    public DBHelper(Context context) {
        super(context, NOME_DATABASE, null, DATABASE_VERSION);
        this.context = context;
    }

    public void initDatabase() {
        this.getWritableDatabase();
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(DB_TAG, "Criando database");
        this.createOrUpdateFromAssets(db, DATABASE_DDL_INICIAL_PATH);
        Log.d(DB_TAG, "Banco de dados criado.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.createOrUpdateFromAssets(db, DATABASE_DDL_UPDATE_PATH);
        Log.d(DB_TAG, "Banco de dados atualizado.");
    }

    @Override
    public synchronized void insertOrUpdate(E obj) {
        String tableName = AnnotationHelper.getTableName(obj);
        String[] keyName = AnnotationHelper.getPrimaryKeyName(obj);
        String[] values = AnnotationHelper.getPrimaryKeyValue(obj);
        ContentValues dados = ContentValueHelper.getValues(obj);
        SQLiteDatabase database = this.getWritableDatabase();
        boolean isToUpdate = false;
        if(values != null) {
            for(String str : values) {
                if(str != null) {
                    isToUpdate = true;
                    break;
                }
            }
        }
        if(isToUpdate) {
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
        } catch(Exception ignore) {
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
            if(obj instanceof AutoIncrementId) {
                ((AutoIncrementId) obj).setId(id);
            }
        } catch(Exception ignore) {
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    @Override
    public void deleteByPrimaryKey(E obj) {
        String[] keyName = AnnotationHelper.getPrimaryKeyName(obj);
        String selection = this.resolveSelection(keyName);
        String tableName = AnnotationHelper.getTableName(obj);

        String[] value = AnnotationHelper.getPrimaryKeyValue(obj);
        SQLiteDatabase database = this.getWritableDatabase();
        database.beginTransaction();
        try {
            database.delete(tableName, selection, value);
            database.setTransactionSuccessful();
        } catch(Exception ignore) {
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public E getByPrimaryKey(E obj) {
        String[] keyName = AnnotationHelper.getPrimaryKeyName(obj);
        String tableName = AnnotationHelper.getTableName(obj);

        String[] value = AnnotationHelper.getPrimaryKeyValue(obj);
        SQLiteDatabase database = this.getReadableDatabase();

        String selection = this.resolveSelection(keyName);
        Cursor cursor = database.query(tableName, null, selection, value, null, null, null);
        E object = (E) CursorHelper.cursorToObj(cursor, obj.getClass());

        if(cursor != null) {
            cursor.close();
        }

        return object;
    }

    protected String resolveSelection(String[] keyName) {
        StringBuilder selection = new StringBuilder("");
        for(String s : keyName) {
            selection.append(s);
            selection.append(" =?, ");
        }
        String resp = selection.toString();
        int indexOf = resp.lastIndexOf(",");
        return resp.substring(0, indexOf);
    }

    private void createOrUpdateFromAssets(SQLiteDatabase db, String path) {
        BufferedReader br = null;
        InputStreamReader in = null;
        StringBuilder builder = new StringBuilder();
        try {
            AssetManager am = this.context.getAssets();
            String[] list = am.list(path);
            for(String file : list) {
                in = new InputStreamReader(am.open(path + "/" + file));
                br = new BufferedReader(in);
                String str = br.readLine();
                while(str != null) {
                    builder.append(str);
                    str = br.readLine();
                }
                Log.d(DB_TAG, "Executando script: " + file);
                db.execSQL(builder.toString());
                builder = new StringBuilder();
            }
        } catch(Exception e) {
            throw new RuntimeException("Erro ao ler arquivos para criar bd");
        } finally {
            try {
                if(br != null) {
                    br.close();
                }
                if(in != null) {
                    in.close();
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}