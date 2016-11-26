package com.github.energion.energionandroid;

public interface DataObservable{
  void subscribe(DataObserver observer);
  void unsubscribe(DataObserver observer);
}