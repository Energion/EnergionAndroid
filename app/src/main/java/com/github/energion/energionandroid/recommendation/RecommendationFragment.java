package com.github.energion.energionandroid.recommendation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.energion.energionandroid.DataObserver;
import com.github.energion.energionandroid.R;
import com.github.energion.energionandroid.model.Day;
import com.github.energion.energionandroid.model.Hour;

import java.util.List;

public class RecommendationFragment extends Fragment implements DataObserver {
  ProgressBar progressBar;

  public static RecommendationFragment newInstance(){
    return new RecommendationFragment();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_recommendation, container, false);

    progressBar = (ProgressBar) view.findViewById(R.id.recommendation_progress);

    return view;
  }

  @Override
  public void update(List<Day> dayList) {
    progressBar.setVisibility(View.GONE);

    for (Day day : dayList) {
      Log.d("apiResponse", "date: " + day.getDate());

      for (Hour hour : day.getHours()) {
        Log.d("apiResponse", "hour: " + hour.getHour() + " and price is " + hour.getPrice());
      }
    }
  }
}
