package br.com.frametcc.shared.api;

import android.content.Intent;
import android.os.Bundle;

import br.com.frametcc.TCCApplication;

public interface BasePresenter<VIEW extends BaseView<?>, MODEL extends BaseModel<?>> {

    String PRESENTER_IMPL = "tccframework.presenterImpl";
    String PRESENTER_INTERFACE = "tccframework.presenterInterface";

    void init();

    void destroy();

    void setModel(MODEL model);

    void setView(VIEW view);

    <C extends BasePresenter<?, ?>, CI extends C> CI getPresenter(Class<CI> control);

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

    void onApplicationRestarted();

    void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults);
}
