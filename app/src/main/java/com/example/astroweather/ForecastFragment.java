package com.example.astroweather;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class ForecastFragment extends Fragment {

    private TextView day1;
    private TextView day2;
    private TextView day3;
    private TextView day4;
    private TextView day5;
    private ImageView icon1;
    private ImageView icon2;
    private ImageView icon3;
    private ImageView icon4;
    private ImageView icon5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);
        day1 = view.findViewById(R.id.day1);
        day2 = view.findViewById(R.id.day2);
        day3 = view.findViewById(R.id.day3);
        day4 = view.findViewById(R.id.day4);
        day5 = view.findViewById(R.id.day5);

        icon1 = view.findViewById(R.id.icon1);
        icon2 = view.findViewById(R.id.icon2);
        icon3 = view.findViewById(R.id.icon3);
        icon4 = view.findViewById(R.id.icon4);
        icon5 = view.findViewById(R.id.icon5);

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
    public void onMessageEvent(ApiRespondedEvent event){
        if(event.isForecast) {
            Map<Integer, ArrayList<String>> forecasts = event.forecasts;
            this.day1.setText(getDateStringFromEpoch(forecasts.get(0).get(2)) + "\nTemperature: " + forecasts.get(0).get(0) + "°C");
            this.day2.setText(getDateStringFromEpoch(forecasts.get(1).get(2)) + "\nTemperature: " + forecasts.get(1).get(0) + "°C");
            this.day3.setText(getDateStringFromEpoch(forecasts.get(2).get(2)) + "\nTemperature: " + forecasts.get(2).get(0) + "°C");
            this.day4.setText(getDateStringFromEpoch(forecasts.get(3).get(2)) + "\nTemperature: " + forecasts.get(3).get(0) + "°C");
            this.day5.setText(getDateStringFromEpoch(forecasts.get(4).get(2)) + "\nTemperature: " + forecasts.get(4).get(0) + "°C");

            Picasso.get().load("http://openweathermap.org/img/wn/" + event.forecasts.get(0).get(1) + ".png").into(icon1);
            Picasso.get().load("http://openweathermap.org/img/wn/" + event.forecasts.get(1).get(1) + ".png").into(icon2);
            Picasso.get().load("http://openweathermap.org/img/wn/" + event.forecasts.get(2).get(1) + ".png").into(icon3);
            Picasso.get().load("http://openweathermap.org/img/wn/" + event.forecasts.get(3).get(1) + ".png").into(icon4);
            Picasso.get().load("http://openweathermap.org/img/wn/" + event.forecasts.get(4).get(1) + ".png").into(icon5); //TODO: Refactor this fucking mess
        }
    }
    private String getDateStringFromEpoch(String epoch){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss", Locale.US);
        Date date = new Date(Long.parseLong(epoch) * 1000);
        return df.format(date);
    }
}
