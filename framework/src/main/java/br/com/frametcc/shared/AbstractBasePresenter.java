package br.com.frametcc.shared;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import br.com.frametcc.TCCApplication;
import br.com.frametcc.database.dao.DatabaseDAO;
import br.com.frametcc.shared.api.BaseModel;
import br.com.frametcc.shared.api.BasePresenter;
import br.com.frametcc.shared.api.BaseView;

public abstract class AbstractBasePresenter<VIEW extends BaseView<?>, MODEL extends BaseModel<?>> implements BasePresenter<VIEW, MODEL> {

    protected MODEL model;
    protected VIEW view;

    @Override
    public void init() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void setModel(MODEL model) {
        this.model = model;
    }

    public void setView(VIEW view) {
        this.view = view;
    }

    @Override
    public <C extends BasePresenter<?, ?>, CI extends C> CI getPresenter(Class<CI> presenter) {
        return getApplication().getControl(presenter);
    }

    /**
     * @deprecated Utilizar método do Model.
     */
    @Deprecated
    public <T extends DatabaseDAO> T getDao(Class<T> dao) {
        return getApplication().getDao(dao);
    }

    @Override
    public TCCApplication getApplication() {
        if (this.view instanceof Fragment)
            return (TCCApplication) ((Fragment) this.view).getActivity().getApplication();
        else
            return (TCCApplication) ((Activity) this.view).getApplication();
    }

    @Override
    public void onCreateActivity(Bundle savedInstanceState) {
    }

    @Override
    public void onStartActivity() {
    }

    @Override
    public void onPauseActivity() {
    }

    @Override
    public void onStopActivity() {
    }

    @Override
    public void onApplicationRestarted() {
    }

    @Override
    public void onRestartActivity() {
    }

    @Override
    public void onDestroyActivity() {
    }

    @Override
    public void onResumeActivity() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
    }

    @Override
    public void onBackPressedActivity() {
    }

}