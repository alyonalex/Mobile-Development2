package ru.mirea.krasikova.greenguide;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import ru.mirea.krasikova.data.repository.AuthRepositoryImpl;
import ru.mirea.krasikova.domain.repository.AuthRepository;
import ru.mirea.krasikova.greenguide.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authRepository = new AuthRepositoryImpl(this);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        // Подключение BottomNavigationView к NavController
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);

        // Проверка текущего фрагмента, чтобы скрыть BottomNavigation для авторизации и регистрации
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int destId = destination.getId();
            if (destId == R.id.loginFragment || destId == R.id.registerFragment) {
                binding.bottomNavigation.setVisibility(View.GONE);
            } else {
                binding.bottomNavigation.setVisibility(View.VISIBLE);
            }
        });

        // Проверка доступа к AddPlantFragment
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.addPlantFragment) {
                String userType = authRepository.getUserType();
                if (!"authorized".equals(userType)) {
                    android.widget.Toast.makeText(this,
                            "Только авторизованные пользователи могут добавлять растения",
                            android.widget.Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            NavigationUI.onNavDestinationSelected(item, navController);
            return true;
        });
    }
}
