package com.vn.android.gpslogger.database;

public class TrackDbSchema {
  public static final class TrackTable {
    public static final String NAME = "trackTable";
    public static final class Cols {
      public static final String ID = "id";
      public static final String NAME = "name";
    }
  }

  public static final class TrackDetailTable {
    public static final String NAME = "trackDetailTable";
    public static final class Cols {
      public static final String LAT = "lat";
      public static final String LNG = "lng";
      public static final String TIMESTAMP = "timestamp";
      public static final String SPEED = "speed";
      public static final String COURSE = "course";
      public static final String ACCURACY = "accuracy";
      public static final String ALTITUDE = "altitude";
      public static final String ID = "detailId";
    }
  }
}
