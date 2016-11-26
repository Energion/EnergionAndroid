package com.github.energion.energionandroid.manual;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.energion.energionandroid.DataObservable;
import com.github.energion.energionandroid.DataObserver;
import com.github.energion.energionandroid.R;
import com.github.energion.energionandroid.model.Day;
import com.github.energion.energionandroid.model.Hour;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

public class ManualFragment extends Fragment implements DataObserver {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Day> daysList = new ArrayList<>();
    private BarChart barChart;
    private BarData barData;
    private ConcurrentHashMap<Float, Integer> hoursDependencies = new ConcurrentHashMap<>();

    private OnFragmentInteractionListener mListener;
    private OnChartValueSelectedListener chartSelectionListener;
    private TextView priceText;

    private DataObservable observable;

    public ManualFragment() {
        // Required empty public constructor
    }

    public static ManualFragment newInstance(DataObservable observable) {
        ManualFragment fragment = new ManualFragment();

        fragment.setObservable(observable);

        observable.subscribe(fragment);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        chartSelectionListener = new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
//                priceText.setText(String.valueOf(((Hour)e.getData()).getPrice()));
                priceText.setText(String.valueOf(e.getY()) + " " + getResources().getString(R.string.selected_price_currency));
            }

            @Override
            public void onNothingSelected() {

            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manual, container, false);

        priceText = (TextView) view.findViewById(R.id.selected_price);
        barChart = (BarChart) view.findViewById(R.id.chart);
        refreshDates();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private float getMinimumPrice() {
        return getPrice(false);
    }

    private float getMaximumPrice() {
        return getPrice(true);
    }

    private float getPrice(boolean maximum) {
        float result = -1f;
        for (Day d : daysList) {
            for (Hour h : d.getHours()) {
                if (result == -1f) {
                    result = h.getPrice();
                } else if (maximum && h.getPrice() > result) {
                    result = h.getPrice();
                } else if (!maximum && h.getPrice() < result) {
                    result = h.getPrice();
                }
            }
        }
        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void update(List<Day> dayList) {
        this.daysList = dayList;
        refreshDates();
    }

    private void refreshDates() {
        List<BarEntry> entries = new ArrayList<>();
        float start = 0f;
        List<String> xLabels = new ArrayList<>();
        for (Day day : daysList) {
            for (Hour hour : day.getHours()) {
                entries.add(new BarEntry(start, hour.getPrice(), hour));
                xLabels.add(String.valueOf(hour.getHour()));
                hoursDependencies.put(start, hour.getHour());
                start++;
            }
        }
        String[] labels = new String[xLabels.size()];
        for (int i = 0; i < xLabels.size(); i++) {
            labels[i] = xLabels.get(i);
        }
        BarDataSet barDataSet = new BarDataSet(entries, "BarDataSet");
        int[] colors = new int[barDataSet.getEntryCount()];
        for (int i = 0; i < colors.length; i++) {
            float selectedPrice = barDataSet.getEntryForIndex(i).getY() - getMinimumPrice();
            float priceRange = (getMaximumPrice() - getMinimumPrice()) / 3;
            float timeFloat = barDataSet.getEntryForIndex(i).getX();
            int hour = hoursDependencies.get(timeFloat);
            Calendar cal = Calendar.getInstance();
            int currentHour = cal.get(Calendar.HOUR_OF_DAY);
            if (timeFloat < 24f && hour < currentHour) {
                colors[i] = Color.parseColor("#CCCCCC");
            } else if (selectedPrice < priceRange) {
                colors[i] = Color.parseColor("#45F442");
            } else if (selectedPrice > (priceRange * 2)) {
                colors[i] = Color.parseColor("#F44242");
            } else {
                colors[i] = Color.parseColor("#F4DC42");
            }
        }
        barDataSet.setColors(colors);
        barDataSet.setDrawValues(false);
        barDataSet.setStackLabels(labels);
        barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.setVisibleXRangeMinimum(0f);
        barChart.setVisibleYRangeMinimum(0f, YAxis.AxisDependency.RIGHT);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        XAxis xaxis = barChart.getXAxis();
        IAxisValueFormatter xAxisFormatter = new HourAxisValueFormatter();
        xaxis.setValueFormatter(xAxisFormatter);
        xaxis.setDrawGridLines(false);
        xaxis.setDrawAxisLine(false);
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        LimitLine limitLine = new LimitLine(23.5f);
        limitLine.setLineColor(Color.parseColor("#939393"));
        xaxis.addLimitLine(limitLine);
        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.getAxisRight().setDrawAxisLine(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.setOnChartValueSelectedListener(chartSelectionListener);
        barChart.getLegend().setEnabled(false);
        barChart.setKeepScreenOn(true);
        barChart.invalidate();
    }

    public class HourAxisValueFormatter implements IAxisValueFormatter {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if (hoursDependencies.get(value) != null) {
                return hoursDependencies.get(value).toString();
            } else {
                return "";
            }
        }
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
