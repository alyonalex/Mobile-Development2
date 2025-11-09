package ru.mirea.krasikova.data.model;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {
    public CurrentWeather current_weather;

    public static class CurrentWeather {
        public double temperature;
        public double windspeed;
    }
}

