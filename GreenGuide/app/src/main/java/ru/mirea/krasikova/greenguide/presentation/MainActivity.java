package ru.mirea.krasikova.greenguide.presentation;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import ru.mirea.krasikova.greenguide.R;
import ru.mirea.krasikova.greenguide.data.repository.PlantRepositoryImpl;
import ru.mirea.krasikova.greenguide.domain.model.Plant;
import ru.mirea.krasikova.greenguide.domain.usecases.AddPlantUseCase;
import ru.mirea.krasikova.greenguide.domain.usecases.GetPlantDetailUseCase;
import ru.mirea.krasikova.greenguide.domain.usecases.GetPlantsUseCase;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "GreenGuideTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PlantRepositoryImpl repository = new PlantRepositoryImpl();

        GetPlantsUseCase getPlantsUseCase = new GetPlantsUseCase(repository);
        GetPlantDetailUseCase getPlantDetailUseCase = new GetPlantDetailUseCase(repository);
        AddPlantUseCase addPlantUseCase = new AddPlantUseCase(repository);

        //Вывод списка растений
        Log.d(TAG, "=== Список растений ===");
        List<Plant> plants = getPlantsUseCase.execute();
        for (Plant p : plants) {
            Log.d(TAG, p.getId() + ": " + p.getName());
        }

        //Добавление нового растения
        Plant newPlant = new Plant(
                3,
                "Роза",
                "Декоративный кустарник с крупными ароматными цветками разнообразной окраски и со стеблями, покрытыми шипами.",
                "https://avatars.mds.yandex.net/i?id=5cb2a58847bdf31e60b0927d075b4a2ee34d958b-3939836-images-thumbs&n=13",
                "2025-09-25"
        );
        addPlantUseCase.execute(newPlant);

        //Повторная проверка списка
        Log.d(TAG, "=== После добавления ===");
        for (Plant p : getPlantsUseCase.execute()) {
            Log.d(TAG, p.getId() + ": " + p.getName());
        }

        //Поиск растения по его id ---
        Plant found = getPlantDetailUseCase.execute(3);
        if (found != null) {
            Log.d(TAG, "Найдено растение: " + found.getName() + " - " + found.getDescription());
        } else {
            Log.d(TAG, "Растение не найдено");
        }
    }
}