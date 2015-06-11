package br.com.frametcc.control.api;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionCheckerHelper {

    private final NetworkInfo infoWifi;
    private final NetworkInfo info3G;
    private final LocationManager manager;
    private final PackageManager packageManager;

    public ConnectionCheckerHelper(Context context) {
        infoWifi = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        info3G = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        packageManager = context.getPackageManager();
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

    public boolean hasGPS() {
        return packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
    }

    public boolean isGPSOn() {
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}
