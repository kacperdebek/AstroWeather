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
        String srHour = fillText(sunInfo.getSunrise().getHour());
        String srMinute = fillText(sunInfo.getSunrise().getMinute());
        String sunriseText = "Sunrise: " + srHour + ":" + srMinute;
        sunriseField.setText(sunriseText);

        //Set up information about the sunset onscreen
        TextView sunsetField = view.findViewById(R.id.sunset);
        String ssHour = fillText(sunInfo.getSunset().getHour());
        String ssMinute = fillText(sunInfo.getSunset().getMinute());
        String sunsetText = "Sunset: " + ssHour + ":" + ssMinute;
        sunsetField.setText(sunsetText);

        //Set up information about the sun's azimuth rise onscreen
        TextView azimuthRiseField = view.findViewById(R.id.azimuthrise);
        //If coordinates make it impossible for azimuth to exist display N/A
        String azimuthRiseText = !Double.isNaN(sunInfo.getAzimuthRise()) ? "Azimuth rise: " + Math.floor(sunInfo.getAzimuthRise() * 100) / 100 + "°" : "Azimuth rise: N/A";
        azimuthRiseField.setText(azimuthRiseText);

        //Set up information about the sun's azimuth set onscreen
        TextView azimuthSetField = view.findViewById(R.id.azimuthset);
        //If coordinates make it impossible for azimuth to exist display N/A
        String azimuthSetText = !Double.isNaN(sunInfo.getAzimuthSet()) ? "Azimuth set: " + Math.floor(sunInfo.getAzimuthSet() * 100) / 100 + "°" : "Azimuth set: N/A";
        azimuthSetField.setText(azimuthSetText);

        //Set up information about the twilight morning onscreen
        TextView twilightMorningField = view.findViewById(R.id.twilightmorning);
        String tmHour = fillText(sunInfo.getTwilightMorning().getHour());
        String tmMinute = fillText(sunInfo.getTwilightMorning().getMinute());
        String twilightMorningText = "Twilight morning: " + tmHour + ":" + tmMinute;
        twilightMorningField.setText(twilightMorningText);

        //Set up information about the twilight evening onscreen
        TextView twilightEveningField = view.findViewById(R.id.twilightevening);
        String teHour = fillText(sunInfo.getTwilightEvening().getHour());
        String teMinute = fillText(sunInfo.getTwilightEvening().getMinute());
        String twilightEveningText = "Twilight evening: " + teHour + ":" + teMinute;
        twilightEveningField.setText(twilightEveningText);

        return view;
    }

    private String fillText(int i) {
        return i > 9 ? i + "" : "0" + i;
    }
}
