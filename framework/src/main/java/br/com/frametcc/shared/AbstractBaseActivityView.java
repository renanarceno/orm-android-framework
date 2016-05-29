package br.com.frametcc.shared;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import br.com.frametcc.shared.api.BaseModel;
import br.com.frametcc.shared.api.BasePresenter;
import br.com.frametcc.shared.api.BaseView;
import br.com.frametcc.view.utils.ActivityNavigator;

public abstract class AbstractBaseActivityView<PRESENTER extends BasePresenter<? extends BaseView<?>, ? extends BaseModel>> extends AppCompatActivity implements BaseView<PRESENTER> {

    protected PRESENTER presenter;

    private ActivityControl control;

    public PRESENTER getPresenter() {
        return presenter;
    }

    public ViewPassController getPass() {
        return control.getPass();
    }

    @Override
    public ActivityNavigator getNavigator() {
        return this.control.getNavigator();
    }

    @Override
    public void setPresenter(PRESENTER presenter) {
        this.presenter = presenter;
    }

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.control = new ActivityControl(this);
        this.control.onCreate(savedInstanceState);
        this.presenter.onCreateActivity(savedInstanceState);
    }

    @Override
    public void onApplicationRestarted() {
        this.presenter.onApplicationRestarted();
    }

    public void onAfterCreateView(Bundle savedInstanceState) {
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.presenter.onStartActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.presenter.onResumeActivity();
        this.control.getApplication().setTopActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.presenter.onPauseActivity();
    }

    @Override
    protected void onStop() {
        this.presenter.onStopActivity();
        this.control.getApplication().onActivityStopped(this);
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.control.getApplication().onApplicationRestarted();
        this.presenter.onRestartActivity();
    }

    @Override
    public void destroy() {
        this.presenter.destroy();
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.control.getApplication().onActivityDestroyed(this);
        this.presenter.onDestroyActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        this.presenter.onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.presenter.onBackPressedActivity();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Nullable
    public Bundle getExtras() {
        Intent intent = getIntent();
        if (intent != null) {
            return intent.getExtras();
        }
        return null;
    }

}