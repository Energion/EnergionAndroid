package com.github.energion.energionandroid.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;

public class CirclesView extends FrameLayout {
  Day day;
  int startHour;
  List<Hour> hourList;
  private ImageView lastCircleView;
  private ImageView firstCircleView;
  private ImageView smallCircleView1;
  private ImageView smallCircleView2;
  private ImageView smallCircleView3;
  private TextView timeView;
  private TextView priceView;

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

    timeView = (TextView) findViewById(R.id.view_circles_time);
    priceView = (TextView) findViewById(R.id.view_circles_price);
    TextView dateView = (TextView) findViewById(R.id.view_circles_date);
    TextView endTime = (TextView) findViewById(R.id.view_circles_end_time);
    lastCircleView = (ImageView) findViewById(R.id.view_circles_last_circle);
    firstCircleView = (ImageView) findViewById(R.id.view_circles_circle_big);
    smallCircleView1 = (ImageView) findViewById(R.id.view_circles_circle_small_1);
    smallCircleView2 = (ImageView) findViewById(R.id.view_circles_circle_small_2);
    smallCircleView3 = (ImageView) findViewById(R.id.view_circles_circle_small_3);

    timeView.setText(time);
    priceView.setText(price);
    dateView.setText(date);

    lastCircleView.setVisibility(lastItem ? VISIBLE : GONE);
    endTime.setVisibility(lastItem ? VISIBLE : GONE);

    priceView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (isAfterDate()) {
          Intent intent = new Intent(getContext(), DetailsActivity.class);
          intent.putExtra(DetailsActivity.CHEAP, getDataAsString(0));
          intent.putExtra(DetailsActivity.MEDIUM, getDataAsString(1));
          intent.putExtra(DetailsActivity.EXPENSIVE, getDataAsString(2));
          getContext().startActivity(intent);
        }
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

  private String getDataAsString(int position) {
    return hourList.get(position).getHour() + "," + hourList.get(position).getPrice();
  }


  public CirclesView setDay(Day day) {
    this.day = day;

    return this;
  }

  public CirclesView setStartHour(int startHour) {
    this.startHour = startHour;

    populateHoursList();
    disablePassedViews();

    return this;
  }

  private void disablePassedViews() {
    if (!isAfterDate()) {
      firstCircleView.setImageResource(R.drawable.big_circle_disabled);
      smallCircleView1.setImageResource(R.drawable.small_circle_disabled);
      smallCircleView2.setImageResource(R.drawable.small_circle_disabled);
      smallCircleView3.setImageResource(R.drawable.small_circle_disabled);
      lastCircleView.setImageResource(R.drawable.big_circle_disabled);
      priceView.setBackgroundResource(R.drawable.button_background_disabled);
      priceView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorDisabled));
    }
  }

  private boolean isAfterDate() {
    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GTM+2"));
    Calendar dayDate = Calendar.getInstance();
    dayDate.setTime(day.getDate());
    dayDate.set(Calendar.HOUR_OF_DAY, startHour + 6);

    return dayDate.after(calendar);
  }
}
