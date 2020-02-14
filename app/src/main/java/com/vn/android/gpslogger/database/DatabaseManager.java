package com.vn.android.gpslogger.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vn.android.gpslogger.models.Point;
import com.vn.android.gpslogger.models.Track;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseManager {
  private static DatabaseManager instance;

  private Context context;
  private SQLiteDatabase database;

  public static DatabaseManager getInstance(Context context) {
    if (instance == null) {
      instance = new DatabaseManager(context);
    }
    return instance;
  }

  private DatabaseManager(Context context) {
    this.context = context;
    database = new TrackBaseHelper(context).getWritableDatabase();
  }

  public static ContentValues getPointContentValues(Point point, UUID id) {
    ContentValues values = new ContentValues();
    values.put(TrackDbSchema.TrackDetailTable.Cols.LAT, point.getLat());
    values.put(TrackDbSchema.TrackDetailTable.Cols.LNG, point.getLng());
    values.put(TrackDbSchema.TrackDetailTable.Cols.TIMESTAMP, point.getTimestamp());
    values.put(TrackDbSchema.TrackDetailTable.Cols.SPEED, point.getSpeed());
    values.put(TrackDbSchema.TrackDetailTable.Cols.COURSE, point.getCourse());
    values.put(TrackDbSchema.TrackDetailTable.Cols.ACCURACY, point.getAccuracy());
    values.put(TrackDbSchema.TrackDetailTable.Cols.ALTITUDE, point.getAltitude());
    values.put(TrackDbSchema.TrackDetailTable.Cols.ID, id.toString());
    return values;
  }

  public static ContentValues getTrackContentValues(Track track) {
    ContentValues values = new ContentValues();
    values.put(TrackDbSchema.TrackTable.Cols.ID, track.getId().toString());
    values.put(TrackDbSchema.TrackTable.Cols.NAME, track.getName());
    return values;
  }

  public void addTrack(Track track) {
    ContentValues trackContentValues = getTrackContentValues(track);
    database.insert(TrackDbSchema.TrackTable.NAME, null, trackContentValues);
    List<Point> points = track.getPointList();
    for (int i = 0; i < points.size(); ++i) {
      Point point = points.get(i);
      ContentValues pointContentValues = getPointContentValues(point, track.getId());
      database.insert(TrackDbSchema.TrackDetailTable.NAME, null, pointContentValues);
    }
  }

  public List<Point> getPoints(UUID id) {
    List<Point> points = new ArrayList<>();
    String[] whereArgs = {id.toString()};
    Cursor cursor = queryPoints(
        TrackDbSchema.TrackDetailTable.Cols.ID + " = ?",
        whereArgs);
    if (cursor != null) {
      cursor.moveToFirst();
      while (!cursor.isAfterLast()) {
        points.add(((TrackDetailCursorWrapper) cursor).getPoint());
        cursor.moveToNext();
      }
    }
    cursor.close();
    return points;
  }

  public List<Track> getAllTracks() {
    List<Track> tracks = new ArrayList<>();
    Cursor cursor = queryTrack(null, null);

    if (cursor != null) {
      if (cursor.getCount() == 0) {
        return null;
      }
      cursor.moveToFirst();
      while (!cursor.isAfterLast()) {
        String id = ((TrackCursorWrapper) cursor).getTrackId();
        String name = ((TrackCursorWrapper) cursor).getTrackName();
        Track track = new Track();
        track.setId(UUID.fromString(id));
        track.setName(name);
        track.setPointList(getPoints(UUID.fromString(id)));
        tracks.add(track);
        cursor.moveToNext();
      }
      cursor.close();
      return tracks;
    }
    else {
      return null;
    }
  }

  public Track getTrack(UUID id) {
    String[] whereArgs = {id.toString()};
    Cursor cursor = queryTrack(
        TrackDbSchema.TrackTable.Cols.ID + " = ?",
        whereArgs);

    if (cursor != null) {
      if (cursor.getCount() == 0) {
        return null;
      }
      cursor.moveToFirst();
      String name = ((TrackCursorWrapper) cursor).getTrackName();
      Track track = new Track();
      track.setId(id);
      track.setName(name);
      track.setPointList(getPoints(id));
      cursor.close();
      return track;
    }
    else {
      return null;
    }
  }

  private TrackCursorWrapper queryTrack(String whereClause, String[] whereArgs) {
    Cursor cursor = database.query(
        TrackDbSchema.TrackTable.NAME,
        null, // selects all columns
        whereClause,
        whereArgs, null, null, null
    );
    return new TrackCursorWrapper(cursor);
  }

  private TrackDetailCursorWrapper queryPoints(String whereClause, String[] whereArgs) {
    Cursor cursor = database.query(
        TrackDbSchema.TrackDetailTable.NAME,
        null, // selects all columns
        whereClause,
        whereArgs, null, null, null
    );
    return new TrackDetailCursorWrapper(cursor);
  }
}
