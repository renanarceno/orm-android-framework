package br.com.frametcc.database.annotation;

/**
 * Classe que auxilia na leitura das annotations.
 */
public class AnnotationHelper {

    /**
     * Extrai da classe, o nome da tabela relacionada.
     * @param clazz Classe para extrair o nome da tabela.
     * @return Nome da tabela
     */
    public static String getTableName(Class<?> clazz) {
        Table tableName = clazz.getAnnotation(Table.class);
        if (tableName == null || "".equals(tableName.value())) {
            throw new RuntimeException("Annotation " + Table.class.getSimpleName() + " não existe na classe: " + clazz.getSimpleName() + ".");
        }
        return tableName.value();
    }
}