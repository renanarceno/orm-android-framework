package br.com.frametcc.database;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBCreationHelper {

    protected static final String DB_TAG = "DB_CREATION";

    private Context context;
    private String DATABASE_CREATE_FOLDER;
    private String DATABASE_UPDATE_FOLDER;

    public DBCreationHelper(Context context, String DATABASE_CREATE_FOLDER, String DATABASE_UPDATE_FOLDER) {
        this.context = context;
        this.DATABASE_CREATE_FOLDER = DATABASE_CREATE_FOLDER;
        this.DATABASE_UPDATE_FOLDER = DATABASE_UPDATE_FOLDER;
    }

    public void createFromAssets(SQLiteDatabase db) {
        try {
            AssetManager am = this.context.getAssets();
            String[] list = am.list(DATABASE_CREATE_FOLDER);
            executeScripts(db, list, DATABASE_CREATE_FOLDER);
        } catch (Exception e) {
            throw new RuntimeException("Error while trying to read script file");
        }
    }

    public void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            AssetManager am = this.context.getAssets();
            List<String> list = Arrays.asList(am.list(DATABASE_UPDATE_FOLDER));
            List<String> files = new ArrayList<>();
            String folder, versionFolder;
            while (oldVersion <= newVersion) {
                versionFolder = folder = checkExists(list, "_" + String.valueOf(oldVersion));
                if (folder != null) {
                    folder = DATABASE_UPDATE_FOLDER + File.separator + folder;
                    for (String string : am.list(folder))
                        files.add(versionFolder + File.separator + string);
                }
                oldVersion++;
            }
            String[] b = new String[files.size()];
            files.toArray(b);
            executeScripts(db, b, DATABASE_UPDATE_FOLDER);
        } catch (Exception e) {
            throw new RuntimeException("Error while trying to read script file");
        }
    }

    private String checkExists(List<String> list, String str) {
        for (String s : list) {
            if (s.endsWith(str))
                return s;
        }
        return null;
    }

    private void executeScripts(SQLiteDatabase db, String[] list, String baseFolder) throws IOException {
        BufferedReader br = null;
        InputStreamReader in = null;
        StringBuilder builder = new StringBuilder();
        String sql;
        for (String file : list) {
            if (file != null && !file.isEmpty()) {
                in = new InputStreamReader(this.context.getAssets().open(baseFolder + File.separator + file));
                br = new BufferedReader(in);
                for (String str = br.readLine(); str != null; str = br.readLine()) {
                    if (str.startsWith("--"))
                        continue;
                    builder.append(str);
                    if (str.endsWith(";")) {
                        sql = builder.toString();
                        db.execSQL(sql);
                        Log.d(DB_TAG, "Executing script: " + sql);
                        builder = new StringBuilder();
                    }
                }
                builder = new StringBuilder();
            }
        }
        if (br != null)
            br.close();
        if (in != null)
            in.close();
    }

}