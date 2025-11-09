package ru.mirea.krasikova.greenguide.plants;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.krasikova.greenguide.R;

public class AddPlantActivity extends AppCompatActivity {

    private AddPlantViewModel viewModel;
    private EditText nameInput, descriptionInput, imageUrlInput;
    private Button saveButton, cancelButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        nameInput = findViewById(R.id.inputPlantName);
        descriptionInput = findViewById(R.id.inputPlantDescription);
        imageUrlInput = findViewById(R.id.inputPlantImageUrl);
        saveButton = findViewById(R.id.btnSavePlant);
        cancelButton = findViewById(R.id.btnCancel);

        AddPlantViewModelFactory factory = new AddPlantViewModelFactory(this);
        viewModel = new ViewModelProvider(this, factory).get(AddPlantViewModel.class);

        saveButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String description = descriptionInput.getText().toString().trim();
            String imageUrl = imageUrlInput.getText().toString().trim();

            if (name.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.addPlant(name, description, imageUrl);

            Toast.makeText(this, "Растение добавлено!", Toast.LENGTH_SHORT).show();
            finish();
        });

        cancelButton.setOnClickListener(v -> finish());
    }
}
