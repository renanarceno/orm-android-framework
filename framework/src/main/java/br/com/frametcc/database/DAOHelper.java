package br.com.frametcc.database;

import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import br.com.frametcc.TCCApplication;
import br.com.frametcc.database.api.DBListener;
import br.com.frametcc.database.dao.DatabaseDAO;
import br.com.frametcc.database.helper.QueryHelper;

public abstract class DAOHelper<E> implements DatabaseDAO<E>, DBListener<E> {

    protected final QueryHelper<E> queryHelper;
    private final Class<E> typeClass;

    @SuppressWarnings("all")
    public DAOHelper() {
        // Pega a classe de 'E'
        this.typeClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.queryHelper = new QueryHelper<>(typeClass);
        this.queryHelper.setListener(this);
    }

    protected SQLiteDatabase getReadableDatabase() {
        return TCCApplication.getDbConnection().getReadableDatabase();
    }

    protected SQLiteDatabase getWritableDatabase() {
        return TCCApplication.getDbConnection().getWritableDatabase();
    }

    public <A extends DatabaseDAO> A getDao(Class<A> clazz) {
        return TCCApplication.getDbConnection().getDao(clazz);
    }

    public synchronized void insert(E obj) {
        SQLiteDatabase database = getWritableDatabase();
        this.queryHelper.insert(database, obj);
    }

    public synchronized void update(E obj) {
        SQLiteDatabase database = getWritableDatabase();
        this.queryHelper.update(database, obj);
    }

    public synchronized void insertOrUpdate(E obj) {
        SQLiteDatabase database = getWritableDatabase();
        this.queryHelper.insertOrUpdate(database, obj);
    }

    @Override
    public void updateWhere(E obj, String column) {
        this.queryHelper.updateWhere(getWritableDatabase(), obj, column);
    }

    @Override
    public void delete(E obj) {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            this.queryHelper.delete(database, obj);
            database.setTransactionSuccessful();
        } catch (Exception ignore) {
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    @Override
    public Long getMaxLongValue(String column) {
        return this.queryHelper.getMaxValue(getReadableDatabase(), column);
    }

    @Override
    public boolean exists(String column, Object value) {
        return this.queryHelper.existInDatabase(getReadableDatabase(), new String[]{column}, value);
    }

    @Override
    public List<E> listAll() {
        return this.queryHelper.listAll(getReadableDatabase());
    }

    @Override
    public int countAll() {
        return this.queryHelper.countAll(getReadableDatabase());
    }

    @Override
    public E getWhere(String columns, Object... value) {
        return this.queryHelper.getWhere(getReadableDatabase(), columns, value);
    }

    @Override
    public List<E> getWhereList(String columns, Object... value) {
        return this.queryHelper.getWhereList(getReadableDatabase(), columns, null, null, null, null, value);
    }

    @Override
    public List<E> getWhereList(String whereQuery, String limit, Object... value) {
        return this.queryHelper.getWhereList(getReadableDatabase(), whereQuery, null, null, null, limit, value);
    }

    @Override
    public List<E> getWhereList(String whereQuery, String orderBy, String limit, Object... value) {
        return this.queryHelper.getWhereList(getReadableDatabase(), whereQuery, null, null, orderBy, limit, value);
    }

    public void setRetrieveListener(DBListener<E> listener) {
        this.queryHelper.setListener(listener);
    }

    @Override
    public void onRetrieve(E obj) {
    }

    public Class<E> getTypeClass() {
        return typeClass;
    }
}