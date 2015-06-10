package br.com.frametcc.shared.api;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import br.com.frametcc.view.utils.ActivitySailor;

public interface BaseView<CONTROL extends BasePresenter> {

    void onApplicationRestarted();

    ActivitySailor getActivitySailor();

    void setPresenter(CONTROL controller);

    void onBackPressed();

    View onCreateView(LayoutInflater layoutInflater, Bundle savedInstanceState);

    void onAfterCreateView(Bundle savedInstanceState);

    void destroy();

    void showToast(String msg);

    Bundle getExtras();

}