package com.example.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Rating {
    @JsonProperty("rate")
    private double rate;

    @JsonProperty("count")
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
