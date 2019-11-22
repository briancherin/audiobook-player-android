package com.corson.audiobookplayer.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

import java.util.UUID;

public class DeviceInformationManager implements IDeviceInformationManager {

    private Context context;
    final String PREF_DEVICE_UUID = "deviceUUID";

    public DeviceInformationManager(Context context) {
        this.context = context;
    }

    @Override
    public boolean deviceIsOffline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)(context).getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        if (network != null) {
            NetworkCapabilities nc = connectivityManager.getNetworkCapabilities(network);
            return !(nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
        }
        return true;
    }

    @Override
    public String getDeviceId() {

        String uniqueId;

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_DEVICE_UUID,
                Context.MODE_PRIVATE);

        uniqueId = sharedPreferences.getString(PREF_DEVICE_UUID, null);

        if (uniqueId == null) {
            uniqueId = UUID.randomUUID().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(PREF_DEVICE_UUID, uniqueId);
            editor.commit();
        }

        return uniqueId;
    }
}
