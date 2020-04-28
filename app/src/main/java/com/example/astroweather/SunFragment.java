package com.example.astroweather;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SunFragment extends Fragment {
    private TextView sunrise;
    private TextView sunset;
    private TextView azimuthRise;
    private TextView azimuthSet;
    private TextView twilightMorning;
    private TextView twilightEvening;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_sun, container, false);
        sunrise = view.findViewById(R.id.sunrise);

        sunrise.setText(activity.sunInfo.getSunrise().toString());
        return view;
    }

}
