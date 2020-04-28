package com.example.astroweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextClock;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends FragmentActivity {

    private float latitude = 0;
    private float longitude = 0;
    private float frequency = 0;
    SharedPreferences pref;
    AstroCalculator.SunInfo sunInfo;
    AstroCalculator.MoonInfo moonInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        TextClock currentTime = findViewById(R.id.currentTime);
        currentTime.setTimeZone(TimeZone.getDefault().getID());
        ImageView optionsIcon = findViewById(R.id.optionsIcon);
        optionsIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Options.class);

                myIntent.putExtra("longitude", String.valueOf(longitude));
                myIntent.putExtra("latitude", String.valueOf(latitude));
                myIntent.putExtra("frequency", String.valueOf(frequency));

                startActivity(myIntent);
            }
        });

        ViewPager pager = findViewById(R.id.fragmentsPager);
        pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        latitude = pref.getFloat("latitude", 0);
        longitude = pref.getFloat("longitude", 0);
        frequency = pref.getFloat("frequency", 0);

        updateSunAndMoonInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            SharedPreferences.Editor editor = pref.edit();
            String bundledLatitude = extras.getString("latitude");
            String bundledLongitude = extras.getString("longitude");
            String bundledFrequency = extras.getString("frequency");
            if (bundledLatitude != null) {
                latitude = Float.parseFloat(bundledLatitude);
                editor.putFloat("latitude", latitude);
            }
            if (bundledLongitude != null) {
                longitude = Float.parseFloat(bundledLongitude);
                editor.putFloat("longitude", longitude);
            }
            if (bundledFrequency != null) {
                frequency = Float.parseFloat(bundledFrequency);
                editor.putFloat("frequency", frequency);
            }
            editor.apply();
            updateSunAndMoonInfo();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private AstroCalculator getAstroCalculator() {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        int offsetInHours = TimeZone.getDefault().getRawOffset() / 3600000;
        boolean isDaylightSavings = TimeZone.getDefault().inDaylightTime(now);
        AstroDateTime currentDate = new AstroDateTime(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1
                , cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)
                , cal.get(Calendar.SECOND), offsetInHours, isDaylightSavings);
        return new AstroCalculator(currentDate, new AstroCalculator.Location(this.latitude, this.longitude));
    }

    private void updateSunAndMoonInfo() {
        AstroCalculator calculator = getAstroCalculator();
        sunInfo = calculator.getSunInfo();
        moonInfo = calculator.getMoonInfo();
    }
}
