package com.example.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeoLocation {
    @JsonProperty("lat")
    private double latitude;

    @JsonProperty("long")
    private double longitude;

    public GeoLocation() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
