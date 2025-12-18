package ru.mirea.krasikova.bottomnavigationapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import ru.mirea.krasikova.bottomnavigationapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Инициализация View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // NavController для навигации между фрагментами
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        // Подключение BottomNavigationView к NavController
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
}
