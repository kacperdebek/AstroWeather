package com.example.astroweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.moodysalem.TimezoneMapper;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

public class MainActivity extends FragmentActivity {

    private float latitude;
    private float longitude;
    private int frequencySelection;
    private SharedPreferences pref;
    public AstroCalculator.SunInfo sunInfo;
    public AstroCalculator.MoonInfo moonInfo;
    private ImageView optionsIcon;
    private ViewPager pager;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 1000 * 10;
    public int simultaneousPages;
    private TextView coordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Make app fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
        //Set up the main clock
        TextClock currentTime = findViewById(R.id.currentTime);
        currentTime.setTimeZone(TimeZone.getDefault().getID());

        coordinates = findViewById(R.id.coordinates);

        //Set up the settings button
        optionsIcon = findViewById(R.id.optionsIcon);
        optionsIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Options.class);
                //Send current values to the options activity
                myIntent.putExtra("longitude", String.valueOf(longitude));
                myIntent.putExtra("latitude", String.valueOf(latitude));
                myIntent.putExtra("frequency", frequencySelection);
                //Inform options activity whether it's app's first launch
                if (pref.getBoolean("firstrun", true)) {
                    myIntent.putExtra("firstrun", true);
                }
                //Proceed to options screen
                startActivity(myIntent);
            }
        });
        simultaneousPages = getResources().getConfiguration().isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_XLARGE) ? 2 : 1;
        //Create swipable pager for fragments
        pager = findViewById(R.id.fragmentsPager);
        pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), simultaneousPages));

        pager.setOffscreenPageLimit(2 * simultaneousPages);
        //Preferences containing user's saved settings
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        //Load saved settings if such exist
        latitude = pref.getFloat("latitude", 0);
        longitude = pref.getFloat("longitude", 0);
        frequencySelection = pref.getInt("frequency", 0);
        //Update sun and moon information
        updateSunAndMoonInfo();

        String coords = "Latitude: " + this.latitude + "  |  Longitude: " + this.longitude;
        coordinates.setText(coords);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        //Check if there is content to access
        if (extras != null) {
            //Prepare to edit saved settings
            SharedPreferences.Editor editor = pref.edit();
            //Get newly inputted settings from options activity
            String bundledLatitude = extras.getString("latitude");
            String bundledLongitude = extras.getString("longitude");
            int bundledFrequency = extras.getInt("frequency");
            //If they exist replace current values and save them in preferences
            if (bundledLatitude != null) {
                latitude = Float.parseFloat(bundledLatitude);
                editor.putFloat("latitude", latitude);
            }
            if (bundledLongitude != null) {
                longitude = Float.parseFloat(bundledLongitude);
                editor.putFloat("longitude", longitude);
            }
            frequencySelection = bundledFrequency;
            editor.putInt("frequency", frequencySelection);
            //Apply changes
            editor.apply();
            //Update sun and moon information
            updateSunAndMoonInfo();
            handleRefreshingFragments();

            String coords = "Latitude: " + this.latitude + "  |  Longitude: " + this.longitude;
            coordinates.setText(coords);
        }
        //Launch tutorial if it's users first app launch
        if (pref.getBoolean("firstrun", true)) {
            //Set up tutorial pointing at options icon
            TourGuide.init(this).with(TourGuide.Technique.CLICK)
                    .setPointer(new Pointer())
                    .setToolTip(new ToolTip().setTitle("Before you start").setDescription("Click on this icon to set up your coordinates"))
                    .setOverlay(new Overlay())
                    .playOn(optionsIcon);
            //Hide pager for the time being
            pager.setVisibility(View.INVISIBLE);
        } else {
            //Users already done tutorial which means we can show pager now
            pager.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private AstroCalculator getAstroCalculator(TimeZone timeZone) {
        //Create date based on calculated timezone
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(timeZone);
        //Calculate timezone offset in hours
        int offsetInHours = timeZone.getRawOffset() / 3600000;
        //Determine whether the timezone falls under daylight savings
        boolean isDaylightSavings = timeZone.inDaylightTime(now);
        //Create AstroDateTime based on above data
        AstroDateTime currentDate = new AstroDateTime(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1
                , cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)
                , cal.get(Calendar.SECOND), offsetInHours, isDaylightSavings);
        return new AstroCalculator(currentDate, new AstroCalculator.Location(this.latitude, this.longitude));
    }

    private void updateSunAndMoonInfo() {
        //Calculate timezone based on user's inputted coordinates
        String timeZone = TimezoneMapper.tzNameAt(latitude, longitude);
        //Create AstroCalculator to calculate sun and moon information
        AstroCalculator calculator = getAstroCalculator(TimeZone.getTimeZone(timeZone));
        //Fetch sun and moon info
        sunInfo = calculator.getSunInfo();
        moonInfo = calculator.getMoonInfo();
    }

    private void handleRefreshingFragments() {
        //set the right refreshing delay as set in options
        switch (frequencySelection) {
            case 0:
                delay = 1000 * 10;
                break;
            case 1:
                delay = 1000 * 60 * 10;
                break;
            case 2:
                delay = 1000 * 60 * 15;
                break;
            case 3:
                delay = 1000 * 60 * 20;
                break;
        }
        //refresh the fragments
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), simultaneousPages));
                handler.postDelayed(runnable, delay);
                Toast.makeText(getApplicationContext(), "Fragments updated", Toast.LENGTH_SHORT).show();
            }
        }, delay);
    }
}
