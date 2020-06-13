package com.example.astroweather;

import java.util.ArrayList;
import java.util.Map;

public class ApiRespondedEvent {
    public String windSpeed;
    public String windDirection;
    public String humidity;
    public String visibility;
    public boolean isForecast;
    public Map<Integer, ArrayList<String>> forecasts;
    public ApiRespondedEvent(String windDirection, String windSpeed, String humidity, String visibility) {
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.humidity = humidity;
        this.visibility = visibility;
        this.isForecast = false;
    }
    public ApiRespondedEvent(Map<Integer, ArrayList<String>> forecasts) {
        this.forecasts = forecasts;
        this.isForecast = true;
    }
}
