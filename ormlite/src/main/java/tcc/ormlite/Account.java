package tcc.ormlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "account")
public class Account {

	@DatabaseField(generatedId = true, columnName = "id")
	public Long id;

	@DatabaseField(columnName = "name")
	public String name;

	@DatabaseField(canBeNull = false, columnName = "password")
	public String password;

}