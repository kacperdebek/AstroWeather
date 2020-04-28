package com.example.astroweather;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        TextView sunrise = view.findViewById(R.id.sunrise);
        String sunriseText = "Sunrise: " + activity.sunInfo.getSunrise().toString();
        sunrise.setText(sunriseText);

        TextView sunset = view.findViewById(R.id.sunset);
        String sunsetText = "Sunset: " + activity.sunInfo.getSunset().toString();
        sunset.setText(sunsetText);

        TextView azimuthRise = view.findViewById(R.id.azimuthrise);
        String azimuthRiseText = "Azimuth rise: " + activity.sunInfo.getAzimuthRise();
        azimuthRise.setText(azimuthRiseText);

        TextView azimuthSet = view.findViewById(R.id.azimuthset);
        String azimuthSetText = "Azimuth set: " + activity.sunInfo.getAzimuthSet();
        azimuthSet.setText(azimuthSetText);

        TextView twilightMorning = view.findViewById(R.id.twilightmorning);
        String twilightMorningText = "Twilight morning: " + activity.sunInfo.getTwilightMorning().toString();
        twilightMorning.setText(twilightMorningText);

        TextView twilightEvening = view.findViewById(R.id.twilightevening);
        String twilightEveningText = "Twilight evening: " + activity.sunInfo.getTwilightEvening().toString();
        twilightEvening.setText(twilightEveningText);

        return view;
    }

}
