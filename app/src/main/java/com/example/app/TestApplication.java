package com.example.app;

import com.example.app.model.LoginModel;
import com.example.app.presenter.LoginPresenter;
import com.example.app.view.LoginView;

import java.util.HashMap;
import java.util.Map;

import br.com.frametcc.TCCApplication;
import br.com.frametcc.database.DAOHelper;
import br.com.frametcc.database.dao.DatabaseDAO;
import br.com.frametcc.shared.api.BaseModel;
import br.com.frametcc.shared.api.BasePresenter;
import br.com.frametcc.shared.api.BaseView;

public class TestApplication extends TCCApplication {

    @Override
    public void create() {

    }

    @Override
    public Map<Class<? extends BaseView>, Class<? extends BasePresenter>> getControlViewMap() {
        Map<Class<? extends BaseView>, Class<? extends BasePresenter>> map = new HashMap<>();

        map.put(LoginView.class, LoginPresenter.class);

        return map;
    }

    @Override
    public Map<Class<? extends BasePresenter>, Class<? extends BaseModel>> getPresenterModelMap() {
        Map<Class<? extends BasePresenter>, Class<? extends BaseModel>> map = new HashMap<>();

        map.put(LoginPresenter.class, LoginModel.class);

        return map;
    }

    @Override
    public Map<Class<? extends DatabaseDAO>, DAOHelper> getDaosIntImpl() {
        return null;
    }
}
