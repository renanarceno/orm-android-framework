package br.com.frametcc.shared.api;

import android.content.Intent;
import android.os.Bundle;

import br.com.frametcc.TCCApplication;

public interface BasePresenter<VIEW extends BaseView<?>> {

    String PRESENTER_IMPL = "tccframework.presenterImpl";
    String PRESENTER_INTERFACE = "tccframework.presenterInterface";

    void init();

    void setView(VIEW view);

    <C extends BasePresenter<?>, CI extends C> CI getControl(Class<CI> control);

    TCCApplication getApplication();

    void onCreateActivity(Bundle savedInstanceState);

    void onStartActivity();

    void onResumeActivity();

    void onPauseActivity();

    void onStopActivity();

    void onRestartActivity();

    void onDestroyActivity();

    void onBackPressedActivity();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onPreExecute();

    Object doInBackground(Object[] params);

    void onPostExecute(Object o);

    void onApplicationRestarted();
}
