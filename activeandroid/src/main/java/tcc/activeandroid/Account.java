package tcc.activeandroid;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Account")
public class Account extends Model{

	@Column(name = "_id")
	public Long id;
	@Column(name = "name")
	public String name;
	@Column(name = "password")
	public String password;

}