package br.com.frametcc.shared.api;

import android.content.Intent;
import android.os.Bundle;

import br.com.frametcc.FrameTCCApplication;

public interface BasePresenter<VIEW extends BaseView<?>> {

    public void init();

    public void setView(VIEW view);

    public <C extends BasePresenter<?>, CI extends C> CI getControl(Class<CI> control);

    FrameTCCApplication getApplication();

    public void onCreateActivity(Bundle savedInstanceState);

    public void onStartActivity();

    public void onResumeActivity();

    public void onPauseActivity();

    public void onStopActivity();

    public void onRestartActivity();

    public void onDestroyActivity();

    public void onBackPressedActivity();

    public void onActivityResult(int requestCode, int resultCode, Intent data);

    void onPreExecute();

    Object doInBackground(Object[] params);

    void onPostExecute(Object o);

    void onApplicationRestarted();
}
