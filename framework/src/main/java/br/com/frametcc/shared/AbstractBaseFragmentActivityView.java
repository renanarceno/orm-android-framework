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
import br.com.frametcc.shared.api.BaseModel;
import br.com.frametcc.shared.api.BasePresenter;
import br.com.frametcc.shared.api.BaseView;
import br.com.frametcc.view.utils.ActivityNavigator;

public abstract class AbstractBaseFragmentActivityView<PRESENTER extends BasePresenter<? extends BaseView<?>, ? extends BaseModel>> extends FragmentActivity implements BaseView<PRESENTER> {

    protected PRESENTER presenter;
    protected ViewPassController pass;
    public ActivityNavigator navigator;
    private TCCApplication application;

    @Override
    public ActivityNavigator getNavigator() {
        return navigator;
    }

    @Override
    public void setPresenter(PRESENTER presenter) {
        this.presenter = presenter;
    }

    @Override
    public PRESENTER getPresenter() {
        return presenter;
    }

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.navigator = new ActivityNavigator(this);
        this.application = (TCCApplication) this.getApplication();
        this.application.setupView(this);
        View view = this.onCreateView(getLayoutInflater(), savedInstanceState);
        this.setContentView(view);
        this.pass = new ViewPassController((ViewGroup) view);
        this.onAfterCreateView(savedInstanceState);
        this.presenter.onCreateActivity(savedInstanceState);
    }

    @Override
    public abstract View onCreateView(LayoutInflater layoutInflater, Bundle savedInstanceState);

    @Override
    public void onApplicationRestarted() {
        this.presenter.onApplicationRestarted();
    }

    public void onAfterCreateView(Bundle savedInstanceState) {
    }

    @Override
    protected void onStart() {
        this.presenter.onStartActivity();
        super.onStart();
    }

    @Override
    protected void onResume() {
        this.presenter.onResumeActivity();
        this.application.setTopActivity(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        this.presenter.onPauseActivity();
        super.onPause();
    }

    @Override
    protected void onStop() {
        this.presenter.onStopActivity();
        this.application.onActivityStopped(this);
        super.onStop();
    }

    @Override
    protected void onRestart() {
        this.application.onApplicationRestarted();
        this.presenter.onRestartActivity();
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        this.application.onActivityDestroyed(this);
        this.presenter.onDestroyActivity();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        this.presenter.onBackPressedActivity();
        super.onBackPressed();
    }

    @Override
    public void destroy() {
        this.presenter.destroy();
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