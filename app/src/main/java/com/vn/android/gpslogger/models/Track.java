package com.vn.android.gpslogger.models;

import android.util.JsonWriter;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Track {
  private UUID id;
  private String name;
  private String date;
  private List<Point> pointList;

  public Track() {
    name = "";
    id = UUID.randomUUID();
    date = new Date().toString();
    pointList = new ArrayList<>();
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Point> getPointList() {
    return pointList;
  }

  public void setPointList(List<Point> pointList) {
    this.pointList = pointList;
  }

  public void addPoint(Point point) {
    this.pointList.add(point);
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String toJSon() {
    StringWriter stringWriter = new StringWriter();
    try {
      JsonWriter jsonWriter = new JsonWriter(stringWriter);
      jsonWriter.beginObject();
      jsonWriter.name("name").value(name);
      jsonWriter.name("data").beginArray();
      for (Point point: pointList) {
        jsonWriter.beginObject();
        jsonWriter.name("lat").value(point.getLat());
        jsonWriter.name("lng").value(point.getLng());
        jsonWriter.name("timestamp").value(point.getTimestamp());
        jsonWriter.name("speed").value(point.getSpeed());
        jsonWriter.name("course").value(point.getCourse());
        jsonWriter.name("accuracy").value(point.getAccuracy());
        jsonWriter.name("altitude").value(point.getAltitude());
        jsonWriter.endObject();
      }
      jsonWriter.endArray();
      jsonWriter.endObject();
    }
    catch (Exception exception) {
      exception.printStackTrace();
      return "";
    }
    return stringWriter.toString();
  }
}
