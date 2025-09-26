package ru.mirea.krasikova.greenguide.data.repository;

import ru.mirea.krasikova.greenguide.data.test.TestDataSource;
import ru.mirea.krasikova.greenguide.domain.repository.WeatherInfo;
import ru.mirea.krasikova.greenguide.domain.repository.WeatherRepository;

public class WeatherRepositoryImpl implements WeatherRepository {
    @Override
    public WeatherInfo getCurrentWeather(double lat, double lon) {
        return TestDataSource.weather;
    }
}
