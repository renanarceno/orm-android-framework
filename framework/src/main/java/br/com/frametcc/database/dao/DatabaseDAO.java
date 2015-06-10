package br.com.frametcc.database.dao;

public interface DatabaseDAO<E> {

    void insertOrUpdate(E obj);

    void deleteByPrimaryKey(E obj);

    E getByPrimaryKey(E obj);

}