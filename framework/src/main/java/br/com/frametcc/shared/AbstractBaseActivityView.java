package br.com.frametcc.shared;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import br.com.frametcc.FrameTCCApplication;
import br.com.frametcc.shared.api.BasePresenter;
import br.com.frametcc.shared.api.BaseView;
import br.com.frametcc.view.utils.ActivitySailor;

public abstract class AbstractBaseActivityView<PRESENTER extends BasePresenter<? extends BaseView<?>>> extends ActionBarActivity implements BaseView<PRESENTER> {

    public ActivitySailor activitySailor;
    protected ViewPassController pass;
    protected PRESENTER presenter;
    private FrameTCCApplication application;

    @Override
    public ActivitySailor getActivitySailor() {
        return activitySailor;
    }

    @Override
    public void setPresenter(PRESENTER presenter) {
        this.presenter = presenter;
    }

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activitySailor = new ActivitySailor(this);
        this.application = (FrameTCCApplication) this.getApplication();
        this.application.setupView(this);
        View view = this.onCreateView(getLayoutInflater(), savedInstanceState);
        this.setContentView(view);
        this.pass = new ViewPassController((ViewGroup) view);
        this.onAfterCreateView(savedInstanceState);
        this.presenter.onCreateActivity(savedInstanceState);
    }

    @Override
    public void onApplicationRestarted() {
        this.presenter.onApplicationRestarted();
    }

    @Override
    public abstract void onAfterCreateView(Bundle savedInstanceState);

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
    public void destroy() {
        super.finish();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Nullable
    public Bundle getExtras() {
        Intent intent = getIntent();
        if(intent != null) {
            return intent.getExtras();
        }
        return null;
    }

}