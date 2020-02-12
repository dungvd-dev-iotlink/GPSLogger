
package com.vn.android.gpslogger;


import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import com.vn.android.gpslogger.services.GPSService;

public class GPSApplication extends Application implements GpsStatus.Listener, LocationListener {

    // Singleton instance
    private static GPSApplication singleton;

    // ------------------------------------------------------------------------------------ Service
    Intent gpsServiceIntent;
    GPSService gpsLoggerService;
    boolean isGPSServiceBound = false;

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    public static GPSApplication getInstance(){
        return singleton;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onGpsStatusChanged(int i) {

    }

    private ServiceConnection GPSServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            GPSService.LocalBinder binder = (GPSService.LocalBinder) service;
            gpsLoggerService = binder.getServiceInstance();
            isGPSServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isGPSServiceBound = false;
        }
    };

    private void startAndBindGPSService() {
        gpsServiceIntent = new Intent(GPSApplication.this, GPSService.class);
        //Start the service
        startService(gpsServiceIntent);
        //Bind to the service
        if (Build.VERSION.SDK_INT >= 14)
            bindService(gpsServiceIntent, GPSServiceConnection, Context.BIND_AUTO_CREATE | Context.BIND_IMPORTANT);
        else
            bindService(gpsServiceIntent, GPSServiceConnection, Context.BIND_AUTO_CREATE);
    }
}
