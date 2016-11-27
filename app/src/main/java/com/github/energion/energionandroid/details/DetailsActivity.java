package com.github.energion.energionandroid.details;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.energion.energionandroid.R;

public class DetailsActivity extends AppCompatActivity {
  public static final String EXPENSIVE = "expensive";
  public static final String MEDIUM = "medium";
  public static final String CHEAP = "cheap";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_details);

    String[] cheap = getIntent().getStringExtra(CHEAP).split(",");
    String[] medium = getIntent().getStringExtra(MEDIUM).split(",");
    String[] expensive = getIntent().getStringExtra(EXPENSIVE).split(",");

    TextView cheapPrice = (TextView) findViewById(R.id.detail_cheapest_price);
    TextView cheapTime = (TextView) findViewById(R.id.detail_cheapest_time);

    TextView mediumPrice = (TextView) findViewById(R.id.detail_medium_price);
    TextView mediumTime = (TextView) findViewById(R.id.detail_medium_time);

    TextView expensivePrice = (TextView) findViewById(R.id.detail_expensive_price);
    TextView expensiveTime = (TextView) findViewById(R.id.detail_expensive_time);

    cheapTime.setText(getHourRange(Integer.parseInt(cheap[0])));
    cheapPrice.setText(cheap[1] + "€/\nMWh");

    mediumTime.setText(getHourRange(Integer.parseInt(medium[0])));
    mediumPrice.setText(medium[1] + "€/\nMWh");

    expensiveTime.setText(getHourRange(Integer.parseInt(expensive[0])));
    expensivePrice.setText(expensive[1] + "€/\nMWh");
  }

  private String getHourRange(int hour){
    int endHour = hour + 1;

    return getCorrectHourFormat(hour) + ":00-" + getCorrectHourFormat(endHour) + ":00";
  }

  private String getCorrectHourFormat(int hour){
    if(hour < 10) {
      return "0" + hour;
    } else {
      return hour + "";
    }
  }
}
