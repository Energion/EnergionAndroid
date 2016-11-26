package com.github.energion.energionandroid.recommendation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.energion.energionandroid.DataObservable;
import com.github.energion.energionandroid.DataObserver;
import com.github.energion.energionandroid.R;
import com.github.energion.energionandroid.model.Day;

import java.util.List;

public class RecommendationFragment extends Fragment implements DataObserver {
  private ProgressBar progressBar;
  private DataObservable observable;

  public static RecommendationFragment newInstance(DataObservable observable) {
    RecommendationFragment fragment = new RecommendationFragment();
    fragment.setObservable(observable);

    observable.subscribe(fragment);

    return fragment;
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

    RecommendationService service = new RecommendationService(dayList);

    List<Float> bestNight = service.getBestNightPrice();
    List<Float> bestMorning = service.getBestMorningPrice();
    List<Float> bestDay = service.getBestDayPrice();
    List<Float> bestEvening = service.getBestEveningPrice();

    if (bestNight.size() == bestMorning.size()
        && bestMorning.size() == bestDay.size()
        && bestDay.size() == bestEvening.size()
        && bestEvening.size() == dayList.size()) {

      for (int i = 0; i < bestNight.size(); i++) {
        Log.d("TAG", "=========== best Night of " + dayList.get(i).getDate() + " is " + bestNight.get(i));
        Log.d("TAG", "=========== best Morning of " + dayList.get(i).getDate() + " is " + bestMorning.get(i));
        Log.d("TAG", "=========== best Day of " + dayList.get(i).getDate() + " is " + bestDay.get(i));
        Log.d("TAG", "=========== best Evening of " + dayList.get(i).getDate() + " is " + bestEvening.get(i));
      }
    }

    /*for (Day day : dayList) {
      Log.d("apiResponse", "date: " + day.getDate());

      for (Hour hour : day.getHours()) {
        Log.d("apiResponse", "hour: " + hour.getHour() + " and price is " + hour.getPrice());
      }
    }*/
  }

  private void setObservable(DataObservable observable) {
    this.observable = observable;
  }

  @Override
  public void onDestroy() {
    if (observable != null) {
      observable.unsubscribe(this);
    }

    super.onDestroy();
  }
}
