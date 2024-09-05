package com.hitachi.movieapp.presentation.movie_details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hitachi.movieapp.data.model.response.Movie;
import com.hitachi.movieapp.data.repository.MovieRepository;
import com.hitachi.movieapp.data.repository.OnMovieDetailsFetchedListener;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * This ViewModel manages movie details retrieval for the Movie Explorer app.
 * It interacts with the MovieRepository to fetch details for a specific movie based on its IMDb ID.
 * It exposes LiveData for movie details and any errors encountered during data fetching.
 */
@HiltViewModel
public class MovieDetailsViewModel extends ViewModel {
    private final MovieRepository movieRepository;
    private final MutableLiveData<String> errorLiveData;
    MutableLiveData<Movie> movieDetails;

    /**
     * Constructor for injecting the MovieRepository dependency.
     *
     * @param movieRepository The MovieRepository instance.
     */
    @Inject
    public MovieDetailsViewModel(
            MovieRepository movieRepository
    ) {
        this.movieRepository = movieRepository;
        errorLiveData = new MutableLiveData<>();
        movieDetails = new MutableLiveData<>();
    }

    /**
     * Exposes LiveData containing any errors encountered during data fetching.
     *
     * @return LiveData for errors.
     */
    public MutableLiveData<String> getError() {
        return errorLiveData;
    }

    /**
     * Fetches movie details for the provided IMDb ID from the MovieRepository.
     *
     * @param imdbID The IMDb ID of the movie.
     * @return LiveData containing the fetched movie details or null if an error occurs.
     */
    public MutableLiveData<Movie> getMovieDetails(String imdbID) {
        movieRepository.getMovieDetails(imdbID, new OnMovieDetailsFetchedListener() {
            @Override
            public void onFetched(Movie movie) {
                movieDetails.setValue(movie);
            }

            @Override
            public void onError(String error) {
                errorLiveData.setValue(error);
            }
        });
        return movieDetails;
    }

}
