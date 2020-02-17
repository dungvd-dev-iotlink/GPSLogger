package com.vn.android.gpslogger.models;

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
}
