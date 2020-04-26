package com.example.astroweather;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextClock;

import java.util.TimeZone;

public class MainActivity extends Activity {
    private TextClock currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        currentTime = findViewById(R.id.currentTime);
        currentTime.setTimeZone(TimeZone.getDefault().getID());
    }
}
