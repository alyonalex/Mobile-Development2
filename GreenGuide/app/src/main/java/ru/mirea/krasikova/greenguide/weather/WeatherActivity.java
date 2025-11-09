package ru.mirea.krasikova.greenguide.weather;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.krasikova.greenguide.MainActivity;
import ru.mirea.krasikova.greenguide.R;
import ru.mirea.krasikova.greenguide.plants.PlantListActivity;
import ru.mirea.krasikova.greenguide.weather.WeatherViewModel;
import ru.mirea.krasikova.greenguide.weather.WeatherViewModelFactory;

public class WeatherActivity extends AppCompatActivity {

    private WeatherViewModel viewModel;
    private TextView cityText;
    private TextView countryText;
    private TextView temperatureText;
    private TextView windSpeedText;
    private TextView errorText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        cityText = findViewById(R.id.cityText);
        countryText = findViewById(R.id.countryText);
        temperatureText = findViewById(R.id.temperatureText);
        windSpeedText = findViewById(R.id.windSpeedText);
        errorText = findViewById(R.id.errorText);


        WeatherViewModelFactory factory = new WeatherViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(WeatherViewModel.class);


        viewModel.getWeatherLiveData().observe(this, weatherInfo -> {
            if (weatherInfo != null) {
                cityText.setText("Город: " + weatherInfo.getCity());
                countryText.setText("Страна: " + weatherInfo.getCountry());
                temperatureText.setText(String.format("Температура: %.1f°C", weatherInfo.getTemperature()));
                windSpeedText.setText(String.format("Ветер: %.1f м/с", weatherInfo.getWindSpeed()));
            }
        });


        viewModel.getErrorLiveData().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                errorText.setText("Ошибка: " + errorMessage);
            } else {
                errorText.setText("");
            }
        });


        viewModel.loadWeatherByIp();

        Button backButton = findViewById(R.id.btnBackToMainMenu);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(WeatherActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
