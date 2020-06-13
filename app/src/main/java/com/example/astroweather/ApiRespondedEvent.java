package com.example.astroweather;

public class ApiRespondedEvent {
    public String windSpeed;
    public String windDirection;
    public String humidity;
    public String visibility;

    public ApiRespondedEvent(String windDirection, String windSpeed, String humidity, String visibility) {
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.humidity = humidity;
        this.visibility = visibility;
    }
}
