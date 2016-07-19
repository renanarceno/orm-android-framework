package br.com.frametcc.database.api;

/**
 * Listener para os objetos obtidos em alguma query no banco
 * @param <E> tipo de objeto retirado do banco
 */
public interface DBListener<E> {
    /**
     * @param obj Objeto resultante da consulta no banco
     */
    void onRetrieve(E obj);
}
