# Movie Explorer App

## Project Overview

The Movie Explorer App is an Android application developed in Java that allows users to explore movies. It integrates with the OMDb API to fetch and display movie data, provides search functionality, and shows detailed information about selected movies. Users can also view their favorite movies.

## Features

- **Movie List Display**: Fetch and display a list of movies from the OMDb API. Each list item shows the movie's title and year of release, with pagination support.
- **Search Functionality**: Allows users to search for movies by title and view search results.
- **Movie Details Screen**: Displays detailed information about a selected movie, including plot, genre, director, and cast.
- **Favorites**: Users can mark movies as favorites and view their list of favorite movies.
- **Sorting** (Optional): Implement sorting by year or title.
- **Responsive UI**: Designed to work well on both phones and tablets using modern Android UI components.

## Architecture

The app is designed using the **MVVM (Model-View-ViewModel)** architectural pattern to separate concerns and make the codebase more maintainable:

- **Model**: Represents the data layer, including data models and repository classes.
- **View**: Represents the UI components such as Activities and Fragments.
- **ViewModel**: Acts as a bridge between the Model and the View, handling business logic and data transformation.

## Dependencies

- **Dagger Hilt**: For dependency injection.
- **Retrofit**: For network requests to the OMDb API.
- **Room**: For local database operations (optional, if implementing favorites feature).
- **Gson**: For JSON parsing.

## Setup Instructions

1. **Clone the Repository**
   ```bash
   git clone https://github.com/NoorElshafei/Movie-App.git

2. **Open in Android Studio**
- Open Android Studio.
- Select `File > Open`.
- Navigate to the cloned project directory and select it.

3. **Build and Run**
- Click on the Build menu in Android Studio and select Make Project.
- To run the app, connect an Android device or start an emulator.
- Click on the Run button (green play icon) in Android Studio.

4. **Testing**
Unit tests are included for repository and ViewModel classes. To run tests, go to the Run menu and select Run 'All Tests'

## Architecture Details

### Activities

- **`MainActivity`**: The main entry point of the app where users can search for movies, view the list of movies, and navigate to movie details.

- **`MovieDetailsActivity`**: Displays detailed information about a selected movie, including plot, genre, director, cast, and poster image.

- **`FavoriteMoviesActivity`**: Shows a list of movies that the user has marked as favorites. Users can view the details of these favorite movies by selecting them.


### ViewModel

- **`MovieViewModel`**: Manages the data for the movie list, sorting, and error handling.
  - **Methods**:
    - `loadMovies(String query, int currentPage)`: Fetches movies based on the search query and page number.
    - `sortMovies(String sortOption)`: Sorts movies based on the selected option.
    - `loadNextPage(String query, int currentPage)`: Loads the next page of movies.
    - `updateFavoriteStatus(Movie movie)`: Updates the favorite status of a movie.

- **`MovieDetailsViewModel`**: Manages fetching movie details and error handling.
  - **Methods**:
    - `getMovieDetails(String imdbID)`: Fetches details for a movie using its IMDb ID.

- **`FavoriteMoviesViewModel`**: Provides access to the list of favorite movies.
  - **Methods**:
    - `getFavoriteMovies()`: Returns a LiveData object containing the list of favorite movies.

### Repository

- **`MovieRepository`**: Handles data operations for fetching movies from the OMDb API and interacting with the local database.
  - **Methods**:
    - `getMovies(String query, int page, OnMoviesFetchedListener listener)`: Fetches movies based on the search query and page number.
    - `getMovieDetails(String imdbID, OnMovieDetailsFetchedListener listener)`: Fetches movie details using its IMDb ID.
    - `updateFavoriteStatus(Movie movie)`: Updates the favorite status of a movie in the local database.
    - `getFavoriteMovies()`: Returns a LiveData object containing the list of favorite movies.

### API Integration

- **OMDb API**: Used for fetching movie data and details. Integrated using Retrofit.

  ## Testing

### Unit Tests

- **`MovieRepositoryTest`**: Tests repository methods for updating favorite status and fetching favorite movies.
  - **Example Test**:
    ```java
    @Test
    public void testUpdateFavoriteStatus_RemoveFromFavorites() throws InterruptedException {
        // Given
        Movie movie = new Movie("tt1375666", "Inception", "2010", "A mind-bending thriller", true);
        // When
        movieRepository.updateFavoriteStatus(movie);
        // Then
        verify(movieDao).deleteById("tt1375666");
    }
    ```

- **`MovieViewModelTest`**: Tests ViewModel methods for loading and sorting movies.
  - **Example Test**:
    ```java
    @Test
    public void testLoadMovies_Success() {
        // Given
        String query = "Inception";
        int currentPage = 1;
        MovieResponse response = new MovieResponse("True", "Success", Arrays.asList(new Movie("tt1375666", "Inception", "2010", "A mind-bending thriller")));
        when(movieRepository.getMovies(anyString(), anyInt(), any())).thenAnswer(invocation -> {
            OnMoviesFetchedListener listener = invocation.getArgument(2);
            listener.onFetched(response);
            return null;
        });

        // When
        movieViewModel.loadMovies(query, currentPage);

        // Then
        assertNotNull(movieViewModel.getMoviesLiveData().getValue());
        assertEquals(response, movieViewModel.getMoviesLiveData().getValue());
    }
    ```

- **`MovieDetailsViewModelTest`**: Tests ViewModel methods for fetching movie details.
  - **Example Test**:
    ```java
    @Test
    public void testGetMovieDetails_Success() {
        // Given
        String imdbID = "tt1375666";
        Movie movie = new Movie(imdbID, "Inception", "2010", "A mind-bending thriller");
        when(movieRepository.getMovieDetails(anyString(), any())).thenAnswer(invocation -> {
            OnMovieDetailsFetchedListener listener = invocation.getArgument(1);
            listener.onFetched(movie);
            return null;
        });

        // When
        movieDetailsViewModel.getMovieDetails(imdbID);

        // Then
        assertNotNull(movieDetailsViewModel.getMovieDetails().getValue());
        assertEquals(movie, movieDetailsViewModel.getMovieDetails().getValue());
    }



