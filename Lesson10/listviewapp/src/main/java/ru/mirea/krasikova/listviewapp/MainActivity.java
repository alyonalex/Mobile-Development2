package ru.mirea.krasikova.listviewapp;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private ListView listViewCountries;

    private String[] countries = new String[] {
            "Австралия", "Австрия", "Аргентина", "Бангладеш", "Белорусь", "Венгрия", "Великобритания",
            "Германия", "Гонконг", "Грузия", "Дания", "Египет", "Израиль", "Испания",
            "Италия", "Канада", "Марокко", "Норвегия", "ОАЭ", "Португалия", "Российская Федерация",
            "Сингапур", "Турция", "Уругвай", "Франция", "Хорватия", "Чехия", "Эквадор", "Япония"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewCountries = findViewById(R.id.listViewCountries);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_2,
                android.R.id.text1,
                countries
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);

                text2.setText(getItem(position).toString());
                text1.setText(String.valueOf(position+1));
                return view;
            }
        };

        listViewCountries.setAdapter(adapter);
    }
}