package com.vn.android.gpslogger.models;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class GPSViewModel extends AndroidViewModel {
  private final MutableLiveData<Location> location = new MutableLiveData<>();

  public GPSViewModel(@NonNull Application application) {
    super(application);
  }

  public void updateLocation(Location location) {
    this.location.setValue(location);
  }

  public MutableLiveData<Location> retrieveLocation() {
    return location;
  }
}
