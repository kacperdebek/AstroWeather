package com.example.astroweather;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;

public class SunFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_sun, container, false);

        //Get sunInfo from main activity
        AstroCalculator.SunInfo sunInfo = activity.sunInfo;

        //Set up information about the sunrise onscreen
        TextView sunriseField = view.findViewById(R.id.sunrise);
        String sunriseText = "Sunrise: " + sunInfo.getSunrise().toString();
        sunriseField.setText(sunriseText);

        //Set up information about the sunset onscreen
        TextView sunsetField = view.findViewById(R.id.sunset);
        String sunsetText = "Sunset: " + sunInfo.getSunset().toString();
        sunsetField.setText(sunsetText);

        //Set up information about the sun's azimuth rise onscreen
        TextView azimuthRiseField = view.findViewById(R.id.azimuthrise);
        //If coordinates make it impossible for azimuth to exist display N/A
        String azimuthRiseText = !Double.isNaN(sunInfo.getAzimuthRise()) ? "Azimuth rise: " + sunInfo.getAzimuthRise() : "Azimuth rise: N/A";
        azimuthRiseField.setText(azimuthRiseText);

        //Set up information about the sun's azimuth set onscreen
        TextView azimuthSetField = view.findViewById(R.id.azimuthset);
        //If coordinates make it impossible for azimuth to exist display N/A
        String azimuthSetText = !Double.isNaN(sunInfo.getAzimuthSet()) ? "Azimuth set: " + sunInfo.getAzimuthSet() : "Azimuth set: N/A";
        azimuthSetField.setText(azimuthSetText);

        //Set up information about the twilight morning onscreen
        TextView twilightMorningField = view.findViewById(R.id.twilightmorning);
        String twilightMorningText = "Twilight morning: " + sunInfo.getTwilightMorning().toString();
        twilightMorningField.setText(twilightMorningText);

        //Set up information about the twilight evening onscreen
        TextView twilightEveningField = view.findViewById(R.id.twilightevening);
        String twilightEveningText = "Twilight evening: " + sunInfo.getTwilightEvening().toString();
        twilightEveningField.setText(twilightEveningText);

        return view;
    }

}
