package com.example.app.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.app.R;
import com.example.app.presenter.LoginPresenter;

import br.com.frametcc.shared.AbstractBaseActivityView;

public class LoginView extends AbstractBaseActivityView<LoginPresenter> {

    @Override
    public View onCreateView(LayoutInflater layoutInflater, Bundle savedInstanceState) {
        View v = layoutInflater.inflate(R.layout.activity_login, null);
        return v;
    }
}