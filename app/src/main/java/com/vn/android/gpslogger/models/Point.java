package com.vn.android.gpslogger.models;

public class Point {
  private double lat;
  private double lng;
  private long timestamp;
  private double speed;
  private float course; //bearing
  private float accuracy;
  private double altitude;

  public Point(double lat, double lng, long timestamp, double speed, float course, float accuracy, double altitude) {
    this.lat = lat;
    this.lng = lng;
    this.timestamp = timestamp;
    this.speed = speed;
    this.course = course;
    this.accuracy = accuracy;
    this.altitude = altitude;
  }

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public double getLng() {
    return lng;
  }

  public void setLng(double lng) {
    this.lng = lng;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public double getSpeed() {
    return speed;
  }

  public void setSpeed(double speed) {
    this.speed = speed;
  }

  public float getCourse() {
    return course;
  }

  public void setCourse(float course) {
    this.course = course;
  }

  public float getAccuracy() {
    return accuracy;
  }

  public void setAccuracy(float accuracy) {
    this.accuracy = accuracy;
  }

  public double getAltitude() {
    return altitude;
  }

  public void setAltitude(double altitude) {
    this.altitude = altitude;
  }
}
