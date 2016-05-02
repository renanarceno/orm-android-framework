package br.com.frametcc.factory;

import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import br.com.frametcc.shared.AbstractBasePresenter;
import br.com.frametcc.shared.api.BasePresenter;
import br.com.frametcc.shared.api.BaseView;

public class ViewPresenterFactory {

    public Map<Class<? extends BaseView>, Class<? extends BasePresenter>> viewPresenterImpl;
    private HashMap<Class<? extends BasePresenter>, BasePresenter> presenterInstances;

    public ViewPresenterFactory() {
        this.presenterInstances = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public void createPresenterInstance(BaseView viewImpl) {
        Class<? extends BasePresenter> controlImpl = getPresenterClass(viewImpl);
        try {
            BasePresenter basePresenter = controlImpl.newInstance();
            viewImpl.setPresenter(basePresenter);
            basePresenter.setView(viewImpl);
            basePresenter.init();
            this.presenterInstances.put(controlImpl, basePresenter);
        } catch (NullPointerException npe) {
            throw new RuntimeException(viewImpl.getClass().getSimpleName() + " não possui um presenter mapeado.", npe);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Class<? extends BasePresenter> getPresenterClass(BaseView viewImpl) {
        Class<? extends BaseView> viewImplClass = viewImpl.getClass();

        for (Map.Entry<Class<? extends BaseView>, Class<? extends BasePresenter>> entry : this.viewPresenterImpl.entrySet()) {
            Class<? extends BaseView> key = entry.getKey();
            Class<? extends BasePresenter> value = entry.getValue();
            if (key.equals(viewImplClass) || key.isAssignableFrom(viewImplClass))
                return value;
        }
        throw new RuntimeException("Can't find presenter \"" + viewImpl.getClass().getSimpleName() + "\"");
    }

    @SuppressWarnings("all")
    private Class<? extends BasePresenter> getPresenterClassFrom(Class basePresenter) {
        for (Class<? extends BasePresenter> presenter : this.viewPresenterImpl.values()) {
            if (basePresenter.isAssignableFrom(presenter) || basePresenter.equals(presenter))
                return presenter;
        }
        throw new RuntimeException("Can't find presenter \"" + basePresenter.getClass().getSimpleName() + "\"");
    }

    @SuppressWarnings("all")
    private Class<? extends BaseView> getViewClassFrom(Class baseView) {
        for (Class<? extends BaseView> view : this.viewPresenterImpl.keySet()) {
            if (baseView.isAssignableFrom(view) || baseView.equals(view))
                return view;
        }
        throw new RuntimeException("Can't find view \"" + baseView.getClass().getSimpleName() + "\"");
    }

    @Nullable
    @SuppressWarnings("all")
    public <C extends BasePresenter<?>, CI extends C> CI getPresenter(Class<CI> interfaceClass) {
        Class<?> aClass = getPresenterClassFrom(interfaceClass);
        return (CI) this.presenterInstances.get(aClass);
    }

    @SuppressWarnings("unchecked")
    public <V extends BaseView<C>, C extends BasePresenter<V>> Class<V> getViewImpl(Class<? extends BaseView> interfaceClass) {
        return (Class<V>) getViewClassFrom(interfaceClass);
    }

    public void removePresenter(BaseView baseView) {
        Class<? extends BasePresenter> aClass = this.viewPresenterImpl.get(baseView.getClass());
        if (aClass != null) {
            baseView.destroy();
            this.presenterInstances.remove(aClass);
        }
    }
}