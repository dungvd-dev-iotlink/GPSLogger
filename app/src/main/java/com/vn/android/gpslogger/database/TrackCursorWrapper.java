package com.vn.android.gpslogger.database;

import android.database.Cursor;
import android.database.CursorWrapper;

public class TrackCursorWrapper extends CursorWrapper {
  /**
   * Creates a cursor wrapper.
   *
   * @param cursor The underlying cursor to wrap.
   */
  public TrackCursorWrapper(Cursor cursor) {
    super(cursor);
  }

  public String getTrackId() {
    return getString(getColumnIndex(TrackDbSchema.TrackTable.Cols.ID));
  }

  public String getTrackName() {
    return getString(getColumnIndex(TrackDbSchema.TrackTable.Cols.NAME));
  }

  public String getDate() {
    return getString(getColumnIndex(TrackDbSchema.TrackTable.Cols.DATE));
  }

  public boolean isUploaded() {
    return getInt(getColumnIndex(TrackDbSchema.TrackTable.Cols.UPLOADED)) == 1;
  }
}
