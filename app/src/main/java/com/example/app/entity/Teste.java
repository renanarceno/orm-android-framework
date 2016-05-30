package com.example.app.entity;

import br.com.frametcc.database.annotation.Column;
import br.com.frametcc.database.annotation.Table;

@Table("Teste")
public class Teste {

    @Column(name = "ID", isPrimaryKey = true)
    public long id;

    @Column(name = "TESTE_TEXT")
    public String testeText;

    @Column(name = "TESTE_DOUBLE")
    public Double testeDouble;

    @Column(name = "TESTE_BLOB")
    public byte[] testeBlob;

}