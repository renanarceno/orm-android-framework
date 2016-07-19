package br.com.frametcc.database.dao;

import java.util.List;

/**
 * Contrato com alguns métodos padrões que facilitam algumas operações no banco.
 *
 * @param <E> entidade que representa uma tabela do banco
 */
public interface DatabaseDAO<E> {

    /**
     * @param obj será inserido no banco ou atualizado se ja existir
     */
    void insertOrUpdate(E obj);

    /**
     * @param obj força a inserção do objeto no banco
     */
    void insert(E obj);

    /**
     * Pega o valor que está setado no objeto no atributo anotado com o mesmo nome de 'column' e atualiza procurando por ele na tabela
     *
     * @param obj    objeto para ser atualizado a partir da coluna 'column'
     * @param column nome da coluna que sera utilizado na clausua 'where' para o UPDATE
     */
    void updateWhere(E obj, String column);

    /**
     * @param obj atualiza atraves da chave primaria
     */
    void update(E obj);

    /**
     * @param obj deleta atraves da chave primaria
     */
    void delete(E obj);

    /**
     * @param column coluna para obter o maior valor
     * @return o maior valor que existe na coluna
     */
    Long getMaxLongValue(String column);

    /**
     * Verifica se existe certa entrada na tabela
     *
     * @param column nome da coluna para ser verificada
     * @param value  valor que será pesquisado
     * @return true se existir
     */
    boolean exists(String column, Object value);

    /**
     * @return lista todos os objetos da tabela
     */
    List<E> listAll();

    /**
     * @return conta quantas entradas existem em uma tabela
     */
    int countAll();

    /**
     * Faz uma query atraves dos nome das colunas e seus valores
     * @param columns colunas a serem pesquisadas
     * @param value valor na coluna
     * @return retorna o objeto encontrado, ou nullo se não encontrar nenhum
     */
    E getWhere(String columns, Object... value);

    /**
     * Faz uma query atraves dos nome das colunas e seus valores
     * @param columns colunas a serem pesquisadas
     * @param value valor na coluna
     * @return retorna uma lista de objetos encontrados, ou nullo se não encontrar nenhum
     */
    List<E> getWhereList(String columns, Object... value);

    List<E> getWhereList(String whereQuery, String limit, Object... value);

    List<E> getWhereList(String whereQuery, String orderBy, String limit, Object... value);
}