package br.com.frametcc;

import android.util.Log;

/**
 * Criado por Renan Arceno em 19/06/2015 - 14:51.
 */
public class TCCLog {

    private static TCCLog instance;
    private final String LOG_TAG = "Framework TCC";
    private boolean showLog = true;

    protected synchronized static TCCLog getInstance() {
        if (instance == null)
            instance = new TCCLog();
        return instance;
    }

    private TCCLog() {
    }

    public void setLogEnable(boolean show) {
        this.showLog = show;
    }

    public void w(String log) {
        if (showLog)
            Log.w(LOG_TAG, log);
    }

}