package com.github.energion.energionandroid;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.energion.energionandroid.manual.ManualFragment;
import com.github.energion.energionandroid.recommendation.RecommendationFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
  private Context context;
  private DataObservable observable;

  public SectionsPagerAdapter(Context context, DataObservable observable, FragmentManager fm) {
    super(fm);
    this.context = context;
    this.observable = observable;
  }

  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case 0:
        return RecommendationFragment.newInstance(observable);

      case 1:
        return ManualFragment.newInstance(observable);

      default:
        return RecommendationFragment.newInstance(observable);
    }
  }

  @Override
  public int getCount() {
    return 2;
  }

  @Override
  public CharSequence getPageTitle(int position) {
    switch (position) {
      case 0:
        return context.getString(R.string.recommendation_title);
      case 1:
        return context.getString(R.string.manual_title);
    }

    return "";
  }
}
