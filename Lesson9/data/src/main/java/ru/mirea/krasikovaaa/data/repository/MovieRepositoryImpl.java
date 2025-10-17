package ru.mirea.krasikovaaa.data.repository;

import android.annotation.SuppressLint;

import java.time.LocalDate;

import ru.mirea.krasikovaaa.data.storage.MovieStorage;
import ru.mirea.krasikovaaa.data.storage.models.Movie;
import ru.mirea.krasikovaaa.domain.models.MovieDomain;
import ru.mirea.krasikovaaa.domain.repository.MovieRepository;

public class MovieRepositoryImpl implements MovieRepository {

    private final MovieStorage movieStorage;
    public MovieRepositoryImpl(MovieStorage movieStorage) {
        this.movieStorage = movieStorage;
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public boolean saveMovie(MovieDomain movie){
        Movie storageMovie = mapToStorage(movie);
        return movieStorage.save(storageMovie);
    }
    @Override
    public MovieDomain getMovie(){
        Movie movie = movieStorage.get();
        return mapToDomain(movie);
    }
    private Movie mapToStorage(MovieDomain movie){
        String name = movie.getName();
        return new Movie(2, name, LocalDate.now().toString());
    }
    private MovieDomain mapToDomain(Movie movie){
        return new MovieDomain(movie.getId(), movie.getName());
    }

}