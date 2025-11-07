package ru.mirea.krasikova.recyclerviewapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<HistoricalEvent> events = new ArrayList<>();
        events.add(new HistoricalEvent("Падение Римской империи",
                "476 год — падение Западной Римской империи, конец античности.",
                R.drawable.rome));
        events.add(new HistoricalEvent("Открытие Америки",
                "1492 год — Христофор Колумб достиг берегов Нового Света.",
                R.drawable.columbus));
        events.add(new HistoricalEvent("Первая мировая война",
                "1914–1918 годы — крупнейший конфликт начала XX века.",
                R.drawable.ww1));
        events.add(new HistoricalEvent("Высадка на Луну",
                "1969 год — Нил Армстронг сделал первый шаг на Луне.",
                R.drawable.moon));

        adapter = new EventAdapter(events);
        recyclerView.setAdapter(adapter);
    }
}