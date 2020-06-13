package com.example.astroweather;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AdditionalWeatherFragment extends Fragment {

    TextView windDirection;
    TextView windSpeed;
    TextView visibility;
    TextView humidity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_additionalweather, container, false);
        windDirection = view.findViewById(R.id.windDirection);
        windSpeed = view.findViewById(R.id.windSpeed);
        visibility = view.findViewById(R.id.visibility);
        humidity = view.findViewById(R.id.humidity);
        EventBus.getDefault().register(this);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ApiRespondedEvent event) {
        if (!event.isForecast) {
            windSpeed.setText("Wind Speed: " + event.windSpeed + "m/s");
            windDirection.setText("Wind direction: " + event.windDirection + "°");
            visibility.setText("Visibility: " + event.visibility + "m");
            humidity.setText("Humidity: " + event.humidity + "%");
        }
    }
}
