package com.example.app.entity;

import br.com.frametcc.database.annotation.Column;
import br.com.frametcc.database.annotation.Table;

@Table("Teste")
public class Teste {

    @Column(name = "id", isPrimaryKey = true)
    public long id;



}