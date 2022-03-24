package com.example.covicareapp.helpers;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class XAxisValueFormatter extends ValueFormatter {

    private int pagerType;

    public XAxisValueFormatter(int pagerType) {
        this.pagerType = pagerType;
    }


    @Override
    public String getAxisLabel(float value, AxisBase axis) {

        long milliSeconds = (long) value;
        Log.d("TEMP", "getAxisLabel: axis label value : " + milliSeconds);
        System.out.println(milliSeconds);
        DateFormat formatter;
        switch (pagerType) {
            case Constants.WEEK:
                formatter = new SimpleDateFormat("EEE HH:mm");
                break;
            case Constants.MONTH:
                formatter = new SimpleDateFormat("MMM dd");
                break;
            case Constants.YEAR:
                formatter = new SimpleDateFormat("MMM");
                break;
            default:
                formatter = new SimpleDateFormat("dd/MM/yy");
        }



        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);


        return formatter.format(calendar.getTime());

    }

    public int getPagerType() {
        return pagerType;
    }

    public void setPagerType(int pagerType) {
        this.pagerType = pagerType;
    }
}