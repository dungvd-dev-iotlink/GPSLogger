package com.vn.android.gpslogger.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.vn.android.gpslogger.R;
import com.vn.android.gpslogger.activities.GPSActivity;
import com.vn.android.gpslogger.location.DeviceLocationProvider;
import com.vn.android.gpslogger.location.LocationListener;
import com.vn.android.gpslogger.location.LocationProvider;

public class GPSService extends Service {
  private static final String CHANNEL_ID = "GPSLoggerServiceChannel";

  private NotificationManager notificationManager;
  private LocationProvider locationProvider;
  private LocationListener locationListener;

  // IBinder
  private final IBinder mBinder = new LocalBinder();

  public void setLocationListener(LocationListener locationListener) {
    this.locationListener = locationListener;
  }

  private void updateLocation(Location location) {
    if (locationListener != null) {
      locationListener.onUpdatedLocation(location);
    }
  }

  @Override
  public void onCreate() {
    super.onCreate();
    notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    locationProvider = new DeviceLocationProvider(getApplicationContext());
    LocationListener locationListener = new LocationListener() {
      @Override
      public void onUpdatedLocation(Location location) {
        updateLocation(location);
      }
    };
    locationProvider.setLocationListener(locationListener);
    locationProvider.startUpdatingLocation();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    startForeground(1, getNotification());
    return START_NOT_STICKY;
  }

  @Override
  public void onDestroy() {
    locationProvider.onDestroy();
    super.onDestroy();
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return mBinder;
  }

  public class LocalBinder extends Binder {
    public GPSService getServiceInstance() {
      return GPSService.this;
    }
  }

  private Notification getNotification() {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel notificationChannel = new NotificationChannel(
          CHANNEL_ID, getString(R.string.channel_name),
          NotificationManager.IMPORTANCE_DEFAULT);
      notificationManager.createNotificationChannel(notificationChannel);
    }

    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
    builder.setSmallIcon(R.mipmap.ic_launcher)
        .setColor(getResources().getColor(R.color.colorPrimaryLight))
        .setContentTitle(getString(R.string.app_name))
        .setShowWhen(false)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setCategory(NotificationCompat.CATEGORY_SERVICE)
        .setOngoing(true)
        .setContentText(getString(R.string.notification_contentText));

    final Intent startIntent = new Intent(getApplicationContext(), GPSActivity.class);
    startIntent.setAction(Intent.ACTION_MAIN);
    startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    startIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 1, startIntent, 0);
    builder.setContentIntent(contentIntent);
    return builder.build();
  }
}
