package br.com.frametcc.database.dao;

import java.util.List;

public interface DatabaseDAO<E> {

    void insertOrUpdate(E obj);

    void delete(E obj);

    List<E> listAll();

    E getWhere(String columnName, Object value);

    List<E> getWhereList(String columnName, Object value);
}