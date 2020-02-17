package com.vn.android.gpslogger.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TrackBaseHelper extends SQLiteOpenHelper {
  private final static int VERSION = 1;
  private final static String DATABASE_NAME = "trackBase.db";


  public TrackBaseHelper(@Nullable Context context) {
    super(context, DATABASE_NAME, null, VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(
        "create table " + TrackDbSchema.TrackTable.NAME + "(" +
            TrackDbSchema.TrackTable.Cols.ID + " text primary key, " +
            TrackDbSchema.TrackTable.Cols.NAME + ", " +
            TrackDbSchema.TrackTable.Cols.DATE +
            ")"
    );

    db.execSQL(
        "create table " + TrackDbSchema.TrackDetailTable.NAME + "(" +
            "_id integer primary key autoincrement, " +
            TrackDbSchema.TrackDetailTable.Cols.LAT + ", " +
            TrackDbSchema.TrackDetailTable.Cols.LNG + ", " +
            TrackDbSchema.TrackDetailTable.Cols.TIMESTAMP + ", " +
            TrackDbSchema.TrackDetailTable.Cols.SPEED + ", " +
            TrackDbSchema.TrackDetailTable.Cols.COURSE + ", " +
            TrackDbSchema.TrackDetailTable.Cols.ACCURACY + ", " +
            TrackDbSchema.TrackDetailTable.Cols.ALTITUDE + ", " +
            TrackDbSchema.TrackDetailTable.Cols.ID + " text not null constraint " +
            TrackDbSchema.TrackDetailTable.Cols.ID + " references " + TrackDbSchema.TrackTable.NAME + "(" +
            TrackDbSchema.TrackTable.Cols.ID + ") on delete cascade" +
            ")"
    );
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }
}
