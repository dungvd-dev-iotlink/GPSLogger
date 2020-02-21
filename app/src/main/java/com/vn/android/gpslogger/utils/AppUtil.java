package com.vn.android.gpslogger.utils;

import com.vn.android.gpslogger.models.Track;

public class AppUtil {
  public static final String JSON_POSTFIX = "gpx";

  public static String generateName(Track track) {
    return track.getName() + " - " + track.getDate() + "." + JSON_POSTFIX;
  }
}
