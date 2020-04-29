package com.example.astroweather;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

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

        AstroCalculator.MoonInfo moonInfo = activity.moonInfo;

        TextView moonriseField = view.findViewById(R.id.moonrise);
        AstroDateTime moonrise = moonInfo.getMoonrise();
        String moonriseText = moonrise != null ? "Moonrise: " + moonrise.toString() : "Moonrise: N/A";
        moonriseField.setText(moonriseText);

        TextView moonsetField = view.findViewById(R.id.moonset);
        AstroDateTime moonset = moonInfo.getMoonset();
        String moonsetText = moonset != null ? "Moonset: " + moonset.toString() : "Moonset: N/A";
        moonsetField.setText(moonsetText);

        TextView nextFullMoonField = view.findViewById(R.id.nextfullmoon);
        String nextFullMoonText = "Next full moon: " + moonInfo.getNextFullMoon().toString();
        nextFullMoonField.setText(nextFullMoonText);

        TextView nextNewMoonField = view.findViewById(R.id.nextnewmoon);
        String nextNewMoonText = "Next new moon: " + moonInfo.getNextNewMoon().toString();
        nextNewMoonField.setText(nextNewMoonText);

        TextView illuminationField = view.findViewById(R.id.illumination);
        String illuminationText = "Illumination: " + Math.round(moonInfo.getIllumination()*100) + "%";
        illuminationField.setText(illuminationText);

        TextView moonAgeField = view.findViewById(R.id.moonage);
        String moonAgeText = "Moon age: " + Math.round(moonInfo.getAge()) + " days";
        moonAgeField.setText(moonAgeText);

        return view;
    }

}
