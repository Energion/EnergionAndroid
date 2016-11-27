package com.github.energion.energionandroid;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class CirclesView extends FrameLayout {
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
    inflate(getContext(), R.layout.view_circles, this);

    TextView timeView = (TextView) findViewById(R.id.view_circles_time);
    TextView priceView = (TextView) findViewById(R.id.view_circles_price);
    TextView dateView = (TextView) findViewById(R.id.view_circles_date);
    ImageView lastCircleView = (ImageView) findViewById(R.id.view_circles_last_circle);

    timeView.setText(time);
    priceView.setText(price);
    dateView.setText(date);

    lastCircleView.setVisibility(lastItem ? VISIBLE : GONE);
  }
}
