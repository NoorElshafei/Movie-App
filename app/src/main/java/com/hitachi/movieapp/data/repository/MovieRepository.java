package com.hitachi.movieapp.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.hitachi.movieapp.BuildConfig;
import com.hitachi.movieapp.data.local.MovieDao;
import com.hitachi.movieapp.data.model.response.Movie;
import com.hitachi.movieapp.data.model.response.MovieResponse;
import com.hitachi.movieapp.data.network.OmdbApi;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Repository class that handles retrieving and managing movie data from both local (database)
 * and remote (OMDb API) sources.
 */
public class MovieRepository {
    /**
     * Interface for OMDb API interaction.
     */
    private final OmdbApi omdbApi;
    /**
     * DAO for accessing the local movie database.
     */
    private final MovieDao movieDao;
    /**
     * OMDb API key from BuildConfig.
     */
    private static final String API_KEY = BuildConfig.OMDB_API_KEY;
    private final Executor executor;

    /**
     * Constructor for injecting dependencies.
     *
     * @param movieDao The MovieDao instance.
     * @param omdbApi  The OmdbApi instance.
     */
    @Inject
    public MovieRepository(
            MovieDao movieDao,
            OmdbApi omdbApi,
            Executor executor) {
        this.movieDao = movieDao;
        this.omdbApi = omdbApi;
        this.executor = executor;

    }

    /**
     * Fetches movies from the OMDb API based on a search query and page number.
     * Uses a callback interface (`OnMoviesFetchedListener`) to notify the caller of the results.
     *
     * @param query    The search query.
     * @param page     The page number (for pagination).
     * @param listener The callback listener for receiving fetched movies or errors.
     */
    public void getMovies(String query, int page, OnMoviesFetchedListener listener) {

        omdbApi.getMovies(API_KEY, query, page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                String responseString = Objects.requireNonNull(response.body()).getResponse();
                if (response.isSuccessful() && responseString.equals("True")) {

                    listener.onFetched(response.body());
                } else {
                    listener.onError(response.body().getErrorMsg());

                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                if (t instanceof IOException) {
                    // Network or connectivity error, such as no internet connection
                    listener.onError("No internet connection");
                } else
                    listener.onError(t.getMessage());

            }
        });
    }

    /**
     * Fetches details of a specific movie using its IMDb ID from the OMDb API.
     * Uses a callback interface (`OnMovieDetailsFetchedListener`) to notify the caller of the results.
     *
     * @param imdbID   The IMDb ID of the movie to fetch details for.
     * @param listener The callback listener for receiving fetched movie details or errors.
     */
    public void getMovieDetails(String imdbID, OnMovieDetailsFetchedListener listener) {
        Call<Movie> call = omdbApi.getMovieDetails(imdbID, API_KEY);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    listener.onFetched(response.body());
                } else {
                    listener.onError("Error fetching movie details");
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });
    }

    /**
     * Updates the favorite status of a movie in the local database.
     *
     * @param movie The movie to update the favorite status for.
     */
    public void updateFavoriteStatus(Movie movie) {
        if (movie.isFavorite())
            executor.execute(() -> movieDao.deleteById(movie.getImdbID()));
        else
            executor.execute(() -> movieDao.insert(movie));
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return movieDao.getFavoriteMovies();
    }


}
