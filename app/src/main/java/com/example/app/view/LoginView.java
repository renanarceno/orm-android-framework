package com.example.app.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.app.R;
import com.example.app.presenter.LoginPresenter;

import br.com.frametcc.shared.AbstractBaseActivityView;

public class LoginView extends AbstractBaseActivityView<LoginPresenter> {

	private TextView remove;
	private TextView update;
	private TextView insert;

	@Override
	public View onCreateView(LayoutInflater layoutInflater, Bundle savedInstanceState) {
		return layoutInflater.inflate(R.layout.activity_main, null);
	}

	@Override
	protected void onResume() {
		insert = (TextView) findViewById(R.id.insert);
		update = (TextView) findViewById(R.id.update);
		remove = (TextView) findViewById(R.id.remove);
		super.onResume();
	}

	public void setInsertMsg(String msg) {
		insert.setText(msg);
	}

	public void setUpdateMsg(String msg) {
		update.setText(msg);
	}

	public void setRemoveMsg(String msg) {
		remove.setText(msg);
	}

}