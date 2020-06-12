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
        String mrHour = fillText(moonInfo.getMoonrise().getHour());
        String mrMinute = fillText(moonInfo.getMoonrise().getMinute());
        //If moonrise can't exist within given coordinates display N/A
        String moonriseText = moonrise != null ? "Moonrise: " + mrHour + ":" + mrMinute : "Moonrise: N/A";
        moonriseField.setText(moonriseText);

        //Set up the moonset information onscreen
        TextView moonsetField = view.findViewById(R.id.moonset);
        AstroDateTime moonset = moonInfo.getMoonset();
        String msHour = fillText(moonInfo.getMoonset().getHour());
        String msMinute = fillText(moonInfo.getMoonset().getMinute());
        //If moonset can't exist within given coordinates display N/A
        String moonsetText = moonset != null ? "Moonset: " + msHour + ":" + msMinute : "Moonset: N/A";
        moonsetField.setText(moonsetText);

        //Set up the information about next full moon onscreen
        TextView nextFullMoonField = view.findViewById(R.id.nextfullmoon);
        String fmHour = fillText(moonInfo.getNextFullMoon().getHour());
        String fmMinute = fillText(moonInfo.getNextFullMoon().getMinute());
        String nextFullMoonText = "Next full moon: " + fmHour + ":" + fmMinute + " "
                + fillText(moonInfo.getNextFullMoon().getDay()) + "-"
                + fillText(moonInfo.getNextFullMoon().getMonth()) + "-"
                + fillText(moonInfo.getNextFullMoon().getYear());
        nextFullMoonField.setText(nextFullMoonText);

        //Set up the information about next new moon onscreen
        TextView nextNewMoonField = view.findViewById(R.id.nextnewmoon);
        String nmHour = fillText(moonInfo.getNextNewMoon().getHour());
        String nmMinute = fillText(moonInfo.getNextNewMoon().getMinute());
        String nextNewMoonText = "Next new moon: " + nmHour + ":" + nmMinute + " "
                + fillText(moonInfo.getNextNewMoon().getDay()) + "-"
                + fillText(moonInfo.getNextNewMoon().getMonth()) + "-"
                + fillText(moonInfo.getNextNewMoon().getYear());
        nextNewMoonField.setText(nextNewMoonText);

        //Set up the information about the moon's illumination percentage onscreen
        TextView illuminationField = view.findViewById(R.id.illumination);
        String illuminationText = "Illumination: " + Math.floor(((moonInfo.getIllumination()*100))*10)/10 + "%";
        illuminationField.setText(illuminationText);

        //Set up the information about the moon's age onscreen
        TextView moonAgeField = view.findViewById(R.id.moonage);
        String moonAgeText = "Moon age: " + Math.round(moonInfo.getAge()) + " days";
        moonAgeField.setText(moonAgeText);

        return view;
    }
    private String fillText(int i){
        return i>9?i+"":"0"+i;
    }
}
