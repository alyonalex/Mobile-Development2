package ru.mirea.krasikova.greenguide.plants;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.mirea.krasikova.data.storage.SharedPrefsUserStorage;
import ru.mirea.krasikova.greenguide.MainActivity;
import ru.mirea.krasikova.greenguide.R;

public class PlantListActivity extends AppCompatActivity {

    private PlantListViewModel viewModel;
    private PlantAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_list);

        SharedPrefsUserStorage userStorage = new SharedPrefsUserStorage(this);
        String userType = userStorage.getUserType();

        RecyclerView recyclerView = findViewById(R.id.plantsRecycler);
        adapter = new PlantAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        PlantListViewModelFactory factory = new PlantListViewModelFactory(this);
        viewModel = new ViewModelProvider(this, factory).get(PlantListViewModel.class);

        viewModel.getPlantsLiveData().observe(this, plants -> {
            adapter.submitList(plants);
        });

        //Просмотр конкретного растения
        adapter.setOnItemClickListener(plant -> {
            Intent intent = new Intent(PlantListActivity.this, PlantDetailActivity.class);
            intent.putExtra("plant_id", plant.getId());
            startActivity(intent);
        });


        FloatingActionButton addButton = findViewById(R.id.addPlantButton);
        if (!userType.equals("authorized")) {
            addButton.setVisibility(View.GONE);
        }

        //переход к экрану добавления
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(PlantListActivity.this, AddPlantActivity.class);
            startActivity(intent);
        });


        Button backButton = findViewById(R.id.btnBackToMainMenu);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(PlantListActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.loadPlants(); // обновить список после добавления нового растения
    }
}