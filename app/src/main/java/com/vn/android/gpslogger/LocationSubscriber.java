package com.vn.android.gpslogger;

import android.location.Location;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.OnLifecycleEvent;

import com.vn.android.gpslogger.location.LocationListener;
import com.vn.android.gpslogger.models.GPSViewModel;

public class LocationSubscriber implements LifecycleObserver {
  private final LifecycleOwner lifecycleOwner;
  private final GPSViewModel gpsViewModel;
  private final LocationListener locationListener;

  public LocationSubscriber(final LifecycleOwner owner, final GPSViewModel model, final LocationListener locationListener) {
    lifecycleOwner = owner;
    lifecycleOwner.getLifecycle().addObserver(this);
    gpsViewModel = model;
    this.locationListener = locationListener;
  }

  public void subscribe() {
    gpsViewModel.retrieveLocation().observe(lifecycleOwner, new Observer<Location>() {
      @Override
      public void onChanged(@Nullable Location location) {
        if (location != null) {
          locationListener.onUpdatedLocation(location);
        }
      }
    });
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  void unsubscribe() {
    gpsViewModel.retrieveLocation().removeObservers(lifecycleOwner);
  }
}
