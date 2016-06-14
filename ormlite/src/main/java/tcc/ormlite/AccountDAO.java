package tcc.ormlite;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;

public class AccountDAO extends BaseDaoImpl<Account, Integer> {

	protected AccountDAO(Class dataClass) throws SQLException {
		super(dataClass);
	}

	protected AccountDAO(ConnectionSource connectionSource, Class dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	protected AccountDAO(ConnectionSource connectionSource, DatabaseTableConfig tableConfig) throws SQLException {
		super(connectionSource, tableConfig);
	}
}