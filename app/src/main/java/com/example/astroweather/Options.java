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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

public class Options extends Activity implements AdapterView.OnItemSelectedListener {
    EditText longitude;
    EditText latitude;
    Spinner frequency;
    Button save;
    TourGuide guide;
    int frequencyChoice = 0;
    boolean firstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Make the view fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_options);

        //Set up variables from the xml
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        frequency = findViewById(R.id.frequency);
        frequency.setOnItemSelectedListener(this);

        //Setup the spinner options
        List<String> availableFrequencies = new ArrayList<>();
        availableFrequencies.add("10 seconds (for testing)");
        availableFrequencies.add("10 minutes");
        availableFrequencies.add("15 minutes");
        availableFrequencies.add("20 minutes");

        //create spinner adapter
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, availableFrequencies);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // set adapter to the spinner
        frequency.setAdapter(dataAdapter);

        //Configure the listeners
        setEditTextListeners();
        //Initialize the boolean determining whether its users first app launch
        firstRun = false;


        Bundle extras = getIntent().getExtras();
        //Get bundled data from main activity if it exists
        if (extras != null) {
            String bundledLatitude = extras.getString("latitude");
            String bundledLongitude = extras.getString("longitude");
            int bundledFrequency = extras.getInt("frequency");
            boolean isFirstRun = extras.getBoolean("firstrun");
            if (bundledLatitude != null) {
                latitude.setText(bundledLatitude);
            }
            if (bundledLongitude != null) {
                longitude.setText(bundledLongitude);
            }
            frequencyChoice = bundledFrequency;
            frequency.setSelection(frequencyChoice);
            firstRun = isFirstRun;
        }
        //Configure the save button
        save = findViewById(R.id.saveOptions);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), MainActivity.class);
                //Make sure the user is not trying to save empty values
                if (!TextUtils.isEmpty(longitude.getText().toString())) {
                    i.putExtra("longitude", longitude.getText().toString());
                }
                if (!TextUtils.isEmpty(latitude.getText().toString())) {
                    i.putExtra("latitude", latitude.getText().toString());
                }
                i.putExtra("frequency", frequencyChoice);
                Context context = getApplicationContext();
                //Inform the user about correct options saving
                Toast.makeText(context, "New options saved", Toast.LENGTH_SHORT).show();
                //Set the firstrun variable to false - during next app launches tutorial will be omitted
                if (firstRun) {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    pref.edit().putBoolean("firstrun", false).apply();
                    firstRun = false;
                }
                //Go back to main activity
                startActivity(i);
            }
        });

        if (firstRun) {
            //Set text fields as empty
            latitude.setText("");
            longitude.setText("");

            //Begin the tutorial from the first text field
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
                        //If user clicks on 'next' button on keyboard then proceed with the tutorial and change text field focus to the next one
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_NEXT ||
                                event != null &&
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if ((event == null || !event.isShiftPressed()) && firstRun) {
                                guide.cleanUp();
                                guide = setUpTourGuide("Longtitude", "Now enter your longitude here", longitude);
                                longitude.requestFocus();
                                return true;
                            }
                        }
                        return false;
                    }
                }
        );
        longitude.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        //If user clicks on 'next' button on keyboard then proceed with the tutorial and change text field focus to the next one
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_NEXT ||
                                event != null &&
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if ((event == null || !event.isShiftPressed()) && firstRun) {
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
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        //if its first run then continue with the tutorial
        if(firstRun) {
            guide.cleanUp();
            guide = setUpTourGuide(" ", " ", save);
            hideKeyboard();
        }
        frequencyChoice = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
