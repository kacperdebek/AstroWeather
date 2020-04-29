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
        //Get moonInfo from main activity
        AstroCalculator.MoonInfo moonInfo = activity.moonInfo;

        //Set up the moonrise information onscreen
        TextView moonriseField = view.findViewById(R.id.moonrise);
        AstroDateTime moonrise = moonInfo.getMoonrise();
        //If moonrise can't exist within given coordinates display N/A
        String moonriseText = moonrise != null ? "Moonrise: " + moonrise.toString() : "Moonrise: N/A";
        moonriseField.setText(moonriseText);

        //Set up the moonset information onscreen
        TextView moonsetField = view.findViewById(R.id.moonset);
        AstroDateTime moonset = moonInfo.getMoonset();
        //If moonset can't exist within given coordinates display N/A
        String moonsetText = moonset != null ? "Moonset: " + moonset.toString() : "Moonset: N/A";
        moonsetField.setText(moonsetText);

        //Set up the information about next full moon onscreen
        TextView nextFullMoonField = view.findViewById(R.id.nextfullmoon);
        String nextFullMoonText = "Next full moon: " + moonInfo.getNextFullMoon().toString();
        nextFullMoonField.setText(nextFullMoonText);

        //Set up the information about next new moon onscreen
        TextView nextNewMoonField = view.findViewById(R.id.nextnewmoon);
        String nextNewMoonText = "Next new moon: " + moonInfo.getNextNewMoon().toString();
        nextNewMoonField.setText(nextNewMoonText);

        //Set up the information about the moon's illumination percentage onscreen
        TextView illuminationField = view.findViewById(R.id.illumination);
        String illuminationText = "Illumination: " + Math.round(moonInfo.getIllumination()*100) + "%";
        illuminationField.setText(illuminationText);

        //Set up the information about the moon's age onscreen
        TextView moonAgeField = view.findViewById(R.id.moonage);
        String moonAgeText = "Moon age: " + Math.round(moonInfo.getAge()) + " days";
        moonAgeField.setText(moonAgeText);

        return view;
    }

}
