package ru.mirea.krasikovaaa.domain.usecases;

import ru.mirea.krasikovaaa.domain.models.MovieDomain;
import ru.mirea.krasikovaaa.domain.repository.MovieRepository;

public class GetFavoriteFilmUseCase {
    private MovieRepository movieRepository;

    public GetFavoriteFilmUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public MovieDomain execute() {
        return movieRepository.getMovie();
    }
}