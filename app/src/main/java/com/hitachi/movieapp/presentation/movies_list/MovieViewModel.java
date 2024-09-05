package com.hitachi.movieapp.presentation.movies_list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hitachi.movieapp.data.model.response.Movie;
import com.hitachi.movieapp.data.model.response.MovieResponse;
import com.hitachi.movieapp.data.repository.MovieRepository;
import com.hitachi.movieapp.data.repository.OnMoviesFetchedListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MovieViewModel extends ViewModel {
    /**
     * Repository instance for accessing movie data.
     */
    private final MovieRepository movieRepository;

    /**
     * MutableLiveData for fetched movies and their details.
     */
    private final MutableLiveData<MovieResponse> moviesLiveData;

    /**
     * MutableLiveData for sorted movies based on user selection.
     */
    private  MutableLiveData<List<Movie>> sortedMoviesLiveData;

    /**
     * MutableLiveData for any errors encountered during data fetching.
     */
    private final MutableLiveData<String> errorLiveData;

    /**
     * LiveData from the MovieRepository containing favorite movies.
     */
    private final LiveData<List<Movie>> favoriteMovies;

    /**
     * Constructor for injecting the MovieRepository dependency.
     *
     * @param movieRepository The MovieRepository instance.
     */
    @Inject
    public MovieViewModel(
            MovieRepository movieRepository
    ) {
        this.movieRepository = movieRepository;
        moviesLiveData = new MutableLiveData<>();
        sortedMoviesLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
        this.favoriteMovies = movieRepository.getFavoriteMovies();

    }
    /**
     * Exposes LiveData containing favorite movies.
     *
     * @return LiveData for favorite movies.
     */
    public LiveData<List<Movie>> getFavoriteMovies() {
        return favoriteMovies;
    }

    /**
     * Exposes MutableLiveData containing fetched movies and their details.
     *
     * @return MutableLiveData for movies data.
     */
    public MutableLiveData<MovieResponse> getMoviesLiveData() {
        return moviesLiveData;
    }

    /**
     * Exposes MutableLiveData containing sorted movies based on user selection.
     *
     * @return LiveData for sorted movies.
     */
    public MutableLiveData<List<Movie>> getSortedMovies() {
        return sortedMoviesLiveData;
    }

    /**
     * Exposes MutableLiveData containing any errors encountered during data fetching.
     *
     * @return MutableLiveData for errors.
     */
    public MutableLiveData<String> getError() {
        return errorLiveData;
    }

    /**
     * Fetches movies from the OMDb API using the MovieRepository.
     *
     * @param query The search query for movies.
     * @param currentPage The current page number for pagination.
     */
    public void loadMovies(String query, int currentPage) {
        movieRepository.getMovies(query, currentPage, new OnMoviesFetchedListener() {
            @Override
            public void onFetched(MovieResponse movieResponse) {
                moviesLiveData.setValue(movieResponse);
            }

            @Override
            public void onError(String error) {
                errorLiveData.setValue(error);
            }
        });
    }

    /**
     * Sorts the fetched movies based on the provided sort option.
     *
     * @param sortOption The user-selected sort option (e.g., "Sort by Year", "Sort by Title").
     */
    public void sortMovies(String sortOption) {
        List<Movie> sortedMovies = new ArrayList<>(moviesLiveData.getValue().getMovies());
        if (sortOption.equals("Sort by Year")) {
            sortedMovies.sort(Comparator.comparing(Movie::getYear));
        } else if (sortOption.equals("Sort by Title")) {
            sortedMovies.sort(Comparator.comparing(Movie::getTitle));
        }
        sortedMoviesLiveData.setValue(sortedMovies);
    }

    public void loadNextPage(String query, int currentPage) {
        loadMovies(query, currentPage);
    }

    public void updateFavoriteStatus(Movie movie) {
        movieRepository.updateFavoriteStatus(movie);
    }
}
