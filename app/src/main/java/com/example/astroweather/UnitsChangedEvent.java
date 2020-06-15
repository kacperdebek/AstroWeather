package com.example.astroweather;

public class UnitsChangedEvent {
    public int from;
    public int to;
    UnitsChangedEvent(int from, int to){
        this.from = from;
        this.to = to;
    }
}
