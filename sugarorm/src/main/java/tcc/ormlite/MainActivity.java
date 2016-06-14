package tcc.ormlite;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.orm.SugarContext;
import com.orm.SugarDb;
import com.orm.SugarRecord;
import com.orm.util.SugarConfig;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SugarContext.init(this);
		new Loader().execute();
	}

	class Loader extends AsyncTask<Object, Object, Object> {
		@Override
		protected Object doInBackground(Object... params) {

			deleteDatabase("banco.db");

			new SQLiteOpenHelper(MainActivity.this, "banco.db", null, 1) {
				@Override
				public void onCreate(SQLiteDatabase db) {
					db.execSQL("create table account(id integer primary key autoincrement, name text, password text)");
				}

				@Override
				public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

				}
			};


			double start = (double) System.currentTimeMillis();
			for (int i = 0; i < 10000; i++) {
				Account conta = new Account();
				conta.name = RandomString.getRandomString(10);
				conta.password = RandomString.getRandomString(10);
				conta.save();
			}
			double total = (System.currentTimeMillis() - start) / 1000.0;
			String tag = "SUGARORM";
			Log.v(tag, "INSERT 10.000 entidades");
			Log.v(tag, "Tempo decorrido INSERT: " + total + "s");
			Log.v(tag, "Tempo médio por entidade: " + (total / 10000.0) + "s");


			Log.v(tag, "Update 10.000 entidades");

			start = (double) System.currentTimeMillis();
			for (int i = 0; i < 10000; i++) {
				Log.v(tag, "update");
				Account conta = new Account();
				conta.setId((long) i);
				conta.name = RandomString.getRandomString(10);
				conta.password = RandomString.getRandomString(10);
				conta.update();
			}
			total = (System.currentTimeMillis() - start) / 1000.0;
			Log.v(tag, "Tempo decorrido UPDATE: " + total + "s");
			Log.v(tag, "Tempo médio por entidade: " + (total / 10000.0) + "s");

			Log.v(tag, "DELETE 10.000 entidades");
			start = (double) System.currentTimeMillis();
			for (int i = 0; i < 10000; i++) {
				Account conta = new Account();
				conta.id = (long) i;
				conta.delete();
			}
			total = (System.currentTimeMillis() - start) / 1000.0;
			Log.v(tag, "Tempo decorrido DELETE: " + total + "s");
			Log.v(tag, "Tempo médio por entidade: " + (total / 10000.0) + "s");

			return null;
		}
	}

}