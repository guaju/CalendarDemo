package com.guaju.calendardemo;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CalendarView;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private CalendarView cv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getCurrentMonth();
        String localMd5 = Md5Utils.getLocalMd5(this);
        Log.e(TAG, "local "+localMd5 );
        AssetManager assets = getAssets();
        try {
            InputStream open = assets.open("app-release.apk");
            String fileMd5 = Md5Utils.getFileMd5(open);
            Log.e(TAG, "file:"+fileMd5);

        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = Md5Utils.Upper2("8E:FE:1E:F3:EC:F3:32:F2:60:ED:D1:42:FF:43:E6:13");
        Log.e(TAG, "locallocal "+s );

        String s1 = Md5Utils.showUninstallAPKSignatures(Environment.getExternalStorageDirectory() + "/test/app.apk");
        Log.e(TAG, "hehe: "+s1 );
      

    }
    private void initView() {
        cv = (CalendarView) findViewById(R.id.cv);

    }
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


//     01e67c3128e16b8ce111477c656c0455
//     8E:FE:1E:F3:EC:F3:32:F2:60:ED:D1:42:FF:43:E6:13
//     01e67c3128e16b8ce111477c656c0455
//        3430bca12cdd7fcbb1840828d171dac7
//        8efe1ef3ecf332f260edd142ff43e613
//        8efe1ef3ecf332f260edd142ff43e613
//        3430bca12cdd7fcbb1840828d171dac7
}
