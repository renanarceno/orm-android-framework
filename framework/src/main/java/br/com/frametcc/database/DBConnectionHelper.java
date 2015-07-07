package br.com.frametcc.database;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DBConnectionHelper extends SQLiteOpenHelper {

    protected static final String DB_TAG = "DATABASE";

    private static String DATABASE_CREATE_FOLDER;
    private static String DATABASE_UPDATE_FOLDER;

    private Context context;
    private int initialVersion = 0;

    public DBConnectionHelper(Context context, String dbName, SQLiteDatabase.CursorFactory cursorFactory, int dbVersion, String dbCreateFolder, String dbUpdateFolder) {
        super(context, dbName, cursorFactory, dbVersion);
        this.context = context;
        this.initialVersion = dbVersion;
        DATABASE_CREATE_FOLDER = dbCreateFolder;
        DATABASE_UPDATE_FOLDER = dbUpdateFolder;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(DB_TAG, "Criando database");
        this.createOrUpdateFromAssets(db, DATABASE_CREATE_FOLDER);
        if (this.initialVersion > 1) {
            for (int i = 1; i <= initialVersion; i++) {
                this.updateDatabase(db, i, initialVersion);
            }
        }
        Log.d(DB_TAG, "Banco de dados criado.");
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("DBConnectionHelper.onDowngrade");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //this.createOrUpdateFromAssets(db, DATABASE_UPDATE_FOLDER);
        this.updateDatabase(db, oldVersion, newVersion);
        Log.d(DB_TAG, "Banco de dados atualizado.");
    }

    private void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        BufferedReader br = null;
        InputStreamReader in = null;
        StringBuilder builder = new StringBuilder();
        int version = oldVersion;
        try {
            AssetManager am = this.context.getAssets();
            String[] list = am.list(DATABASE_UPDATE_FOLDER);
            List<String> files = new ArrayList<>();
            for (String folder : list) {
                if (version <= newVersion && folder.endsWith(String.valueOf(version)))
                    for (String file : am.list(DATABASE_UPDATE_FOLDER + File.separator + folder))
                        files.add(DATABASE_UPDATE_FOLDER + File.separator + folder + File.separator + file);
                version++;
            }

            for (String file : files) {
                final InputStream open = am.open(file);
                in = new InputStreamReader(open);
                br = new BufferedReader(in);
                for (String str = br.readLine(); str != null; str = br.readLine()) {
                    if (str.startsWith("--"))
                        continue;
                    builder.append(str);
                    if (str.endsWith(";")) {
                        final String sql = builder.toString();
                        db.execSQL(sql);
                        Log.d(DB_TAG, "Executando script: " + sql);
                        builder = new StringBuilder();
                    }
                }
                builder = new StringBuilder();
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler arquivos para criar bd");
        } finally {
            try {
                if (br != null) br.close();
                if (in != null) in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createOrUpdateFromAssets(SQLiteDatabase db, String path) {
        BufferedReader br = null;
        InputStreamReader in = null;
        StringBuilder builder = new StringBuilder();
        try {
            AssetManager am = this.context.getAssets();
            String[] list = am.list(path);
            for (String file : list) {
                in = new InputStreamReader(am.open(path + "/" + file));
                br = new BufferedReader(in);
                for (String str = br.readLine(); str != null; str = br.readLine()) {
                    if (str.startsWith("--"))
                        continue;
                    builder.append(str);
                    if (str.endsWith(";")) {
                        final String sql = builder.toString();
                        db.execSQL(sql);
                        Log.d(DB_TAG, "Executando script: " + sql);
                        builder = new StringBuilder();
                    }
                }
                builder = new StringBuilder();
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler arquivos para criar bd");
        } finally {
            try {
                if (br != null) br.close();
                if (in != null) in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}