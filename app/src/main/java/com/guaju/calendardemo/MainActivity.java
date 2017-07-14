package com.guaju.calendardemo;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CalendarView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private CalendarView cv;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getCurrentMonth();
    }

    private void initView() {
        cv = (CalendarView) findViewById(R.id.cv);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void getCurrentMonth() {

        Date date = new Date();
        long time = date.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String format = sdf.format(time);
        Log.e(TAG, "getCurrentMonth: " + format);
        int year = Integer.parseInt(format);
        int month = date.getMonth() + 1;
        int maxDayNum = MonthDay.getMaxDayNum(year, month);

        Date date1 = new Date(month + "/01" + "/" + year);
        Date date2 = new Date(month + "/" + maxDayNum + "/" + year);
        cv.setMinDate(date1.getTime());
        cv.setMaxDate(date2.getTime());




    }
}
