package com.example.astroweather;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MoonFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_moon, container, false);

        TextView moonrise = view.findViewById(R.id.moonrise);
        String moonriseText = "Moonrise: " + activity.moonInfo.getMoonrise().toString();
        moonrise.setText(moonriseText);

        TextView moonset = view.findViewById(R.id.moonset);
        String moonsetText = "Moonset: " + activity.moonInfo.getMoonset().toString();
        moonset.setText(moonsetText);

        TextView nextFullMoon = view.findViewById(R.id.nextfullmoon);
        String nextFullMoonText = "Next full moon: " + activity.moonInfo.getNextFullMoon().toString();
        nextFullMoon.setText(nextFullMoonText);

        TextView nextNewMoon = view.findViewById(R.id.nextnewmoon);
        String nextNewMoonText = "Next new moon: " + activity.moonInfo.getNextNewMoon().toString();
        nextNewMoon.setText(nextNewMoonText);

        TextView illumination = view.findViewById(R.id.illumination);
        String illuminationText = "Illumination %: " + activity.moonInfo.getIllumination();
        illumination.setText(illuminationText);

        TextView moonAge = view.findViewById(R.id.moonage);
        String moonAgeText = "Moon age: " + activity.moonInfo.getAge();
        moonAge.setText(moonAgeText);

        return view;
    }

}
