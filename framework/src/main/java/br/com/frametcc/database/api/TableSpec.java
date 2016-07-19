package br.com.frametcc.database.api;

import android.content.ContentValues;
import android.database.Cursor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.frametcc.database.annotation.AnnotationHelper;
import br.com.frametcc.database.annotation.Column;
import br.com.frametcc.database.helper.ReflectionHelper;
import br.com.frametcc.database.helper.ValueAsString;

/**
 * Contém os dados sobre o objeto e sobre a tabela a qual ele está relacionado.
 *
 * @param <E> Classe que representa uma tabela no banco.
 */

public class TableSpec<E> {

    private final Class<E> entity;
    private final String tableName;
    private final Map<String, Field> primaryKeyMap;
    private final Map<String, Field> columnsMap;
    private final Map<String, Field> foreignKeyMap;
    private final TableObjectParser<E> objectParser;

    private TableSpec(Class<E> entity, String tableName, Map<String, Field> primaryKeyMap, Map<String, Field> columnsMap, Map<String, Field> foreignKeyMap) {
        this.objectParser = new TableObjectParser<E>();
        this.entity = entity;
        this.tableName = tableName;
        this.primaryKeyMap = primaryKeyMap;
        this.columnsMap = columnsMap;
        this.foreignKeyMap = foreignKeyMap;
    }

    /**
     * Cria uma instancia de TableSpec.
     *
     * @param entity Extrai as informações da classe para fazer a relação com a tabela que ela representa.
     * @return retorna uma instancia de TableSpec
     */
    @SuppressWarnings("all")
    public static <E> TableSpec<E> createInstance(Class<E> entity) {
        String tableName = AnnotationHelper.getTableName(entity);
        List<Field> fields = ReflectionHelper.getAnnotationFields(entity, Column.class);
        Map<String, Field> pkMap = new HashMap<>();
        Map<String, Field> colMap = new HashMap<>();
        Map<String, Field> foreignKeyMap = new HashMap<>();
        Column column;

        for (Field f : fields) {
            column = f.getAnnotation(Column.class);
            if (column.isAutoIncrementPrimaryKey() || column.isPrimaryKey())
                pkMap.put(column.name(), f);
            else if (!column.foreignKeyRef().equals(""))
                foreignKeyMap.put(column.name(), f);
            colMap.put(column.name(), f);
        }
        return new TableSpec(entity, tableName, pkMap, colMap, foreignKeyMap);
    }

    /**
     * @return retorna o mapa que conecta o nome do campo na tabela com o atributo do objeto.
     */
    public Map<String, Field> getPrimaryKeyMap() {
        return primaryKeyMap;
    }

    /**
     * @return retorna a string para a clausula 'where' da primary key. ex: "_id = ?, uuid = ?"
     */
    public String getPrimaryKeyWhereQuery() {
        return resolveSelection(getPrimaryKeyColumns());
    }

    /**
     * @return um array de strings contendo os nomes das colunas que são chaves primárias.
     */
    public String[] getPrimaryKeyColumns() {
        final Object[] objects = primaryKeyMap.keySet().toArray();
        String[] strings = new String[objects.length];
        for (int i = 0; i < objects.length; i++) {
            strings[i] = (String) objects[i];
        }
        return strings;
    }

    /**
     * @param obj Objeto para extrair os valores da primary key
     * @return um array de strings contendo todos os valores das chaves primárias.
     */
    public String[] getPrimaryKeyValues(E obj) {
        String[] columns = getPrimaryKeyColumns();
        String[] values = new String[columns.length];
        for (int i = 0; i < columns.length; i++) {
            final Field field = primaryKeyMap.get(columns[i]);
            final Object value = ReflectionHelper.getValue(field, obj);
            if (value != null)
                values[i] = ValueAsString.getAsString(value);
        }
        return values;
    }

    /**
     * @param obj objeto que contém as chaves primárias 'auto incrementável'
     * @param id  valor do novo id auto incrementavel
     */
    public void setAutoIncrementPrimaryKey(E obj, Long id) {
        for (Field f : primaryKeyMap.values()) ReflectionHelper.setFieldValue(f, obj, id);
    }

    /**
     * @return nome da tabela que este objeto TableSpec representa
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @return a classe que essa TableSpec representa
     */
    public Class<E> getEntity() {
        return entity;
    }

    /**
     * ContentValues é um mapeador utilizado pela API do android para inserir no banco os valores do tipo "nome da coluna" -> "valor"
     *
     * @param obj objeto que terá seus valores extraídos
     * @return o mapeador que contém: "nome da coluna" -> "valor"
     */
    public ContentValues getInsertContentValues(E obj) {
        return this.objectParser.getInsertContentValues(obj, this.columnsMap);
    }

    /**
     * Ao fazer uma requisição para a API do SQLite, é retornado um objeto do tipo cursor,
     * que podemos entender como um mapeador similar ao ContentValues ("nome da coluna" -> "valor")
     * Esse método serve para converter de Cursor para o objeto.
     *
     * @param cursor   cursor que contém os dados resultantes de uma consulta no banco
     * @param listener chamado ao converter os objetos
     * @return objeto extraído de Cursor
     * @see TableObjectParser
     */
    public E cursorToObject(Cursor cursor, DBListener<E> listener) {
        if (!cursor.moveToFirst()) return null;
        return this.objectParser.getObjectFromCursor(cursor, listener, getEntity(), this.columnsMap);
    }

    /**
     * Identico ao "CursorToObject", porém ao inves de retornar apenas um objeto, retorna uma lista de objetos extraídos de Cursor
     * @param cursor   cursor que contém os dados resultantes de uma consulta no banco
     * @param listener chamado ao converter os objetos
     * @return uma lista de objetos extraídos de Cursor
     * @see TableObjectParser
     */
    public List<E> cursorToObjectList(Cursor cursor, DBListener<E> listener) {
        if (!cursor.moveToFirst()) return null;
        return this.objectParser.getObjectListFromCursor(cursor, listener, getEntity(), this.columnsMap);
    }

    public List<E> cursorToObjectList(Cursor cursor) {
        return this.cursorToObjectList(cursor, null);
    }

    /**
     * Método auxiliar que extrai do atributo correto (atributo que está anotado com 'column') o valor que ele contém
     * @param obj objeto que será extraído o valor
     * @param column nome da coluna que será procurada entre os atributos de obj
     * @return valor contido no atributo que está anotado com o valor de column
     * @see Column
     * @see ReflectionHelper
     */
    public Object getValueFromColumn(E obj, String column) {
        return ReflectionHelper.getValue(this.columnsMap.get(column), obj);
    }

    private String resolveSelection(String[] keyName) {
        StringBuilder selection = new StringBuilder("");
        for (String s : keyName) {
            selection.append(s);
            selection.append(" =?, ");
        }
        String resp = selection.toString();
        int indexOf = resp.lastIndexOf(",");
        return resp.substring(0, indexOf);
    }
}