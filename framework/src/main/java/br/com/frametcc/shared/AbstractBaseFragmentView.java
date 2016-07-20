package br.com.frametcc.shared;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public abstract class AbstractBaseFragmentView<PRESENTER extends BasePresenter<? extends BaseView<?>, ? extends BaseModel>> extends Fragment implements BaseView<PRESENTER> {

    public PRESENTER presenter;
    public ViewPassController pass;
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
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        this.navigator = new ActivityNavigator(activity);
        this.application = (TCCApplication) activity.getApplication();
        this.application.setupView(this);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, Bundle savedInstanceState) {
        return null;
    }

    public ViewPassController getPass() {
        return pass;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.pass = new ViewPassController((ViewGroup) view);
        onAfterCreateView(savedInstanceState);
        this.presenter.onCreateActivity(savedInstanceState);
    }

    @Override
    public void onApplicationRestarted() {
        this.presenter.onApplicationRestarted();
    }

    public void onAfterCreateView(Bundle savedInstanceState) {
    }

    @Override
    public void onStart() {
        this.presenter.onStartActivity();
        super.onStart();
    }

    @Override
    public void onResume() {
        this.presenter.onResumeActivity();
        this.application.setTopActivity(this.getActivity());
        super.onResume();
    }

    @Override
    public void onPause() {
        this.presenter.onPauseActivity();
        super.onPause();
    }

    @Override
    public void onStop() {
        this.presenter.onStopActivity();
        this.application.onActivityStopped(this.getActivity());
        super.onStop();
    }

    @Override
    public void onDestroy() {
        this.application.onActivityDestroyed(this.getActivity());
        this.presenter.onDestroyActivity();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        this.presenter.onBackPressedActivity();
        getActivity().onBackPressed();
    }

    @Override
    public void destroy() {
        this.presenter.destroy();
        getActivity().finish();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this.getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    @Nullable
    public Bundle getExtras() {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            return intent.getExtras();
        }
        return null;
    }
}