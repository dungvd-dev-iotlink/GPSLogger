package com.vn.android.gpslogger.location;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.vn.android.gpslogger.R;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class DeviceLocationProvider extends LocationProvider {

  private static final String TAG = DeviceLocationProvider.class.getSimpleName();
  private final long TIME_REQUEST_UPDATE_LOCATION = 10L;
  private final float DISTANCE_REQUEST_UPDATE_LOCATION = 0f;
  private static final int TWO_MINUTES = 1000 * 60 * 2;

  private Location lastLocation;
  private Location location;

  private boolean isGPSEnabled;
  private boolean isNetworkEnabled;
  private LocationManager locationManager;
  private LocationListener locationListener;

  public DeviceLocationProvider(Context context) {
    super(context);
  }

  private void initialize() {
    checkNetworkProviderEnable(); // check if gps is available
    getLocation(); // get location using Android API
  }

  private void checkNetworkProviderEnable() {
    locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
  }

  private void getLocation() {
    // Acquire a reference to the system Location Manager
    locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    setLocationListener();
    captureLocation();
  }

  private void setLocationListener() {
    // Define a listener that responds to location updates
    locationListener = new LocationListener() {
      public void onLocationChanged(Location _location) {
        // Called when a new location is found by the network location provider.
        if (_location == null) {
          Log.d(TAG, "location == null");
        } else {
          lastLocation = location;
          location = getBetterLocation(_location, lastLocation);
          navigationLocationListener.onUpdatedLocation(location);
        }
      }

      public void onStatusChanged(String provider, int status, Bundle extras) {
      }

      public void onProviderEnabled(String provider) {
      }

      public void onProviderDisabled(String provider) {
      }
    };
  }

  private Location getBetterLocation(Location location, Location currentBestLocation) {
    if (currentBestLocation == null) {
      // A new location is always better than no location
      return location;
    }

    // Check whether the new location fix is newer or older
    long timeDelta = location.getTime() - currentBestLocation.getTime();
    boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
    boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
    boolean isNewer = timeDelta > 0;

    // If it's been more than two minutes since the current location, use the new location
    // because the user has likely moved
    if (isSignificantlyNewer) {
      return location;
      // If the new location is more than two minutes older, it must be worse
    } else if (isSignificantlyOlder) {
      return currentBestLocation;
    }

    // Check whether the new location fix is more or less accurate
    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
    boolean isLessAccurate = accuracyDelta > 0;
    boolean isMoreAccurate = accuracyDelta < 0;
    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

    // Check if the old and new location are from the same provider
    boolean isFromSameProvider = isSameProvider(location.getProvider(),
      currentBestLocation.getProvider());

    // Determine location quality using a combination of timeliness and accuracy
    if (isMoreAccurate) {
      return location;
    } else if (isNewer && !isLessAccurate) {
      return location;
    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
      return location;
    }
    return currentBestLocation;
  }

  /**
   * Checks whether two providers are the same
   */
  private boolean isSameProvider(String provider1, String provider2) {
    if (provider1 == null) {
      return provider2 == null;
    }
    return provider1.equals(provider2);
  }

  private void captureLocation() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
      && ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
      && ContextCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      Log.e(TAG, context.getResources().getString(R.string.location_allow_permission_msg));
      return;
    }
    try {
      if (isGPSEnabled) {
        locationManager.requestLocationUpdates(
          LocationManager.GPS_PROVIDER,
          TIME_REQUEST_UPDATE_LOCATION,
          DISTANCE_REQUEST_UPDATE_LOCATION,
          locationListener);
      }
      if (isNetworkEnabled) {
        locationManager.requestLocationUpdates(
          LocationManager.NETWORK_PROVIDER,
          TIME_REQUEST_UPDATE_LOCATION,
          DISTANCE_REQUEST_UPDATE_LOCATION,
          locationListener);
      }
    } catch (Exception e) {
      Log.e(TAG, e.getMessage());
    }
  }

  @Override
  public void startUpdatingLocation() {
    if (navigationLocationListener != null) {
      initialize();
    } else {
      Log.e(TAG, "navigationLocationListener == null. Please set navigationLocationListener first !");
    }
  }

  @Override
  public void onDestroy() {
    if (locationManager != null) {
      try {
        locationManager.removeUpdates(locationListener);
      } catch (Exception e) {
        Log.i(TAG, "Fail to remove location listeners, ignore. ", e);
      }
    }
  }
}
