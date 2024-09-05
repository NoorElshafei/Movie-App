package com.hitachi.movieapp;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.hitachi.movieapp.data.model.response.Movie;
import com.hitachi.movieapp.data.repository.MovieRepository;
import com.hitachi.movieapp.data.repository.OnMovieDetailsFetchedListener;
import com.hitachi.movieapp.presentation.movie_details.MovieDetailsViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MovieDetailsViewModelTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule(); // Allows LiveData to work synchronously

    @Mock
    private MovieRepository movieRepository;

    private MovieDetailsViewModel movieDetailsViewModel;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        movieDetailsViewModel = new MovieDetailsViewModel(movieRepository); // Initialize ViewModel with mocked repo
    }

    @Test
    public void testGetMovieDetails_success() {
        // Arrange: Mock the movie details and the listener callback

        Movie mockMovie =new Movie();
        mockMovie.setTitle("Inception");
        mockMovie.setYear("2010");
        mockMovie.setPoster("https://m.media-amazon.com/images/M/MV5BOTY4YjI2N2MtYmFlMC00ZjcyLTg3YjEtMDQyM2ZjYzQ5YWFkXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg");
        mockMovie.setPlot("Plot of the movie");
        mockMovie.setGenre("Sci-Fi");
        mockMovie.setActors("Leonardo DiCaprio");
        mockMovie.setDirector("Christopher Nolan");
        Mockito.doAnswer(invocation -> {
            OnMovieDetailsFetchedListener listener = invocation.getArgument(1);
            listener.onFetched(mockMovie);  // Simulate success
            return null;
        }).when(movieRepository).getMovieDetails(Mockito.anyString(), Mockito.any());

        // Act: Call the method to get movie details
        movieDetailsViewModel.getMovieDetails("tt1375666");

        // Assert: Verify that the LiveData was updated with the correct movie details
        assertEquals(mockMovie, movieDetailsViewModel.getMovieDetails("tt1375666").getValue());
    }

    @Test
    public void testGetMovieDetails_error() {
        // Arrange: Mock the repository to simulate an error
        String errorMessage = "Movie not found";

        Mockito.doAnswer(invocation -> {
            OnMovieDetailsFetchedListener listener = invocation.getArgument(1);
            listener.onError(errorMessage);  // Simulate error
            return null;
        }).when(movieRepository).getMovieDetails(Mockito.anyString(), Mockito.any());

        // Act: Call the method to get movie details
        movieDetailsViewModel.getMovieDetails("tt1375666");

        // Assert: Verify that the error LiveData was updated with the correct error message
        assertEquals(errorMessage, movieDetailsViewModel.getError().getValue());
    }
}
