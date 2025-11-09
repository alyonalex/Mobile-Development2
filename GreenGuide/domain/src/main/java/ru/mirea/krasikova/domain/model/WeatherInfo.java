package ru.mirea.krasikova.domain.model;

public class WeatherInfo {
    private final String city;
    private final String country;
    private final double temperature;
    private final double windSpeed;

    public WeatherInfo(String city, String country, double temperature, double windSpeed) {
        this.city = city;
        this.country = country;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
    }

    public String getCity() { return city; }
    public String getCountry() { return country; }
    public double getTemperature() { return temperature; }
    public double getWindSpeed() { return windSpeed; }
}

