package ru.mirea.krasikova.greenguide;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.mirea.krasikova.greenguide.R;
import ru.mirea.krasikova.data.repository.AuthRepositoryImpl;
import ru.mirea.krasikova.domain.repository.AuthRepository;
import ru.mirea.krasikova.greenguide.LoginActivity;
import ru.mirea.krasikova.greenguide.plants.PlantListActivity;
import ru.mirea.krasikova.greenguide.weather.WeatherActivity;

public class MainActivity extends AppCompatActivity {

    private TextView userStatusText;
    private Button btnLogout;
    private Button btnPlants;
    private Button btnWeather;

    private AuthRepository authRepository;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userStatusText = findViewById(R.id.userRoleText);
        btnLogout = findViewById(R.id.btnLogout);
        btnPlants = findViewById(R.id.btnPlants);
        btnWeather = findViewById(R.id.btnWeather);

        authRepository = new AuthRepositoryImpl(this);
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String userType = authRepository.getUserType();

        if (currentUser != null) {
            // Пользователь вошёл через Firebase
            userStatusText.setText("Статус: авторизованный\n" +
                    "Email: " + currentUser.getEmail());
            authRepository.saveUserType("authorized");
        } else {
            // Гостевой пользователь
            userStatusText.setText("Статус: гость");
            authRepository.saveUserType("guest");
        }

        // Переход к списку растений
        btnPlants.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PlantListActivity.class);
            startActivity(intent);
        });

        // Переход к экрану погоды
        btnWeather.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
            startActivity(intent);
        });

        // Обработка выхода
        btnLogout.setOnClickListener(v -> {
            firebaseAuth.signOut();
            authRepository.logout();
            Toast.makeText(this, "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show();

            // Возврат к экрану входа
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
