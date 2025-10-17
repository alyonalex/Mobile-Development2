package ru.mirea.krasikova.domain.repository;

import ru.mirea.krasikova.domain.model.WeatherInfo;

public interface WeatherRepository {
    WeatherInfo getCurrentWeather(double lat, double lon);
}
