package ru.mirea.krasikova.greenguide;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ru.mirea.krasikova.greenguide.auth.LoginFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new LoginFragment())
                    .commit();
        }
    }
}
