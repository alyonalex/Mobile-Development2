package ru.mirea.krasikova.greenguide.domain.repository;

public interface WeatherRepository {
    WeatherInfo getCurrentWeather(double lat, double lon);
}
