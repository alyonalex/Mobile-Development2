package ru.mirea.krasikovaaa.lesson9;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ru.mirea.krasikovaaa.lesson9.R;
import ru.mirea.krasikovaaa.data.repository.MovieRepositoryImpl;
import ru.mirea.krasikovaaa.data.storage.sharedprefs.SharedPrefMovieStorage;
import ru.mirea.krasikovaaa.domain.repository.MovieRepository;
import ru.mirea.krasikovaaa.domain.usecases.GetFavoriteFilmUseCase;
import ru.mirea.krasikovaaa.domain.usecases.SaveMovieToFavoriteUseCase;
import ru.mirea.krasikovaaa.domain.models.MovieDomain;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText text = findViewById(R.id.editTextMovie);
        TextView textView = findViewById(R.id.textViewMovie);

        SharedPrefMovieStorage sharedPrefMovieStorage = new SharedPrefMovieStorage(this);
        MovieRepository movieRepository = new MovieRepositoryImpl(sharedPrefMovieStorage);

        findViewById(R.id.buttonSaveMovie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean result = new
                        SaveMovieToFavoriteUseCase(movieRepository).execute(new MovieDomain(2,
                        text.getText().toString()));
                textView.setText(String.format("Save result %s", result));
            }
        });
        findViewById(R.id.buttonGetMovie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MovieDomain moview = new GetFavoriteFilmUseCase(movieRepository).execute();
                textView.setText(String.format("Save result %s", moview.getName()));
            }
        });
    }
}