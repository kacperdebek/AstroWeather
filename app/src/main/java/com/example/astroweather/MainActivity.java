package com.example.astroweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextClock;

import java.util.TimeZone;

public class MainActivity extends Activity {

    private double latitude = 0;
    private double longitude = 0;
    private double frequency = 0;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String bundledLatitude = extras.getString("latitude");
            String bundledLongitude = extras.getString("longitude");
            String bundledFrequency = extras.getString("frequency");
            if (bundledLatitude != null) {
                latitude = Double.parseDouble(bundledLatitude);
            }
            if (bundledLongitude != null) {
                longitude = Double.parseDouble(bundledLongitude);
            }
            if (bundledFrequency != null) {
                frequency = Double.parseDouble(bundledFrequency);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
}
