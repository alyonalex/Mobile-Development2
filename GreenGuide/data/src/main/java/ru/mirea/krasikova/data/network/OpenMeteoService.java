package ru.mirea.krasikova.data.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.mirea.krasikova.data.model.WeatherResponse;
public interface OpenMeteoService {
    @GET("v1/forecast")
    Call<WeatherResponse> getCurrentWeather(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("current_weather") boolean currentWeather
    );
}
