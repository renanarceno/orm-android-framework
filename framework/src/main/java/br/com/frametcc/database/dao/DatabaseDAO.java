package br.com.frametcc.database.dao;

import java.util.List;

public interface DatabaseDAO<E> {

    void insertOrUpdate(E obj);

    void insert(E obj);

    void updateWhere(E obj, String column);

    void delete(E obj);

    Long getMaxLongValue(String column);

    boolean exists(String column, Object value);

    List<E> listAll();

    int countAll();

    E getWhere(String whereQuery, Object... value);

    List<E> getWhereList(String whereQuery, Object... value);

    List<E> getWhereList(String whereQuery, String limit, Object... value);

    List<E> getWhereList(String whereQuery, String orderBy, String limit, Object... value);
}