package ru.mirea.krasikova.greenguide.plants;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import ru.mirea.krasikova.greenguide.MainActivity;
import ru.mirea.krasikova.greenguide.R;

public class PlantDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView nameText;
    private TextView descriptionText;
    private TextView dateText;
    private PlantDetailViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detail);

        imageView = findViewById(R.id.imagePlant);
        nameText = findViewById(R.id.textName);
        descriptionText = findViewById(R.id.textDescription);
        dateText = findViewById(R.id.textDate);

        int plantId = getIntent().getIntExtra("plant_id", -1);
        if (plantId == -1) {
            finish();
            return;
        }

        PlantDetailViewModelFactory factory = new PlantDetailViewModelFactory(getApplication());
        viewModel = new ViewModelProvider(this, factory).get(PlantDetailViewModel.class);

        // Подписка на LiveData
        viewModel.getPlantLiveData().observe(this, plant -> {
            if (plant != null) {
                nameText.setText(plant.getName());
                descriptionText.setText(plant.getDescription());
                dateText.setText("Добавлено: " + plant.getDateAdded());

                Glide.with(this)
                        .load(plant.getImageUrl())
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(imageView);
            }
        });

        // Запрос данных
        viewModel.loadPlant(plantId);

        Button backButton = findViewById(R.id.btnBackToMainMenu);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(PlantDetailActivity.this, PlantListActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
