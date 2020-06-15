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

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

public class Options extends Activity {
    EditText longitude;
    EditText latitude;
    CustomSpinner frequency;
    CustomSpinner units;
    Button save;
    TourGuide guide;
    int frequencyChoice = 0;
    int unitsChoice = 0;
    int prevChoice = 0;
    boolean firstRun;
    private boolean unitsChanged;

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
        units = findViewById(R.id.units);

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
        //prevent setOnItemSelectedListener from invoking onItemSelected
        frequency.setSelection(0,false);
        frequency.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                //if its first run then continue with the tutorial
                if(firstRun) {
                    guide.cleanUp();
                    guide = setUpTourGuide("Units", "Now select desired unit system", units);
                    hideKeyboard();
                    units.requestFocus();
                }
                frequencyChoice = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Setup the spinner options
        List<String> availableUnits = new ArrayList<>();
        availableUnits.add("Celsius");
        availableUnits.add("Fahrenheit");
        availableUnits.add("Kelvin");

        //create spinner adapter
        ArrayAdapter<String> unitsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, availableUnits);
        unitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        unitsChanged = false;
        // set adapter to the spinner
        units.setAdapter(unitsAdapter);
        //prevent setOnItemSelectedListener from invoking onItemSelected
        units.setSelection(0,false);
        units.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(firstRun){
                    guide.cleanUp();
                    guide = setUpTourGuide(" ", " ", save);
                }
                if(position != unitsChoice) {
                    unitsChanged = true;
                }
                prevChoice = unitsChoice;
                unitsChoice = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
            int bundledUnits = extras.getInt("units");
            boolean isFirstRun = extras.getBoolean("firstrun");
            if (bundledLatitude != null) {
                latitude.setText(bundledLatitude);
            }
            if (bundledLongitude != null) {
                longitude.setText(bundledLongitude);
            }
            frequencyChoice = bundledFrequency;
            unitsChoice = bundledUnits;
            frequency.setSelection(frequencyChoice);
            units.setSelection(unitsChoice);
            firstRun = isFirstRun;
        }
        //Configure the save button
        save = findViewById(R.id.saveOptions);
        save.setOnClickListener(view -> {
            Intent i = new Intent(view.getContext(), MainActivity.class);
            //Make sure the user is not trying to save empty values
            if (!TextUtils.isEmpty(longitude.getText().toString())) {
                i.putExtra("longitude", longitude.getText().toString());
            }
            if (!TextUtils.isEmpty(latitude.getText().toString())) {
                i.putExtra("latitude", latitude.getText().toString());
            }
            i.putExtra("frequency", frequencyChoice);
            i.putExtra("units", unitsChoice);
            Context context = getApplicationContext();
            if(unitsChanged){
                sendUnitsChanged();
            }
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
        });

        if (firstRun) {
            //Set text fields as empty
            latitude.setText("");
            longitude.setText("");

            //Begin the tutorial from the first text field
            guide = setUpTourGuide("Latitude", "Enter your current latitude here", latitude);
        }
    }
    private void sendUnitsChanged(){
        EventBus.getDefault().postSticky(new UnitsChangedEvent(prevChoice, unitsChoice));
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
                        //If user clicks on 'next' or 'done' button on keyboard then proceed with the tutorial and change text field focus to the next one
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_NEXT ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
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
                        //If user clicks on 'next' or 'done' button on keyboard then proceed with the tutorial and change text field focus to the next one
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_NEXT ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event != null &&
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if ((event == null || !event.isShiftPressed()) && firstRun) {
                                guide.cleanUp();
                                guide = setUpTourGuide("Frequency", "Now enter refreshing frequency", frequency);
                                hideKeyboard();
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
}
