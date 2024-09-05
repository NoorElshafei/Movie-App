package com.hitachi.movieapp;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hitachi.movieapp.data.model.response.Movie;
import com.hitachi.movieapp.data.repository.MovieRepository;
import com.hitachi.movieapp.presentation.favorite_movies.FavoriteMoviesViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class FavoriteMoviesViewModelTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule(); // Allows LiveData to work synchronously

    @Mock
    private MovieRepository movieRepository;

    private FavoriteMoviesViewModel favoriteMoviesViewModel;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        favoriteMoviesViewModel = new FavoriteMoviesViewModel(movieRepository); // Initialize ViewModel with mocked repo
    }

    @Test
    public void testGetFavoriteMovies() {
        // Arrange: Mock the favorite movies data
        Movie mockMovie1 =new Movie();
        mockMovie1.setTitle("Inception");
        mockMovie1.setYear("2010");
        mockMovie1.setPoster("https://m.media-amazon.com/images/M/MV5BOTY4YjI2N2MtYmFlMC00ZjcyLTg3YjEtMDQyM2ZjYzQ5YWFkXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg");
        mockMovie1.setPlot("Plot of the movie");
        mockMovie1.setGenre("Sci-Fi");
        mockMovie1.setActors("Leonardo DiCaprio");
        mockMovie1.setDirector("Christopher Nolan");

        Movie mockMovie2 =new Movie();
        mockMovie2.setTitle("Interstellar");
        mockMovie2.setYear("2014");
        mockMovie2.setPoster("https://m.media-amazon.com/images/M/MV5BOTY4YjI2N2MtYmFlMC00ZjcyLTg3YjEtMDQyM2ZjYzQ5YWFkXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg");
        mockMovie2.setPlot("Plot 2");
        mockMovie2.setGenre("Sci-Fi");
        mockMovie2.setActors("Matthew McConaughey");
        mockMovie2.setDirector("Christopher Nolan");
        List<Movie> mockFavoriteMovies = Arrays.asList(mockMovie1, mockMovie2);

        // Set up the mock repository to return a LiveData object with the favorite movies
        MutableLiveData<List<Movie>> favoriteMoviesLiveData = new MutableLiveData<>();
        favoriteMoviesLiveData.setValue(mockFavoriteMovies);
        when(movieRepository.getFavoriteMovies()).thenReturn(favoriteMoviesLiveData);

        // Act: Get the favorite movies from the ViewModel
        LiveData<List<Movie>> favoriteMovies = favoriteMoviesViewModel.getFavoriteMovies();

        // Assert: Check if the ViewModel's LiveData contains the expected favorite movies
        assertEquals(mockFavoriteMovies, favoriteMovies.getValue());
    }
}
