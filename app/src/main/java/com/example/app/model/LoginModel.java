package com.example.app.model;

import android.util.Log;

import com.example.app.presenter.LoginPresenter;

import br.com.frametcc.shared.AbstractBaseModel;

public class LoginModel extends AbstractBaseModel<LoginPresenter> {

    @Override
    public void init() {
        Log.v("MODEEEEELLE", "************************ MODEL \nINIT ************************");
    }

    public void teste() {

        Log.v("TESTSETESTS", "************************ TESTE ************************");

    }
}