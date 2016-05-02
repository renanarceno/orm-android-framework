package br.com.frametcc.database.api;

import br.com.frametcc.database.dao.DatabaseDAO;
import br.com.frametcc.factory.DAOFactory;

public interface DBConnection {
    void initDatabase();

    @SuppressWarnings("all")
    <E> TableSpec<E> getTableSpec(Class<E> entityType);

    void addTableSpec(Class clazz);

    <T extends DatabaseDAO> T getDao(Class<T> clazz);

    void setDaoFactory(DAOFactory daoFactory);
}
