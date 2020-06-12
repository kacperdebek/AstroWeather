package com.example.astroweather;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainWeatherFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mainweather, container, false);
        TextView weather = view.findViewById(R.id.textView);
        APICaller apiCaller = new APICaller();
        try {
            apiCaller.callApi(weather);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
}
