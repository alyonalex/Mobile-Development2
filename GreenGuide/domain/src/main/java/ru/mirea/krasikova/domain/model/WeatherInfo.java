package ru.mirea.krasikova.domain.model;

public class WeatherInfo {
    private double temperature;
    private String description;

    public WeatherInfo(double temperature, String description) {
        this.temperature = temperature;
        this.description = description;
    }

    public double getTemperature() {
        return temperature;
    }
    public String getDescription() {
        return description;
    }
}
