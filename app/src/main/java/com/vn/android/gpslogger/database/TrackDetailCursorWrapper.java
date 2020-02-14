package com.vn.android.gpslogger.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.vn.android.gpslogger.models.Point;

public class TrackDetailCursorWrapper extends CursorWrapper {
  /**
   * Creates a cursor wrapper.
   *
   * @param cursor The underlying cursor to wrap.
   */
  public TrackDetailCursorWrapper(Cursor cursor) {
    super(cursor);
  }

  public Point getPoint() {
    double lat = getDouble(getColumnIndex(TrackDbSchema.TrackDetailTable.Cols.LAT));
    double lng = getDouble(getColumnIndex(TrackDbSchema.TrackDetailTable.Cols.LNG));
    long timestamp = getLong(getColumnIndex(TrackDbSchema.TrackDetailTable.Cols.TIMESTAMP));
    double speed = getDouble(getColumnIndex(TrackDbSchema.TrackDetailTable.Cols.SPEED));
    float course = getFloat(getColumnIndex(TrackDbSchema.TrackDetailTable.Cols.COURSE));
    float accuracy = getFloat(getColumnIndex(TrackDbSchema.TrackDetailTable.Cols.ACCURACY));
    double altitude = getDouble(getColumnIndex(TrackDbSchema.TrackDetailTable.Cols.ALTITUDE));

    Point point = new Point(lat, lng, timestamp, speed, course, accuracy, altitude);
    return point;
  }
}
