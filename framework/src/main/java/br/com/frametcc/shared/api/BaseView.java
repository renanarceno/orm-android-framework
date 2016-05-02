package br.com.frametcc.shared.api;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import br.com.frametcc.view.utils.ActivityNavigator;

public interface BaseView<CONTROL extends BasePresenter> {

    View onCreateView(LayoutInflater layoutInflater, Bundle savedInstanceState);

    void onAfterCreateView(Bundle savedInstanceState);

    void onApplicationRestarted();

    ActivityNavigator getNavigator();

    void setPresenter(CONTROL controller);

    CONTROL getPresenter();

    void onBackPressed();

    void destroy();

    void showToast(String msg);

    Bundle getExtras();

}