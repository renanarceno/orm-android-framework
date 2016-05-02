package br.com.frametcc;

import android.util.Log;

public class TCCLog {

    private static TCCLog instance;
    private final String LOG_TAG = "Framework TCC";
    private boolean showLog = true;

    private TCCLog() {
    }

    protected synchronized static TCCLog getInstance() {
        if (instance == null)
            instance = new TCCLog();
        return instance;
    }

    public void setLogEnable(boolean show) {
        this.showLog = show;
    }

    public void w(String log) {
        if (showLog)
            Log.w(LOG_TAG, log);
    }

}