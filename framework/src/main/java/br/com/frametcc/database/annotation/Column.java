package br.com.frametcc.database.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation responsável por conter as informações da coluna que o atributo do objeto representa. Seu nome, se é uma chave primária auto incrementável, etc.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    /**
     * Nome da coluna que o atributo representa
     */
    String name();

    /**
     * Se a coluna que o atributo representa é auto incrementável
     */
    boolean isAutoIncrementPrimaryKey() default false;

    /**
     * Se a coluna que o atributo representa é apenas uma chave primária
     */
    boolean isPrimaryKey() default false;

    /**
     * Se a coluna é uma chave secundária, contém o nome da coluna da outra tabela
     */
    String foreignKeyRef() default "";

}