package com.example.oauth.client.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Rating {

    private double rate;

    private int count;

    public Rating() {
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
