package com.github.energion.energionandroid.manual;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.energion.energionandroid.R;
import com.github.energion.energionandroid.model.Day;
import com.github.energion.energionandroid.model.Hour;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ManualFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ManualFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManualFragment extends Fragment {
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

    private OnFragmentInteractionListener mListener;
    private OnChartValueSelectedListener chartSelectionListener;
    private TextView priceText;

    public ManualFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManualFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManualFragment newInstance(String param1, String param2) {
        ManualFragment fragment = new ManualFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        chartSelectionListener = new OnChartValueSelectedListener(){
            @Override
            public void onValueSelected(Entry e, Highlight h) {
//                priceText.setText(String.valueOf(((Hour)e.getData()).getPrice()));
                priceText.setText(String.valueOf(e.getY()) + " " + getResources().getString(R.string.selected_price_currency));
            }

            @Override
            public void onNothingSelected() {

            }
        };
//        daysList.add(new Day());
//        daysList.get(0).setDate(new Date().toString());
//        List<Hour> hours = new ArrayList<>();
//        for (int i = 0; i < 35; i++) {
//            Hour h = new Hour();
//            h.setHour(String.valueOf(i));
//            h.setPrice((float)i);
//            hours.add(h);
//        }
//        daysList.get(0).setHours(hours);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manual, container, false);

        priceText = (TextView) view.findViewById(R.id.selected_price);
        List<BarEntry> entries = new ArrayList<>();
        float start = 1f;
        for (Day day : daysList) {
            for (Hour hour : day.getHours()) {
//                entries.add(new BarEntry(hour.getPrice(), start, hour));
                start++;
            }
        }
        for (int i = 1; i < 50; i++) {

            entries.add(new BarEntry(i, i));
        }

        BarDataSet barDataSet = new BarDataSet(entries, "BarDataSet");
        int[] colors = new int[barDataSet.getEntryCount()];
        for (int i = 0; i < colors.length; i++){
            float selectedPrice = barDataSet.getEntryForIndex(i).getY() - getMinimumPrice();
            float priceRange = (getMaximumPrice() - getMinimumPrice()) / 3;
            Log.d("FragmentColors: ", "Selected price: " + selectedPrice + ", priceRange: " + priceRange);
            if (selectedPrice < priceRange) {
                colors[i] = Color.parseColor("#F44242");
            } else if (selectedPrice > (priceRange * 2)) {
                colors[i] = Color.parseColor("#45F442");
            } else {
                colors[i] = Color.parseColor("#F4DC42");
            }
        }
        barDataSet.setColors(colors);

        barData = new BarData(barDataSet);
        barChart = (BarChart) view.findViewById(R.id.chart);
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.setVisibleXRangeMinimum(0f);
        barChart.setVisibleYRangeMinimum(0f, YAxis.AxisDependency.RIGHT);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setDrawAxisLine(false);
        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.getAxisRight().setDrawAxisLine(false);
        barChart.setOnChartValueSelectedListener(chartSelectionListener);
        barChart.invalidate();

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
