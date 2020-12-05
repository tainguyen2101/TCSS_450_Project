package edu.uw.group1app.ui.weather;

/**
 * Weather Object class
 * @author Ford Nguyen
 * @version 1.o
 */
public class Weather {

    private final String mCity;
    private final String mCondition;
    private final String mTemperature;


    public Weather(final String city, final String condition, final String temperature) {
        this.mCity = city;
        this.mCondition = condition;
        this.mTemperature = temperature;
    }

    public String getCity() {
        return this.mCity;
    }

    public String getCondition() {
        return this.mCondition;
    }

    public String getTemperature() {
        return this.mTemperature;
    }
}
