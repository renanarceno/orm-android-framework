package tcc.ormlite;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

	private TextView remove;
	private TextView insert;
	private TextView update;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		insert = (TextView) findViewById(R.id.insert);
		update = (TextView) findViewById(R.id.update);
		remove = (TextView) findViewById(R.id.remove);

		new Loader().execute();
	}

	class Loader extends AsyncTask<Object, Object, Object> {
		@Override
		protected Object doInBackground(Object... params) {

			deleteDatabase("banco.db");

			ConnectionSource c = new AndroidConnectionSource(new SQLiteOpenHelper(MainActivity.this, "banco.db", null, 1) {
				@Override
				public void onCreate(SQLiteDatabase db) {
					db.execSQL("create table account(id integer primary key autoincrement, name text, password text)");
				}

				@Override
				public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

				}
			});

			Dao<Account, Integer> accDao = null;
			try {
				accDao = new AccountDAO(c, Account.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			double start = (double) System.currentTimeMillis();
			for (int i = 0; i < 10000; i++) {
				Account conta = new Account();
				conta.name = RandomString.getRandomString(10);
				conta.password = RandomString.getRandomString(10);
				try {
					accDao.create(conta);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			double total = (System.currentTimeMillis() - start) / 1000.0;
			final double finalTotal = total;
			MainActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					insert.setText("Tempo decorrido INSERT: " + finalTotal + "s");
				}
			});
			String tag = "ORM_LITE";
			Log.v(tag, "INSERT 10.000 entidades");
			Log.v(tag, "Tempo decorrido INSERT: " + total + "s");
			Log.v(tag, "Tempo médio por entidade: " + (total / 10000.0) + "s");


			Log.v(tag, "Update 10.000 entidades");
			start = (double) System.currentTimeMillis();
			for (int i = 0; i < 10000; i++) {
				Account conta = new Account();
				conta.id = (long) i;
				conta.name = RandomString.getRandomString(10);
				conta.password = RandomString.getRandomString(10);
				try {
					accDao.update(conta);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			total = (System.currentTimeMillis() - start) / 1000.0;
			final double finalTotal1 = total;
			MainActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					update.setText("Tempo decorrido UPDATE: " + finalTotal1 + "s");
				}
			});
			Log.v(tag, "Tempo decorrido UPDATE: " + total + "s");
			Log.v(tag, "Tempo médio por entidade: " + (total / 10000.0) + "s");

			Log.v(tag, "DELETE 10.000 entidades");
			start = (double) System.currentTimeMillis();
			for (int i = 0; i < 10000; i++) {
				Account conta = new Account();
				conta.id = (long) i;
				try {
					accDao.delete(conta);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			total = (System.currentTimeMillis() - start) / 1000.0;
			final double finalTotal2 = total;
			MainActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					remove.setText("Tempo decorrido DELETE: " + finalTotal2 + "s");
				}
			});
			Log.v(tag, "Tempo decorrido DELETE: " + total + "s");
			Log.v(tag, "Tempo médio por entidade: " + (total / 10000.0) + "s");

			return null;
		}
	}

}