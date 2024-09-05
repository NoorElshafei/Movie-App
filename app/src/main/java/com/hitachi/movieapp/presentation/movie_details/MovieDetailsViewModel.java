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

@HiltViewModel
public class MovieDetailsViewModel extends ViewModel {
    private final MovieRepository movieRepository;
    private final MutableLiveData<String> errorLiveData;
    MutableLiveData<Movie> movieDetails;

    @Inject
    public MovieDetailsViewModel(
            MovieRepository movieRepository
    ) {
        this.movieRepository = movieRepository;
        errorLiveData = new MutableLiveData<>();
        movieDetails = new MutableLiveData<>();
    }


    public LiveData<String> getError() {
        return errorLiveData;
    }


    public LiveData<Movie> getMovieDetails(String imdbID) {
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
