package br.com.frametcc.database.api;

import br.com.frametcc.database.dao.DatabaseDAO;
import br.com.frametcc.factory.DAOFactory;

/**
 * Responsável por gerenciar as conexões com o banco.
 */
public interface DBConnection {
    /**
     * Inicializa o banco de dados. Força o sistema a criar as estruturas do banco.
     */
    void initDatabase();

    /**
     * @param entityType Classe de um objeto que representa uma tabela do banco
     * @return uma instancia de TableSpec, que contém as informações para converter entre objeto e tabela
     * @see TableSpec
     */
    @SuppressWarnings("all")
    <E> TableSpec<E> getTableSpec(Class<E> entityType);

    /**
     * Essa classe retorna a instancia DAO que comunica-se com o banco
     * @param clazz Interface do DAO
     * @return Um objeto DAO para se comunicar com o banco
     */
    <T extends DatabaseDAO> T getDao(Class<T> clazz);

    /**
     * Seta a classe responsável por ter as referencias das classes DAO
     * @param daoFactory classe que conterá as classes dao
     */
    void setDaoFactory(DAOFactory daoFactory);
}
