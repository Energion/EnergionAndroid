package com.github.energion.energionandroid.model;

import java.util.Date;
import java.util.List;

public class Day {
  Date date;
  List<Hour> hours;

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public List<Hour> getHours() {
    return hours;
  }

  public void setHours(List<Hour> hours) {
    this.hours = hours;
  }
}
