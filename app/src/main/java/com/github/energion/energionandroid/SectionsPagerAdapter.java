package com.github.energion.energionandroid;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.energion.energionandroid.manual.ManualFragment;
import com.github.energion.energionandroid.recommendation.RecommendationFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
  Context context;

  public SectionsPagerAdapter(Context context, FragmentManager fm) {
    super(fm);
    this.context = context;
  }

  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case 0:
        return RecommendationFragment.newInstance();

      case 1:
        return ManualFragment.newInstance("", "");

      default:
        return RecommendationFragment.newInstance();
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
