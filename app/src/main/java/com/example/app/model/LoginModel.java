package com.example.app.model;

import android.os.AsyncTask;
import android.util.Log;

import com.example.app.dao.AccountDAO;
import com.example.app.entity.Account;
import com.example.app.entity.RandomString;
import com.example.app.presenter.LoginPresenter;

import java.sql.SQLException;

import br.com.frametcc.TCCApplication;
import br.com.frametcc.shared.AbstractBaseModel;

public class LoginModel extends AbstractBaseModel<LoginPresenter> {

	private Loader loader;

	@Override
	public void init() {
		loader = new Loader();
	}

	public void teste() {
		if (loader.getStatus() == AsyncTask.Status.PENDING) loader.execute();
	}

	class Loader extends AsyncTask<Object, Object, Object> {
		@Override
		protected Object doInBackground(Object... params) {

			AccountDAO dao = getDAO(AccountDAO.class);
			double start = (double) System.currentTimeMillis();
			for (int i = 0; i < 10000; i++) {
				Account conta = new Account();
				conta.name = RandomString.getRandomString(10);
				conta.password = RandomString.getRandomString(10);
				dao.insert(conta);
			}
			double total = (System.currentTimeMillis() - start) / 1000.0;
			presenter.setInsertMsg("Tempo decorrido INSERT: " + total + "s");

			String tag = "APP_ORM";
			Log.v(tag, "insert 10.000 entidades");
			Log.v(tag, "Tempo decorrido INSERT: " + total + "s");
			Log.v(tag, "Tempo médio por entidade: " + (total / 10000.0) + "s");
			start = (double) System.currentTimeMillis();
			for (int i = 0; i < 10000; i++) {
				Account conta = new Account();
				conta.id = (long) i;
				conta.name = RandomString.getRandomString(10);
				conta.password = RandomString.getRandomString(10);
				dao.update(conta);
			}
			total = (System.currentTimeMillis() - start) / 1000.0;
			presenter.setUpdateMsg("Tempo decorrido UPDATE: " + total + "s");

			Log.v(tag, "Update em 10.000 entidades");
			Log.v(tag, "Tempo decorrido UPDATE: " + total + "s");
			Log.v(tag, "Tempo médio por entidade: " + (total / 10000.0) + "s");

			start = (double) System.currentTimeMillis();

			for (int i = 0; i < 10000; i++) {
				Account conta = new Account();
				conta.id = (long) i;
				dao.delete(conta);
			}
			total = (System.currentTimeMillis() - start) / 1000.0;
			presenter.setRemoveMsg("Tempo decorrido DELETE: " + total + "s");
			Log.v(tag, "DELETE em 10.000 entidades");
			Log.v(tag, "Tempo decorrido DELETE: " + total + "s");
			Log.v(tag, "Tempo médio por entidade: " + (total / 10000.0) + "s");
			return null;
		}
	}
}