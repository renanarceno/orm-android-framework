package br.com.frametcc.factory;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import br.com.frametcc.shared.AbstractBasePresenter;
import br.com.frametcc.shared.api.BasePresenter;
import br.com.frametcc.shared.api.BaseView;

public class ViewPresenterFactory2 {

    public Map<Class<? extends BaseView>, Class<? extends BasePresenter>> viewPresenterImpl;
    public Map<Class<? extends BaseView>, Class<? extends BaseView>> views;
    public Map<Class<? extends BasePresenter>, Class<? extends AbstractBasePresenter>> presenters;

    private HashMap<Class<? extends BasePresenter>, BasePresenter> presenterInstances;

    public ViewPresenterFactory2() {
        this.presenterInstances = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public void createPresenterInstance(BaseView viewImpl) {
        try {
            ActivityInfo info = ((Activity) viewImpl).getPackageManager().getActivityInfo(((Activity) viewImpl).getComponentName(), PackageManager.GET_ACTIVITIES | PackageManager.GET_META_DATA);
            Bundle metaData = info.metaData;
            String presenterInterface = metaData.getString(BasePresenter.PRESENTER_INTERFACE);
            String presenterImpl = metaData.getString(BasePresenter.PRESENTER_IMPL);


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Class<? extends BasePresenter> controlImpl = this.viewPresenterImpl.get(viewImpl.getClass());
        try {
            BasePresenter basePresenter = controlImpl.newInstance();
            viewImpl.setPresenter(basePresenter);
            basePresenter.setView(viewImpl);
            basePresenter.init();
            this.presenterInstances.put(controlImpl, basePresenter);
        } catch (NullPointerException npe) {
            throw new RuntimeException(viewImpl.getClass().getSimpleName() + " não possui um presenter mapeado.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    @SuppressWarnings("all")
    public <C extends BasePresenter<?>, CI extends C> CI getControl(Class<CI> interfaceClass) {
        Class<?> aClass = this.presenters.get(interfaceClass);
        return (CI) this.presenterInstances.get(aClass);
    }

    @SuppressWarnings("unchecked")
    public <V extends BaseView<C>, C extends BasePresenter<V>> Class<V> getViewImpl(Class<? extends BaseView> interfaceClass) {
        return (Class<V>) this.views.get(interfaceClass);
    }

    public void removeControl(Class<? extends BaseView> baseView) {
        Class<? extends BasePresenter> aClass = this.viewPresenterImpl.get(baseView);
        if (aClass != null) {
            this.presenterInstances.remove(aClass);
        }
    }
}