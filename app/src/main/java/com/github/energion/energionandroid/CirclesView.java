package com.github.energion.energionandroid;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

public class CirclesView extends FrameLayout {
  public CirclesView(Context context, String time, String price) {
    super(context);

    init(time, price);
  }

  public CirclesView(Context context, AttributeSet attrs, String time, String price) {
    super(context, attrs);

    init(time, price);
  }

  public CirclesView(Context context, AttributeSet attrs, int defStyleAttr, String time, String price) {
    super(context, attrs, defStyleAttr);

    init(time, price);
  }

  private void init(String time, String price) {
    inflate(getContext(), R.layout.view_circles, this);

    TextView timeView = (TextView) findViewById(R.id.view_circles_time);
    TextView priceView = (TextView) findViewById(R.id.view_circles_price);

    timeView.setText(time);
    priceView.setText(price);
  }
}
