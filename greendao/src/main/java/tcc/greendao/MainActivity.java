package tcc.greendao;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

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

			DaoMaster.DevOpenHelper sess = new DaoMaster.DevOpenHelper(MainActivity.this, "banco.db", null);
			DaoMaster dao = new tcc.greendao.DaoMaster(sess.getWritableDatabase());
			AccountDao accountDao = dao.newSession().getAccountDao();

			double start = (double) System.currentTimeMillis();
			for (int i = 0; i < 10000; i++) {
				Account conta = new Account();
				conta.setName(RandomString.getRandomString(10));
				conta.setPassword(RandomString.getRandomString(10));
				accountDao.insert(conta);
			}
			double total = (System.currentTimeMillis() - start) / 1000.0;
			final double finalTotal = total;
			MainActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					insert.setText("Tempo decorrido INSERT: " + finalTotal + "s");
				}
			});
			String tag = "GREEN DAO";
			Log.v(tag, "INSERT 10.000 entidades");
			Log.v(tag, "Tempo decorrido INSERT: " + total + "s");
			Log.v(tag, "Tempo médio por entidade: " + (total / 10000.0) + "s");


			Log.v(tag, "Update 10.000 entidades");
			start = (double) System.currentTimeMillis();
			for (int i = 0; i < 10000; i++) {
				Account conta = new Account();
				conta.setId((long) i);
				conta.setName(RandomString.getRandomString(10));
				conta.setPassword(RandomString.getRandomString(10));
				accountDao.update(conta);
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
				conta.setId((long) i);
				accountDao.delete(conta);
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