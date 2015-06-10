package br.com.frametcc.control.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionCheckerHelper {

    private static ConnectionCheckerHelper singleton;

    private NetworkInfo infoWifi;
    private NetworkInfo info3G;

    public ConnectionCheckerHelper(Context context) {
        infoWifi = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        info3G = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
    }

    public boolean isInternetConnected() {
        return this.isWifiConnected() || this.is3GConnected();
    }

    public boolean is3GConnected() {
        return info3G != null && info3G.isAvailable() && info3G.isConnected();
    }

    public boolean isWifiConnected() {
        return infoWifi != null && infoWifi.isAvailable() && infoWifi.isConnected();
    }

}
