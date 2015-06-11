package br.com.frametcc.shared.api;

import android.os.Bundle;

import com.github.mrengineer13.snackbar.SnackBar;

import br.com.frametcc.view.utils.ActivityNavigator;

public interface BaseView<CONTROL extends BasePresenter> {

    public void onApplicationRestarted();

    ActivityNavigator getNavigator();

    public void setPresenter(CONTROL controller);

    public void onBackPressed();

    public void destroy();

    void showSnackBar(String msg);

    void showSnackBar(String message, String actionMsg, SnackBar.OnMessageClickListener listener);

    void showToast(String msg);

    Bundle getExtras();

}