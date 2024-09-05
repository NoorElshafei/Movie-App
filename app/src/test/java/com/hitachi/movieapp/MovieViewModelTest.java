package com.hitachi.movieapp;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.hitachi.movieapp.data.model.response.Movie;
import com.hitachi.movieapp.data.model.response.MovieResponse;
import com.hitachi.movieapp.data.repository.MovieRepository;
import com.hitachi.movieapp.data.repository.OnMoviesFetchedListener;
import com.hitachi.movieapp.presentation.movies_list.MovieViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MovieViewModelTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();  // Allows LiveData to work synchronously

    @Mock
    private MovieRepository movieRepository;

    private MovieViewModel movieViewModel;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        movieViewModel = new MovieViewModel(movieRepository);  // Initialize ViewModel with mocked repo
    }

    @Test
    public void testLoadMovies_success() {
        // Arrange: Mock the movie response and the listener callback
        MovieResponse mockMovieResponse = new MovieResponse();
        List<Movie> mockMovieList = new ArrayList<>();
        Movie movie =new Movie();
        movie.setTitle("Inception");
        movie.setYear("2010");
        movie.setPoster("https://m.media-amazon.com/images/M/MV5BOTY4YjI2N2MtYmFlMC00ZjcyLTg3YjEtMDQyM2ZjYzQ5YWFkXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg");
        mockMovieList.add(movie);
        mockMovieResponse.setMovies(mockMovieList);

        Mockito.doAnswer(invocation -> {
            OnMoviesFetchedListener listener = invocation.getArgument(2);
            listener.onFetched(mockMovieResponse);  // Simulate success
            return null;
        }).when(movieRepository).getMovies(Mockito.anyString(), Mockito.anyInt(), Mockito.any());

        // Act: Call the method to load movies
        movieViewModel.loadMovies("Inception", 1);

        // Assert: Verify that the LiveData was updated with the correct movie data
        assertEquals(mockMovieResponse, movieViewModel.getMoviesLiveData().getValue());
    }

    @Test
    public void testLoadMovies_error() {
        // Arrange: Mock the repository to simulate an error
        String errorMessage = "API Error";
        Mockito.doAnswer(invocation -> {
            OnMoviesFetchedListener listener = invocation.getArgument(2);
            listener.onError(errorMessage);  // Simulate error
            return null;
        }).when(movieRepository).getMovies(Mockito.anyString(), Mockito.anyInt(), Mockito.any());

        // Act: Call the method to load movies
        movieViewModel.loadMovies("Inception", 1);

        // Assert: Verify that the error LiveData was updated with the correct error message
        assertEquals(errorMessage, movieViewModel.getError().getValue());
    }

    @Test
    public void testSortMovies_byTitle() {
        // Arrange: Mock movie data
        List<Movie> mockMovieList = new ArrayList<>();
        Movie movie =new Movie();
        movie.setTitle("Inception");
        movie.setYear("2010");
        movie.setPoster("https://m.media-amazon.com/images/M/MV5BOTY4YjI2N2MtYmFlMC00ZjcyLTg3YjEtMDQyM2ZjYzQ5YWFkXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg");
        Movie movie1 =new Movie();
        movie1.setTitle("Avatar");
        movie1.setYear("2009");
        movie1.setPoster("https://m.media-amazon.com/images/M/MV5BZDA0OGQxNTItMDZkMC00N2UyLTg3MzMtYTJmNjg3Nzk5MzRiXkEyXkFqcGdeQXVyMjUzOTY1NTc@._V1_SX300.jpg");

        mockMovieList.add(movie);
        mockMovieList.add(movie1);

        MovieResponse mockMovieResponse = new MovieResponse();
        mockMovieResponse.setMovies(mockMovieList);

        MutableLiveData<MovieResponse> moviesLiveData = new MutableLiveData<>();
        moviesLiveData.setValue(mockMovieResponse);
        
        // Set the ViewModel LiveData
        movieViewModel.getMoviesLiveData().setValue(mockMovieResponse);

        // Act: Sort movies by title
        movieViewModel.sortMovies("Sort by Title");

        // Assert: Verify that the movies are sorted by title
        List<Movie> sortedMovies = movieViewModel.getSortedMovies().getValue();
        assertEquals("Avatar", sortedMovies.get(0).getTitle());
        assertEquals("Inception", sortedMovies.get(1).getTitle());
    }

    @Test
    public void testSortMovies_byYear() {
        // Arrange: Mock movie data
        List<Movie> mockMovieList = new ArrayList<>();
        Movie movie =new Movie();
        movie.setTitle("Inception");
        movie.setYear("2010");
        movie.setPoster("https://m.media-amazon.com/images/M/MV5BOTY4YjI2N2MtYmFlMC00ZjcyLTg3YjEtMDQyM2ZjYzQ5YWFkXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg");
        Movie movie1 =new Movie();
        movie1.setTitle("Avatar");
        movie1.setYear("2009");
        movie1.setPoster("https://m.media-amazon.com/images/M/MV5BZDA0OGQxNTItMDZkMC00N2UyLTg3MzMtYTJmNjg3Nzk5MzRiXkEyXkFqcGdeQXVyMjUzOTY1NTc@._V1_SX300.jpg");

        mockMovieList.add(movie);
        mockMovieList.add(movie1);
        MovieResponse mockMovieResponse = new MovieResponse();
        mockMovieResponse.setMovies(mockMovieList);

        MutableLiveData<MovieResponse> moviesLiveData = new MutableLiveData<>();
        moviesLiveData.setValue(mockMovieResponse);
        
        // Set the ViewModel LiveData
        movieViewModel.getMoviesLiveData().setValue(mockMovieResponse);

        // Act: Sort movies by year
        movieViewModel.sortMovies("Sort by Year");

        // Assert: Verify that the movies are sorted by year
        List<Movie> sortedMovies = movieViewModel.getSortedMovies().getValue();
        assertEquals("Avatar", sortedMovies.get(0).getTitle());
        assertEquals("Inception", sortedMovies.get(1).getTitle());
    }

    @Test
    public void testUpdateFavoriteStatus() {
        // Arrange: Mock a movie
        Movie mockMovie =new Movie();
        mockMovie.setTitle("Inception");
        mockMovie.setYear("2010");
        mockMovie.setPoster("https://m.media-amazon.com/images/M/MV5BOTY4YjI2N2MtYmFlMC00ZjcyLTg3YjEtMDQyM2ZjYzQ5YWFkXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg");

        // Act: Call the method to update favorite status
        movieViewModel.updateFavoriteStatus(mockMovie);

        // Assert: Verify that the repository method was called
        verify(movieRepository).updateFavoriteStatus(mockMovie);
    }
}
