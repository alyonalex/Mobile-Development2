package ru.mirea.krasikovaaa.movieproject.domain.repository;

import ru.mirea.krasikovaaa.movieproject.domain.models.Movie;

public interface MovieRepository {
    public boolean saveMovie(Movie movie);
    public Movie getMovie();
}