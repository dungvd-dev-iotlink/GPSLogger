package com.vn.android.gpslogger.services;

import android.os.Binder;

public class GPSService {
  public class LocalBinder extends Binder {
    public GPSService getServiceInstance() {
      return GPSService.this;
    }
  }
}
