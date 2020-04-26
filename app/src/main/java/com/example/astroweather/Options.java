package com.example.astroweather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Options extends Activity {
    EditText longitude;
    EditText latitude;
    EditText frequency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_options);

        longitude = findViewById(R.id.longitude);
        latitude = findViewById(R.id.latitude);
        frequency = findViewById(R.id.frequency);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String bundledLatitude = extras.getString("latitude");
            String bundledLongitude = extras.getString("longitude");
            String bundledFrequency = extras.getString("frequency");
            if (bundledLatitude != null) {
                latitude.setText(bundledLatitude);
            }
            if (bundledLongitude != null) {
                longitude.setText(bundledLongitude);
            }
            if (bundledFrequency != null) {
                frequency.setText(bundledFrequency);
            }
        }

        Button save = findViewById(R.id.saveOptions);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), MainActivity.class);
                if (!TextUtils.isEmpty(longitude.getText().toString())) {
                    i.putExtra("longitude", longitude.getText().toString());
                }
                if (!TextUtils.isEmpty(latitude.getText().toString())) {
                    i.putExtra("latitude", latitude.getText().toString());
                }
                if (!TextUtils.isEmpty(frequency.getText().toString())) {
                    i.putExtra("frequency", frequency.getText().toString());
                }
                Context context = getApplicationContext();
                Toast.makeText(context, "New options saved", Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });
    }
}
