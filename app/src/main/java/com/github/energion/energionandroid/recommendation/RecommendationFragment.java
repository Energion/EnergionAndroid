package com.github.energion.energionandroid.recommendation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.energion.energionandroid.DataObserver;
import com.github.energion.energionandroid.R;
import com.github.energion.energionandroid.model.Day;
import com.github.energion.energionandroid.model.Hour;

import java.util.List;

public class RecommendationFragment extends Fragment implements DataObserver {
  public RecommendationFragment() {
  }

  public static RecommendationFragment newInstance(){
    return new RecommendationFragment();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_recommendation, container, false);
  }

  @Override
  public void update(List<Day> dayList) {
    for (Day day : dayList) {
      Log.d("apiResponse", "date: " + day.getDate());

      for (Hour hour : day.getHours()) {
        Log.d("apiResponse", "hour: " + hour.getHour() + " and price is " + hour.getPrice());
      }
    }
  }
}
