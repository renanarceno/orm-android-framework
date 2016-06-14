package com.example.app.presenter;

import android.os.Bundle;
import android.util.Log;

import com.example.app.model.LoginModel;
import com.example.app.view.LoginView;

import br.com.frametcc.shared.AbstractBasePresenter;

public class LoginPresenter extends AbstractBasePresenter<LoginView, LoginModel> {

	@Override
	public void onCreateActivity(Bundle savedInstanceState) {
		super.onCreateActivity(savedInstanceState);
	}

	@Override
	public void onResumeActivity() {
		this.model.teste();
	}
}