package ru.mirea.krasikova.domain.repository;

import javax.security.auth.callback.Callback;

import ru.mirea.krasikova.domain.model.WeatherInfo;

public interface WeatherRepository {
    void getWeatherByIp(RepositoryCallback<WeatherInfo> callback);

    interface RepositoryCallback<T> {
        void onSuccess(T weatherInfo);
        void onError(String errorMessage);
    }
}


