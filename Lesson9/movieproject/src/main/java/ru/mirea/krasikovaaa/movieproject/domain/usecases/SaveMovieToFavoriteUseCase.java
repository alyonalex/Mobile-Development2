package ru.mirea.krasikovaaa.movieproject.domain.usecases;

import ru.mirea.krasikovaaa.movieproject.domain.models.Movie;
import ru.mirea.krasikovaaa.movieproject.domain.repository.MovieRepository;
public class SaveMovieToFavoriteUseCase {
    private MovieRepository movieRepository;
    public SaveMovieToFavoriteUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
    public boolean execute(Movie movie){
        return movieRepository.saveMovie(movie);
    }
}
