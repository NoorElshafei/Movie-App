package com.hitachi.movieapp;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hitachi.movieapp.data.local.MovieDao;
import com.hitachi.movieapp.data.model.response.Movie;
import com.hitachi.movieapp.data.model.response.MovieResponse;
import com.hitachi.movieapp.data.network.OmdbApi;
import com.hitachi.movieapp.data.repository.MovieRepository;
import com.hitachi.movieapp.data.repository.OnMovieDetailsFetchedListener;
import com.hitachi.movieapp.data.repository.OnMoviesFetchedListener;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class MovieRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private OmdbApi omdbApi;

    @Mock
    private MovieDao movieDao;

    @Mock
    private Call<MovieResponse> movieResponseCall;

    @Mock
    private Call<Movie> movieDetailsCall;

    @InjectMocks
    private MovieRepository movieRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Executor immediateExecutor = Runnable::run;  // Synchronous execution
        movieRepository = new MovieRepository(movieDao, omdbApi, immediateExecutor);
    }

    @Test
    public void testGetMovies_Success() {
        // Arrange
        MovieResponse movieResponse = new MovieResponse();
        movieResponse.setResponse("True");
        when(omdbApi.getMovies(anyString(), anyString(), anyInt())).thenReturn(movieResponseCall);

        doAnswer(invocation -> {
            Callback<MovieResponse> callback = invocation.getArgument(0);
            callback.onResponse(movieResponseCall, Response.success(movieResponse));
            return null;
        }).when(movieResponseCall).enqueue(any());

        OnMoviesFetchedListener listener = mock(OnMoviesFetchedListener.class);

        // Act
        movieRepository.getMovies("Inception", 1, listener);

        // Assert
        verify(listener).onFetched(movieResponse);
    }

    @Test
    public void testGetMovies_Failure_NoInternet() {
        // Arrange
        when(omdbApi.getMovies(anyString(), anyString(), anyInt())).thenReturn(movieResponseCall);

        doAnswer(invocation -> {
            Callback<MovieResponse> callback = invocation.getArgument(0);
            callback.onFailure(movieResponseCall, new IOException("No internet connection"));
            return null;
        }).when(movieResponseCall).enqueue(any());

        OnMoviesFetchedListener listener = mock(OnMoviesFetchedListener.class);

        // Act
        movieRepository.getMovies("Inception", 1, listener);

        // Assert
        verify(listener).onError("No internet connection");
    }

    @Test
    public void testGetMovieDetails_Success() {
        // Arrange
        Movie mockMovie =new Movie();
        mockMovie.setTitle("Inception");
        mockMovie.setYear("2010");
        mockMovie.setPoster("https://m.media-amazon.com/images/M/MV5BOTY4YjI2N2MtYmFlMC00ZjcyLTg3YjEtMDQyM2ZjYzQ5YWFkXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg");
        mockMovie.setPlot("Plot of the movie");
        mockMovie.setGenre("Sci-Fi");
        mockMovie.setActors("Leonardo DiCaprio");
        mockMovie.setDirector("Christopher Nolan");
        when(omdbApi.getMovieDetails(anyString(), anyString())).thenReturn(movieDetailsCall);

        doAnswer(invocation -> {
            Callback<Movie> callback = invocation.getArgument(0);
            callback.onResponse(movieDetailsCall, Response.success(mockMovie));
            return null;
        }).when(movieDetailsCall).enqueue(any());

        OnMovieDetailsFetchedListener listener = mock(OnMovieDetailsFetchedListener.class);

        // Act
        movieRepository.getMovieDetails("tt1375666", listener);

        // Assert
        verify(listener).onFetched(mockMovie);
    }

    @Test
    public void testGetMovieDetails_Failure() {
        // Arrange
        when(omdbApi.getMovieDetails(anyString(), anyString())).thenReturn(movieDetailsCall);

        doAnswer(invocation -> {
            Callback<Movie> callback = invocation.getArgument(0);
            callback.onFailure(movieDetailsCall, new IOException("Error fetching movie details"));
            return null;
        }).when(movieDetailsCall).enqueue(any());

        OnMovieDetailsFetchedListener listener = mock(OnMovieDetailsFetchedListener.class);

        // Act
        movieRepository.getMovieDetails("tt1375666", listener);

        // Assert
        verify(listener).onError("Error fetching movie details");
    }

    @Test
    public void testUpdateFavoriteStatus_MarkAsFavorite() {
        // Arrange
        Movie mockMovie =new Movie();
        mockMovie.setTitle("Inception");
        mockMovie.setImdbID("tt1375666");
        mockMovie.setYear("2010");
        mockMovie.setPoster("https://m.media-amazon.com/images/M/MV5BOTY4YjI2N2MtYmFlMC00ZjcyLTg3YjEtMDQyM2ZjYzQ5YWFkXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg");
        mockMovie.setPlot("Plot of the movie");
        mockMovie.setGenre("Sci-Fi");
        mockMovie.setActors("Leonardo DiCaprio");
        mockMovie.setDirector("Christopher Nolan");
        mockMovie.setFavorite(false);

        // Act
        movieRepository.updateFavoriteStatus(mockMovie);

        // Assert
        verify(movieDao).insert(mockMovie);
    }

    @Test
    public void testUpdateFavoriteStatus_RemoveFromFavorites() {
        // Arrange
        Movie mockMovie =new Movie();
        mockMovie.setTitle("Inception");
        mockMovie.setImdbID("tt1375666");
        mockMovie.setYear("2010");
        mockMovie.setPoster("https://m.media-amazon.com/images/M/MV5BOTY4YjI2N2MtYmFlMC00ZjcyLTg3YjEtMDQyM2ZjYzQ5YWFkXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg");
        mockMovie.setPlot("Plot of the movie");
        mockMovie.setGenre("Sci-Fi");
        mockMovie.setActors("Leonardo DiCaprio");
        mockMovie.setDirector("Christopher Nolan");
        mockMovie.setFavorite(true);

        // Act
        movieRepository.updateFavoriteStatus(mockMovie);

        // Assert
        verify(movieDao).deleteById(mockMovie.getImdbID());
    }

    @Test
    public void testGetFavoriteMovies() {
        // Arrange
        MutableLiveData<List<Movie>> favoriteMoviesLiveData = new MutableLiveData<>(new ArrayList<>());
        when(movieDao.getFavoriteMovies()).thenReturn(favoriteMoviesLiveData);

        // Act
        LiveData<List<Movie>> favoriteMovies = movieRepository.getFavoriteMovies();

        // Assert
        assertEquals(favoriteMoviesLiveData, favoriteMovies);
    }
}
