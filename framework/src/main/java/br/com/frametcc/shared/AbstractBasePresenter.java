package br.com.frametcc.shared;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import br.com.frametcc.FrameTCCApplication;
import br.com.frametcc.control.api.ConnectionCheckerHelper;
import br.com.frametcc.database.dao.DatabaseDAO;
import br.com.frametcc.shared.api.BasePresenter;
import br.com.frametcc.shared.api.BaseView;

public abstract class AbstractBasePresenter<VIEW extends BaseView<?>> implements BasePresenter<VIEW> {

    protected ConnectionCheckerHelper connChecker;
    protected VIEW view;
    private List<AsyncTask> tasks;

    @Override
    public final void init() {
        this.connChecker = new ConnectionCheckerHelper((android.content.Context) view);
    }

    public void setView(VIEW view) {
        this.view = view;
    }

    @Override
    @Nullable
    public <C extends BasePresenter<?>, CI extends C> CI getControl(Class<CI> control) {
        return getApplication().getControl(control);
    }

    public <T extends DatabaseDAO> T getDao(Class<T> dao) {
        return getApplication().getDao(dao);
    }

    @Override
    public FrameTCCApplication getApplication() {
        return (FrameTCCApplication) ((Activity) this.view).getApplication();
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
        if(this.tasks != null) {
            for(AsyncTask async : this.tasks) {
                async.cancel(true);
            }
        }
    }

    @Override
    public void onApplicationRestarted() {
    }

    @Override
    public void onRestartActivity() {
    }

    @Override
    public void onDestroyActivity() {
        if(tasks != null) {
            for(AsyncTask task : tasks) {
                if(task != null && !task.isCancelled()) {
                    task.cancel(true);
                }
            }
        }
    }

    @Override
    public void onResumeActivity() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    @Override
    public void onBackPressedActivity() {
    }

    /**
     * Método para iniciar a execução da AsyncTask. <br><i>Sobrescrever pelo menos o método doInBackground(Object[] params) </i>
     *
     * @param params parametros que serão passados para o método "doInBackGround"
     * @see android.os.AsyncTask
     */
    @SuppressWarnings("unchecked")
    public final void startExecutionInBackGround(Object[] params) {
        if(this.tasks == null) {
            this.tasks = new ArrayList<>();
        }
        this.tasks.add(new Async(this).execute(params));
    }

    @Override
    public void onPreExecute() {
    }

    @Override
    public Object doInBackground(Object[] params) {
        return null;
    }

    @Override
    public void onPostExecute(Object o) {
    }

    protected class Async extends AsyncTask {

        BasePresenter basePresenter;

        public Async(BasePresenter basePresenter) {
            this.basePresenter = basePresenter;
        }

        @Override
        protected void onPreExecute() {
            this.basePresenter.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            return this.basePresenter.doInBackground(params);
        }

        @Override
        protected void onPostExecute(Object o) {
            if(!isCancelled()) {
                this.basePresenter.onPostExecute(o);
            }
        }
    }

}