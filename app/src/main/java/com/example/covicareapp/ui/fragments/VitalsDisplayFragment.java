package com.example.covicareapp.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.covicareapp.R;
import com.example.covicareapp.databinding.FragmentVitalsDisplayBinding;
import com.example.covicareapp.helpers.Constants;
import com.example.covicareapp.helpers.VitalsSQLiteHelper;
import com.example.covicareapp.helpers.XAxisValueFormatter;
import com.example.covicareapp.models.OnlineUserVitalsModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class VitalsDisplayFragment extends Fragment {

    private static final String TAG = "VitalDisplayFragment";
    LineChart chart;
    FragmentVitalsDisplayBinding binding;
    int currentVital = Constants.TEMPERATURE_ID;
    int leftCardVital = Constants.PULSE_ID;
    int rightCardVital = Constants.SPO2_ID;
    int pagerType;
    List<OnlineUserVitalsModel> vitalsData = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("#.#");


    public VitalsDisplayFragment() {
        // Required empty public constructor
    }

    public static VitalsDisplayFragment newInstance() {
        VitalsDisplayFragment fragment = new VitalsDisplayFragment();

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void updateUI() {

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.leftVitalCard.setOnClickListener(view1 -> {
            int temp = currentVital;
            currentVital = leftCardVital;
            leftCardVital = temp;
            setData();
        });

        binding.rightVitalCard.setOnClickListener(view1 -> {
            int temp = currentVital;
            currentVital = rightCardVital;
            rightCardVital = temp;
            setData();
        });

        Bundle args = getArguments();

        assert args != null;
        pagerType = args.getInt(Constants.PAGER_TYPE);
        String userId = args.getString(Constants.USER_ID);


        binding.vitalValue.setText(String.valueOf(pagerType));
        chart = binding.chart;

        getData(pagerType, userId);

        XAxis xAxis = chart.getXAxis();
        XAxis.XAxisPosition position = XAxis.XAxisPosition.BOTTOM;
        xAxis.setPosition(position);
        xAxis.enableGridDashedLine(2f, 7f, 0f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(315f);

        chart.getDescription().setEnabled(false);

        YAxis axisLeft = chart.getAxisLeft();
        axisLeft.setTextColor(Color.WHITE);
        YAxis axisRight = chart.getAxisRight();
        axisRight.setTextColor(Color.WHITE);

    }

    private void getData(int pagerType, String userId) {

        final List<OnlineUserVitalsModel>[] data = new List[]{new ArrayList<>()};
        VitalsSQLiteHelper vitalsSQLiteHelper = new VitalsSQLiteHelper(getActivity());
        XAxis xAxis = chart.getXAxis();

        switch (pagerType) {
            case Constants.WEEK:

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, -7);
                vitalsData = vitalsSQLiteHelper.getVitalsForUserListOnlineBetween(userId, calendar.getTime().getTime(), Calendar.getInstance().getTime().getTime());

                Log.d(TAG, "getData: userId : " + userId);
                Log.d(TAG, "getData: between : " + calendar.getTime().getTime() + " and " + Calendar.getInstance().getTime().getTime());
                vitalsData = vitalsSQLiteHelper.getVitalsForUserListOnlineBetween(userId, calendar.getTime().getTime(), Calendar.getInstance().getTime().getTime());

                setData();

                xAxis.setValueFormatter(new XAxisValueFormatter(Constants.WEEK));


                break;

            case Constants.MONTH:

                calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, -30);
                Log.d(TAG, "getData: calendar time from : " + calendar.getTime().getTime());
                Log.d(TAG, "getData: to : " + Calendar.getInstance().getTime().getTime());
                Log.d(TAG, "getData: week vitals data : " + vitalsData.toString());

                vitalsData = vitalsSQLiteHelper.getVitalsForUserListOnlineBetween(userId, calendar.getTime().getTime(), Calendar.getInstance().getTime().getTime());

                Log.d(TAG, "getData: vitals Data : " + vitalsData);
                setData();

                xAxis.setValueFormatter(new XAxisValueFormatter(Constants.MONTH));


                break;
            case Constants.YEAR:

                calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, -12);
                Log.d(TAG, "getData: calendar time from : " + calendar.getTime().getTime());
                Log.d(TAG, "getData: to : " + Calendar.getInstance().getTime().getTime());
                vitalsData = vitalsSQLiteHelper.getVitalsForUserListOnlineBetween(userId, calendar.getTime().getTime(), Calendar.getInstance().getTime().getTime());
                Log.d(TAG, "getData: year vitals data : " + vitalsData.toString());

                setData();

                xAxis.setValueFormatter(new XAxisValueFormatter(Constants.YEAR));
            default:
        }


    }

    void setData() {

        if (vitalsData.size() == 0) {
            chart.invalidate();
            updateNoData();
            return;
        }
        ArrayList<Entry> values = new ArrayList<>();
        OnlineUserVitalsModel lastVital = vitalsData.get(vitalsData.size() - 1);

        setVitalInfo(lastVital);

        for (OnlineUserVitalsModel vital : vitalsData) {
            switch (currentVital) {
                case Constants.TEMPERATURE_ID:
                    values.add(new Entry(vital.getRecDateTime(), (float) vital.getTemperature()));
                    break;
                case Constants.SPO2_ID:
                    values.add(new Entry(vital.getRecDateTime(), (float) vital.getSp02()));
                    break;
                case Constants.PULSE_ID:
                    values.add(new Entry(vital.getRecDateTime(), (float) vital.getPulse()));
                    break;

                default:


            }
        }

        values.sort(new EntryXComparator());

        LineDataSet lineDataSet = new LineDataSet(values, getLineDataName());
        chart.getLegend().setTextColor(Color.WHITE);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        Log.d(TAG, "setData: linedataset : " + lineDataSet);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        LineData lineData = new LineData(dataSets);
        chart.setData(lineData);
        chart.invalidate();
        chart.animateX(3000, Easing.EaseOutBack);
    }

    private void updateNoData() {
        binding.vitalValue.setText("No Data");
        binding.leftVitalValue.setText("No Data");
        binding.rightVitalValue.setText("No Data");
    }

    private String getLineDataName() {
        switch (currentVital) {
            case Constants.TEMPERATURE_ID:
                return getString(R.string.temperature);
            case Constants.PULSE_ID:
                return getString(R.string.pulse);
            case Constants.SPO2_ID:
                return getString(R.string.spo2);
        }
        return "";
    }

    private void setVitalInfo(OnlineUserVitalsModel vital) {
        updateVitalInfoUI(vital, leftCardVital, binding.leftVitalValue, binding.leftVitalName);
        updateVitalInfoUI(vital, rightCardVital, binding.rightVitalValue, binding.rightVitalName);
        updateVitalInfoUI(vital, currentVital, binding.vitalValue, binding.vitalName);
    }


    private void updateVitalInfoUI(OnlineUserVitalsModel vital, int switchOn, TextView vitalValue, TextView vitalName) {
        String value;
        switch (switchOn) {
            case Constants.TEMPERATURE_ID:

                value = df.format(vital.getTemperature()) + " " + Constants.DEGREE_C;
                Log.d(TAG, "updateVitalInfoUI: temp : " + value);
                vitalValue.setText(value);
                vitalName.setText(getString(R.string.temperature));
                break;
            case Constants.SPO2_ID:
                value = df.format(vital.getSp02()) + " %";
                Log.d(TAG, "updateVitalInfoUI: spo2 : " + value);
                vitalValue.setText(value);
                vitalName.setText(getString(R.string.spo2));
                break;
            case Constants.PULSE_ID:
                value = df.format(vital.getPulse()) + " bpm";
                Log.d(TAG, "updateVitalInfoUI: pulse : " + value);
                vitalValue.setText(value);
                vitalName.setText(getString(R.string.pulse));
                break;

            default:
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVitalsDisplayBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}