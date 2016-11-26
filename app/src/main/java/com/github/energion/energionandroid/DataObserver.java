package com.github.energion.energionandroid;

import com.github.energion.energionandroid.model.Day;

import java.util.List;

public interface DataObserver {
  void update(List<Day> dayList);
}