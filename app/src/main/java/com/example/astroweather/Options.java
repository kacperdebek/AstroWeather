package com.example.astroweather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

public class Options extends Activity {
    EditText longitude;
    EditText latitude;
    EditText frequency;
    Button save;
    TourGuide guide;
    boolean firstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_options);


        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        frequency = findViewById(R.id.frequency);
        setEditTextListeners();
        firstRun = false;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String bundledLatitude = extras.getString("latitude");
            String bundledLongitude = extras.getString("longitude");
            String bundledFrequency = extras.getString("frequency");
            boolean isFirstRun = extras.getBoolean("firstrun");
            if (bundledLatitude != null) {
                latitude.setText(bundledLatitude);
            }
            if (bundledLongitude != null) {
                longitude.setText(bundledLongitude);
            }
            if (bundledFrequency != null) {
                frequency.setText(bundledFrequency);
            }
            firstRun = isFirstRun;
        }

        save = findViewById(R.id.saveOptions);
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
                if (firstRun) {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    System.out.println("First time running the app no more");
                    pref.edit().putBoolean("firstrun", false).apply();
                }
                startActivity(i);
            }
        });

        if (firstRun) {
            guide = setUpTourGuide("Latitude", "Enter your current latitude here", latitude);
        }
    }

    public TourGuide setUpTourGuide(String tooltipTitle, String description, View tourElement) {
        return TourGuide.init(this).with(TourGuide.Technique.CLICK)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setTitle(tooltipTitle).setDescription(description))
                .setOverlay(new Overlay())
                .playOn(tourElement);
    }

    private void setEditTextListeners() {
        latitude.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_NEXT ||
                                event != null &&
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (event == null || !event.isShiftPressed() && firstRun) {
                                guide.cleanUp();
                                guide = setUpTourGuide("Longtitude", "Now enter your longitude here", longitude);
                                longitude.requestFocus();
                                return true; // consume.
                            }
                        }
                        return false; // pass on to other listeners.
                    }
                }
        );
        longitude.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_NEXT ||
                                event != null &&
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (event == null || !event.isShiftPressed() && firstRun) {
                                guide.cleanUp();
                                guide = setUpTourGuide("Frequency", "Now enter refreshing frequency", frequency);
                                frequency.requestFocus();
                                return true;
                            }
                        }
                        return false;
                    }
                }
        );
        frequency.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event != null &&
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                        (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                            if (event == null || !event.isShiftPressed() && firstRun) {
                                guide.cleanUp();
                                guide = setUpTourGuide(" ", " ", save);
                                hideKeyboard();
                                return true;
                            }
                        }
                        return false;
                    }
                }
        );
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
