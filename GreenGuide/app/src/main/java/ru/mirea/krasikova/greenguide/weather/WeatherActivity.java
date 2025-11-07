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
import ru.mirea.krasikova.greenguide.weather.WeatherViewModel;
import ru.mirea.krasikova.greenguide.weather.WeatherViewModelFactory;

public class WeatherActivity extends AppCompatActivity {

    private WeatherViewModel viewModel;
    private TextView cityText;
    private TextView countryText;
    private TextView temperatureText;
    private TextView windSpeedText;
    private Button btnBackToMainMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        cityText = findViewById(R.id.cityText);
        countryText = findViewById(R.id.countryText);
        temperatureText = findViewById(R.id.temperatureText);
        windSpeedText = findViewById(R.id.windSpeedText);
        btnBackToMainMenu = findViewById(R.id.btnBackToMainMenu);

        WeatherViewModelFactory factory = new WeatherViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(WeatherViewModel.class);

        // Пример координат
        viewModel.setCoordinates(55.751244, 37.618423); // Москва

        viewModel.getWeatherLiveData().observe(this, weatherInfo -> {
            cityText.setText("Город: " + weatherInfo.getCity());
            countryText.setText("Страна: " + weatherInfo.getCountry());
            temperatureText.setText("Температура: " + weatherInfo.getTemperature() + "°C");
            windSpeedText.setText("Скорость ветра: " + weatherInfo.getWindSpeed() + " м/с");
        });

        btnBackToMainMenu.setOnClickListener(v -> {
            Intent intent = new Intent(WeatherActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}