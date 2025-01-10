package org.example.shared;

import org.example.shared.enums.RequestType;

public class WeatherRequest extends Request {
    private String weatherData;

    public WeatherRequest(RequestType requestType) {
        if (requestType != RequestType.GET_WEATHER && requestType != RequestType.UPDATE_WEATHER) {
            throw new IllegalArgumentException("RequestType must be weather related");
        }
        super(requestType);
    }

    public String getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(String weatherData) {
        this.weatherData = weatherData;
    }
}
