package com.github.energion.energionandroid.recommendation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.energion.energionandroid.R;

public class RecommendationFragment extends Fragment {
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

}
