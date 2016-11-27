package com.github.energion.energionandroid.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.energion.energionandroid.R;
import com.github.energion.energionandroid.details.DetailsActivity;
import com.github.energion.energionandroid.model.Day;
import com.github.energion.energionandroid.model.Hour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CirclesView extends FrameLayout {
  Day day;
  int startHour;
  List<Hour> hourList;

  public CirclesView(Context context, String time, String price, boolean lastItem, String date) {
    super(context);

    init(time, price, lastItem, date);
  }

  public CirclesView(Context context, AttributeSet attrs, String time, String price) {
    super(context, attrs);

    init(time, price, false, "");
  }

  public CirclesView(Context context, AttributeSet attrs, int defStyleAttr, String time, String price) {
    super(context, attrs, defStyleAttr);

    init(time, price, false, "");
  }

  private void init(String time, String price, boolean lastItem, String date) {
    hourList = new ArrayList<>();

    inflate(getContext(), R.layout.view_circles, this);

    TextView timeView = (TextView) findViewById(R.id.view_circles_time);
    TextView priceView = (TextView) findViewById(R.id.view_circles_price);
    TextView dateView = (TextView) findViewById(R.id.view_circles_date);
    ImageView lastCircleView = (ImageView) findViewById(R.id.view_circles_last_circle);

    timeView.setText(time);
    priceView.setText(price);
    dateView.setText(date);

    lastCircleView.setVisibility(lastItem ? VISIBLE : GONE);

    priceView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra(DetailsActivity.CHEAP, getDataAsString(0));
        intent.putExtra(DetailsActivity.MEDIUM, getDataAsString(1));
        intent.putExtra(DetailsActivity.EXPENSIVE, getDataAsString(2));
        getContext().startActivity(intent);
      }
    });
  }

  private void populateHoursList() {
    if (day != null) {
      int endHour = startHour + 6;

      for (int i = startHour; i < endHour; i++) {
        hourList.add(day.getHours().get(i));
      }

      Collections.sort(hourList, new Comparator<Hour>() {
        @Override
        public int compare(Hour o1, Hour o2) {
          Float price1 = o1.getPrice();
          Float price2 = o2.getPrice();

          return price1.compareTo(price2);
        }
      });
    }
  }

  private String getDataAsString(int position){
    return hourList.get(position).getHour() + "," + hourList.get(position).getPrice();
  }


  public CirclesView setDay(Day day) {
    this.day = day;

    return this;
  }

  public CirclesView setStartHour(int startHour) {
    this.startHour = startHour;

    populateHoursList();

    return this;
  }
}
