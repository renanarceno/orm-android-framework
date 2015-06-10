package br.com.frametcc;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Map;

import br.com.frametcc.database.api.DBConfig;
import br.com.frametcc.database.DBHelper;
import br.com.frametcc.database.dao.DatabaseDAO;
import br.com.frametcc.factory.DAOFactory;
import br.com.frametcc.factory.ViewPresenterFactory;
import br.com.frametcc.shared.api.BasePresenter;
import br.com.frametcc.shared.api.BaseView;

public abstract class FrameTCCApplication extends Application {
    private static final String APP_TAG = "FRAME";

    private Activity lastActivity;
    private ViewPresenterFactory factory;
    private DAOFactory daoFactory;

    @Override
    @SuppressWarnings("all")
    public final void onCreate() {
        super.onCreate();
        this.create();
        this.factory = new ViewPresenterFactory();
        this.daoFactory = new DAOFactory(this.getApplicationContext());
        this.daoFactory.daos = getDaosIntImpl();
        this.factory.viewPresenterImpl = getControlViewMap();
        this.factory.presenters = getControlIntImpl();
        this.factory.views = getViewIntImpl();
        this.onAfterCreate();
    }

    public void setupView(BaseView view) {
        this.factory.createControlInstance(view);
    }

    public void setTopActivity(Activity activity) {
        this.lastActivity = activity;
    }

    public Activity getTopActivity() {
        return this.lastActivity;
    }

    /**
     * Sinaliza quando o aplicativo ficou em segundo plano (totalmente invisível ao usuário).
     */
    public void onActivityStopped(Activity activity) {
        if(this.lastActivity.equals(activity)) {
            Log.w(APP_TAG, "App stopped");
        }
    }

    /**
     * Sinaliza quando o aplicativo voltou a ser visível e o usuário consegue interagir com ele novamente.
     */
    public void onApplicationRestarted() {
        Log.w(APP_TAG, "App restarted");
    }

    /**
     * Chamado quando uma activity é destruída
     */
    public void onActivityDestroyed(Activity activity) {
        BaseView v = ((BaseView) activity);
        this.factory.removePresenter(v.getClass());
        Log.w(APP_TAG, "Activity " + activity.getClass().getSimpleName() + " destroyed");
    }

    public <T extends DatabaseDAO> T getDao(Class<T> clazz) {
        T dao = daoFactory.getDao(clazz);
        if(dao == null) {
            throw new RuntimeException("DAO não mapeado para uma implementação");
        }
        return dao;
    }

    @Nullable
    public <C extends BasePresenter<?>, CI extends C> CI getControl(Class<CI> control) {
        return this.factory.getPresenter(control);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public <V extends BaseView<C>, C extends BasePresenter<V>> Class<V> getViewImpl(Class<V> viewInterface) {
        return (Class<V>) this.factory.getViewImpl(viewInterface);
    }

    /**
     * Chamado ao criar a aplicação
     */
    public abstract void create();

    /**
     * Chamado assim que a aplicação inicializa as Factories
     */
    public void onAfterCreate() {

    }

    /**
     * Mapeamento da implementação do controle com sua implementação da visão
     */
    public abstract Map<Class<? extends BaseView>, Class<? extends BasePresenter>> getControlViewMap();

    /**
     * Mapeamento da interface do controle com sua implementação
     */
    public abstract Map<Class<? extends BasePresenter>, Class<?>> getControlIntImpl();

    /**
     * Mapeamento da interface da view com sua implementação
     */
    public abstract Map<Class<? extends BaseView>, Class<? extends BaseView>> getViewIntImpl();

    /**
     * Mapeamento da interface do DAO com sua implementação, que estende de DBHelper
     */
    public abstract Map<Class<? extends DatabaseDAO>, DBHelper> getDaosIntImpl();

    /**
     * Configurações sobre o banco de dados do aplicativo
     */
    public abstract DBConfig getDBConfig();
}