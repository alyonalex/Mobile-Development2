package ru.mirea.krasikova.domain.usecases;

import ru.mirea.krasikova.domain.model.WeatherInfo;
import ru.mirea.krasikova.domain.repository.WeatherRepository;

public class GetWeatherUseCase {
    private final WeatherRepository repository;

    public GetWeatherUseCase(WeatherRepository repository) {
        this.repository = repository;
    }

    public WeatherInfo execute(double lat, double lon) {
        return repository.getCurrentWeather(lat, lon);
    }
}
