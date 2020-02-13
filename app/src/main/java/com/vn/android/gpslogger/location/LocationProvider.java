package com.vn.android.gpslogger.location;

import android.content.Context;

public abstract class LocationProvider {

  protected LocationListener navigationLocationListener;

  protected Context context;

  public LocationProvider(Context context) {
    this.context = context;
  }

  public abstract void startUpdatingLocation();

  public abstract void onDestroy();

  public void setLocationListener(LocationListener navigationLocationListener) {
    this.navigationLocationListener = navigationLocationListener;
  }

}
