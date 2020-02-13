
package com.vn.android.gpslogger;


import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.lifecycle.ViewModelProviders;

import com.vn.android.gpslogger.activities.GPSActivity;
import com.vn.android.gpslogger.location.LocationListener;
import com.vn.android.gpslogger.models.GPSViewModel;
import com.vn.android.gpslogger.services.GPSService;

public class GPSApplication extends Application implements LocationListener {

    private static final String TAG = GPSApplication.class.getSimpleName();
    private static final String PREFS_NOBACKUP = "prefs_nobackup";
    public static final String FLAG_RECORDING   = "flagRecording";  // The persistent Flag is set when the app is recording, in order to detect Background Crashes

    // Singleton instance
    private static GPSApplication singleton;

    private int gpsActivity_activeTab = 0;                  // The active tab on GPSActivity
    private boolean locationPermissionChecked = false;      // If the flag is false the GPSActivity will check for Location Permission
    private boolean recording = false;
    private Location location;
    private GPSViewModel gpsViewModel;
    private GPSActivity gpsActivity;

    // ---------------------------- Service ----------------------------
    Intent gpsServiceIntent;
    GPSService gpsService;
    boolean isGPSServiceBound = false;

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    public static GPSApplication getInstance() {
        return singleton;
    }

    private ServiceConnection gpsServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            GPSService.LocalBinder binder = (GPSService.LocalBinder) service;
            gpsService = binder.getServiceInstance();
            gpsService.setLocationListener(GPSApplication.this);
            isGPSServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isGPSServiceBound = false;
        }
    };

    @Override
    public void onTerminate() {
        stopAndUnbindGPSService();
        super.onTerminate();
    }

    @Override
    public void onUpdatedLocation(Location location) {
        gpsViewModel.updateLocation(location);
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void onResume() {
        startAndBindGPSService();
        gpsViewModel = ViewModelProviders.of(getGpsActivity()).get(GPSViewModel.class);
    }

    public void onDestroy() {
        stopAndUnbindGPSService();
    }

    public GPSActivity getGpsActivity() {
        return gpsActivity;
    }

    public void setGpsActivity(GPSActivity gpsActivity) {
        this.gpsActivity = gpsActivity;
    }

    public void startAndBindGPSService() {
        gpsServiceIntent = new Intent(GPSApplication.this, GPSService.class);
        //Start the service
        startService(gpsServiceIntent);
        //Bind to the service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            bindService(gpsServiceIntent, gpsServiceConnection, Context.BIND_AUTO_CREATE | Context.BIND_IMPORTANT);
        }
        else {
            bindService(gpsServiceIntent, gpsServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private void stopAndUnbindGPSService() {
        try {
            unbindService(gpsServiceConnection); // Unbind to the service
        } catch (Exception e) {
            Log.e(TAG, "Unable to unbind the GPSService");
        }

        try {
            stopService(gpsServiceIntent); //Stop the service
        } catch (Exception e) {
            Log.e(TAG, "Unable to stop GPSService");
        }
    }

    public int getGpsActivity_activeTab() {
        return gpsActivity_activeTab;
    }

    public void setGpsActivity_activeTab(int gpsActivity_activeTab) {
        this.gpsActivity_activeTab = gpsActivity_activeTab;
    }

    public boolean isLocationPermissionChecked() {
        return locationPermissionChecked;
    }

    public void setLocationPermissionChecked(boolean locationPermissionChecked) {
        this.locationPermissionChecked = locationPermissionChecked;
    }

    // Flags are Boolean SharedPreferences that are excluded by automatic Backups

    public void flagAdd(String flag) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NOBACKUP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(flag, true);
        editor.commit();
    }


    public void flagRemove(String flag) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NOBACKUP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(flag);
        editor.commit();
    }


    public boolean flagExists(String flag) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NOBACKUP, Context.MODE_PRIVATE);
        return preferences.getBoolean(flag, false);
    }

    public boolean getRecording() {
        return recording;
    }

    public void setRecording(boolean recordingState) {
        recording = recordingState;
        if (recording) flagAdd(FLAG_RECORDING);
        else flagRemove(FLAG_RECORDING);
    }
}
