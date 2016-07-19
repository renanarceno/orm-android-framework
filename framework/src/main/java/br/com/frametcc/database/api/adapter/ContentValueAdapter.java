package br.com.frametcc.database.api.adapter;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe auxiliar que converte um tipo qualquer para string para ser colocado no mapa de ContentValues e também extrai do cursor e converte para o objeto correto
 *
 * @param <TYPE> classe do tipo que será convertido
 */
public interface ContentValueAdapter<TYPE> {

    /**
     * Insere em value o valor da forma correta.
     *
     * @param value      mapeador que contém todos os pares "nome da coluna" -> "valor"
     * @param columnName nome da coluna que será será adicionado em 'value'
     * @param o          objeto para ser inserido em 'value'
     */
    void putContentValue(ContentValues value, String columnName, TYPE o);

    /**
     * Extrai de cursor o valor convertido para o objeto correto
     * @param cursor Cursor que contém o dado a ser convertido
     * @param index posição dentro do cursor onde está o valor
     * @return retorna o objeto extraido do cursor e convertido para TYPE
     */
    TYPE getValueFromCursor(Cursor cursor, int index);

}
