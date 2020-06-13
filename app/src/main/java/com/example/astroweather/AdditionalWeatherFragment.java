package com.example.astroweather;

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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ApiRespondedEvent event){
        windSpeed.setText(event.windSpeed);
        windDirection.setText(event.windDirection);
        visibility.setText(event.visibility);
        humidity.setText(event.humidity);
    }
    public TextView getWindDirection() {
        return windDirection;
    }

    public TextView getWindSpeed() {
        return windSpeed;
    }

    public TextView getVisibility() {
        return visibility;
    }

    public TextView getHumidity() {
        return humidity;
    }

}
