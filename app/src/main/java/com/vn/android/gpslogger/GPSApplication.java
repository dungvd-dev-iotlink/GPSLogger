package com.vn.android.gpslogger;

import android.Manifest;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.content.PermissionChecker;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.vn.android.gpslogger.activities.GPSActivity;
import com.vn.android.gpslogger.database.DatabaseManager;
import com.vn.android.gpslogger.fragments.FragmentTrackList;
import com.vn.android.gpslogger.location.LocationListener;
import com.vn.android.gpslogger.models.GPSViewModel;
import com.vn.android.gpslogger.models.Point;
import com.vn.android.gpslogger.models.Track;
import com.vn.android.gpslogger.services.GPSService;

public class GPSApplication extends Application implements LocationListener {

    private static final String TAG = GPSApplication.class.getSimpleName();

    // Singleton instance
    private static GPSApplication singleton;

    private int gpsActivity_activeTab = 0;                  // The active tab on GPSActivity
    private boolean locationPermissionChecked = false;      // If the flag is false the GPSActivity will check for Location Permission
    private boolean recording = false;
    private Location location;
    private GPSViewModel gpsViewModel;
    private GPSActivity gpsActivity;
    private Track track;

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

    private final ServiceConnection gpsServiceConnection = new ServiceConnection() {

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
        if (location == null) {
            Log.e(TAG, "Error when receive NULL Location");
            return;
        }
        this.location = location;
        if (recording) {
            recordNewTrack(location);
        }
        gpsViewModel.updateLocation(location);
    }

    private void recordNewTrack(Location location) {
        Point point = new Point(location.getLatitude(),
            location.getLongitude(), location.getTime(),
            location.getSpeed(), location.getBearing(),
            location.getAccuracy(),
            location.getAltitude());
        track.addPoint(point);
    }

    public Location getLocation() {
        return location;
    }

    public void onResume() {
        if (PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED
            && PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED) {
            startAndBindGPSService();
        }
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

    private void startAndBindGPSService() {
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

    public boolean getRecording() {
        return recording;
    }

    public void setRecording(boolean recordingState) {
        recording = recordingState;
        if (recording) {
            track = new Track();
        }
        else {
            // TODO : Save track to local memory on other Thread
            gpsActivity.showDialogInputName();
        }
    }

    public int getTrackRecordingSize() {
        if (track == null) {
            return 0;
        }
        return track.getPointList().size();
    }

    public void saveTrack(String name) {
        track.setName(name);
        DatabaseManager.getInstance(getApplicationContext()).addTrack(track);
        track = null;
    }
}
