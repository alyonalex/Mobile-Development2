package ru.mirea.krasikova.data.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenMeteoApiService {
    @GET("v1/forecast?current_weather=true")
    Call<String> getCurrentWeather(
            @Query("latitude") double lat,
            @Query("longitude") double lon
    );
}
