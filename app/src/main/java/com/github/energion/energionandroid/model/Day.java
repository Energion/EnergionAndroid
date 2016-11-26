package com.github.energion.energionandroid.model;

import java.util.List;

public class Day {
  String date;
  List<Hour> hours;

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public List<Hour> getHours() {
    return hours;
  }

  public void setHours(List<Hour> hours) {
    this.hours = hours;
  }
}
