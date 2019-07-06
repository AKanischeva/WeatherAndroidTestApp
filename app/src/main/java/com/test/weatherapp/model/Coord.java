package com.test.weatherapp.model;

public class Coord {
    private String lon;

    private String lat;

    public String getLon() {
        return lon == null ? "Unknown" : lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat == null ? "Unknown" : lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "[lon = " + getLon() + ", lat = " + getLat() + "]";
    }
}