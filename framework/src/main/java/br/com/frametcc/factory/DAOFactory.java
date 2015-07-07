package br.com.frametcc.factory;

import android.content.Context;

import java.util.Map;

import br.com.frametcc.database.DAOHelper;
import br.com.frametcc.database.DBConnectionHelper;
import br.com.frametcc.database.dao.DatabaseDAO;

public class DAOFactory {
    public Map<Class<? extends DatabaseDAO>, DAOHelper> daos;

    protected Context context;

    public DAOFactory(Context context) {
        this.context = context;
    }

    @SuppressWarnings("unchecked")
    public <T extends DatabaseDAO> T getDao(Class<T> clazz) {
        return (T) daos.get(clazz);
    }
}
