package com.test.weatherapp.model;

public class Clouds {
    private String all;

    public String getAll() {
        return all == null ? "Unknown" : all;
    }

    public void setAll(String all) {
        this.all = all;
    }

    @Override
    public String toString() {
        return "[all = " + getAll() + "]";
    }
}