package br.com.frametcc.database.api;

public interface DBListener<E> {

    void onRetrieve(E obj);

}
