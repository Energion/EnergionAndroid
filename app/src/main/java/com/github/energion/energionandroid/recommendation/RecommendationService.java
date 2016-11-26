package com.github.energion.energionandroid.recommendation;

import com.github.energion.energionandroid.model.Day;

import java.util.ArrayList;
import java.util.List;

public class RecommendationService {
  private List<Day> list;

  public RecommendationService(List<Day> list) {
    this.list = list;
  }

  private List<Float> getBestPriceByTime(int start, int end) {
    List<Float> bestPrice = new ArrayList<>();

    //Calendar calendar = Calendar.getInstance();
    //int hour = calendar.get(Calendar.HOUR_OF_DAY);

    for (Day day : list) {
      float minPrice = day.getHours().get(start).getPrice();

      for(int i = start; i < end; i++){
        if(day.getHours().get(i).getPrice() < minPrice){
          minPrice = day.getHours().get(i).getPrice();
        }
      }

      bestPrice.add(minPrice);
    }

    return bestPrice;
  }

  public List<Float> getBestNightPrice() {
    return getBestPriceByTime(0, 6);
  }

  public List<Float> getBestMorningPrice() {
    return getBestPriceByTime(6, 12);
  }

  public List<Float> getBestDayPrice() {
    return getBestPriceByTime(12, 18);
  }

  public List<Float> getBestEveningPrice() {
    return getBestPriceByTime(18, 24);
  }
}