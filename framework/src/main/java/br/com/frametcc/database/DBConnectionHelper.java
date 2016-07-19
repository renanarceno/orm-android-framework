package br.com.frametcc.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import br.com.frametcc.database.api.DBConnection;
import br.com.frametcc.database.api.TableSpec;
import br.com.frametcc.database.dao.DatabaseDAO;
import br.com.frametcc.factory.DAOFactory;

public class DBConnectionHelper extends SQLiteOpenHelper implements DBConnection {

    protected static final String DB_TAG = "DATABASE";
    private final DBCreationHelper mDbCreationHelper;

    private DAOFactory daoFactory;
    private Map<Class, TableSpec> tablesSpec = new HashMap<>();
    private int currentVersion = 0;

    public DBConnectionHelper(Context context, String dbName, SQLiteDatabase.CursorFactory cursorFactory, int dbVersion, String dbCreateFolder, String dbUpdateFolder) {
        super(context, dbName, cursorFactory, dbVersion);
        this.currentVersion = dbVersion;
        this.mDbCreationHelper = new DBCreationHelper(context, dbCreateFolder, dbUpdateFolder);
    }

    @Override
    public void initDatabase() {
        boolean exists = mDbCreationHelper.checkIfDatabaseExists(getDatabaseName());
        if (!exists)
            getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(DB_TAG, "Creating database");
        int version = db.getVersion();
        if (version == 0)
            this.mDbCreationHelper.createFromAssets(db);
        if (this.currentVersion > version)
            this.mDbCreationHelper.updateDatabase(db, version, currentVersion);
        Log.d(DB_TAG, "Database created");
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.mDbCreationHelper.updateDatabase(db, oldVersion, newVersion);
        Log.d(DB_TAG, "Database updated");
    }

    @Override
    @SuppressWarnings("all")
    public <E> TableSpec<E> getTableSpec(Class<E> entityType) {
        TableSpec<E> tableSpec = tablesSpec.get(entityType);
        if (tableSpec == null) {
            tableSpec = TableSpec.createInstance(entityType);
            tablesSpec.put(entityType, tableSpec);
        }
        return tableSpec;
    }

    @Override
    public <T extends DatabaseDAO> T getDao(Class<T> clazz) {
        T dao = daoFactory.getDao(clazz);
        if (dao == null)
            throw new RuntimeException("Can't find a DAO implementation");
        return dao;
    }

    @Override
    public void setDaoFactory(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}