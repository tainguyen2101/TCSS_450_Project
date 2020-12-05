package edu.uw.group1app.ui.weather;

import java.io.Serializable;

public class CurrentWeather implements Serializable {

    private final String mCity;
    private final String mTemperature;
    private final String mStatus;

    public static class Builder{
        private final String mCity;
        private final String mTemperature;
        private final String mStatus;

        public Builder(String city, String temperature, String status){
            this.mCity = city;
            this.mTemperature = temperature;
            this.mStatus = status;

        }

        public CurrentWeather build(){
            return new CurrentWeather(this);
        }

    }

    private CurrentWeather(final Builder builder){
        this.mCity = builder.mCity;
        this.mTemperature = builder.mTemperature;
        this.mStatus = builder.mStatus;
    }

    public String getCity(){
        return mCity;
    }

    public String getTemperature(){
        return mTemperature;
    }

    public String getStatus(){
        return mStatus;
    }



}
