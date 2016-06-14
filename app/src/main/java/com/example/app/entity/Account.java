package com.example.app.entity;

import br.com.frametcc.database.annotation.Column;
import br.com.frametcc.database.annotation.Table;

@Table("account")
public class Account {

	@Column(name = "id", isAutoIncrementPrimaryKey = true)
	public Long id;

	@Column(name = "name")
	public String name;

	@Column(name = "password")
	public String password;

}