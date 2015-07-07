package br.com.frametcc.shared;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import br.com.frametcc.TCCApplication;
import br.com.frametcc.shared.api.BaseView;
import br.com.frametcc.view.utils.ActivityNavigator;

public class ActivityControl {

    /* Objeto auxiliar para interagir com elementos da tela. */
    private ViewPassController pass;

    private Activity activity;
    private BaseView baseView;
    private ActivityNavigator navigator;
    private TCCApplication application;

    public ActivityControl(BaseView baseView) {
        this.baseView = baseView;
        this.activity = ((Activity) baseView);
    }

    public void onCreate(Bundle savedInstanceState) {
        this.navigator = new ActivityNavigator(activity);
        this.application = (TCCApplication) activity.getApplication();
        this.application.setupView(baseView);
        View view = baseView.onCreateView(activity.getLayoutInflater(), savedInstanceState);
        activity.setContentView(view);
        this.pass = new ViewPassController((ViewGroup) view);
        baseView.onAfterCreateView(savedInstanceState);
    }

    public ActivityNavigator getNavigator() {
        return navigator;
    }

    public TCCApplication getApplication() {
        return application;
    }

    public ViewPassController getPass() {
        return pass;
    }

}
