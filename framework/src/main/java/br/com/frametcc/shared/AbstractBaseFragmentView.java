package br.com.frametcc.shared;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import br.com.frametcc.TCCApplication;
import br.com.frametcc.shared.api.BasePresenter;
import br.com.frametcc.shared.api.BaseView;
import br.com.frametcc.view.utils.ActivityNavigator;

public abstract class AbstractBaseFragmentView<CONTROL extends BasePresenter<? extends BaseView<?>>> extends FragmentActivity implements BaseView<CONTROL> {

    protected CONTROL control;

    /* Objeto auxiliar para interagir com elementos da tela. */
    protected ViewPassController pass;

    public ActivityNavigator navigator;

    private TCCApplication application;

    @Override
    public ActivityNavigator getNavigator() {
        return navigator;
    }

    @Override
    public void setPresenter(CONTROL controller) {
        this.control = controller;
    }

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.navigator = new ActivityNavigator(this);
        this.application = (TCCApplication) this.getApplication();
        this.application.setupView(this);
        View view = this.createView(getLayoutInflater(), savedInstanceState);
        this.setContentView(view);
        this.pass = new ViewPassController((ViewGroup) view);
        this.onAfterCreateView(savedInstanceState);
        this.control.onCreateActivity(savedInstanceState);
    }

    @Override
    public void onApplicationRestarted() {
        this.control.onApplicationRestarted();
    }

    protected abstract View createView(LayoutInflater layoutInflater, Bundle savedInstanceState);

    public void onAfterCreateView(Bundle savedInstanceState) {
    }

    @Override
    protected void onStart() {
        this.control.onStartActivity();
        super.onStart();
    }

    @Override
    protected void onResume() {
        this.control.onResumeActivity();
        this.application.setTopActivity(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        this.control.onPauseActivity();
        super.onPause();
    }

    @Override
    protected void onStop() {
        this.control.onStopActivity();
        this.application.onActivityStopped(this);
        super.onStop();
    }

    @Override
    protected void onRestart() {
        this.application.onApplicationRestarted();
        this.control.onRestartActivity();
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        this.application.onActivityDestroyed(this);
        this.control.onDestroyActivity();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.control.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        this.control.onBackPressedActivity();
        super.onBackPressed();
    }

    @Override
    public void destroy() {
        super.finish();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    @Nullable
    public Bundle getExtras() {
        Intent intent = getIntent();
        if (intent != null) {
            return intent.getExtras();
        }
        return null;
    }
}