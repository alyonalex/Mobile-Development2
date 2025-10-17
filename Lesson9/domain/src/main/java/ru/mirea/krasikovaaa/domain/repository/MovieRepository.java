package ru.mirea.krasikovaaa.domain.repository;

import ru.mirea.krasikovaaa.domain.models.MovieDomain;

public interface MovieRepository {
    public boolean saveMovie(MovieDomain movie);
    public MovieDomain getMovie();
}