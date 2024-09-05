package com.hitachi.movieapp.presentation.movies_list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Toast;

import com.hitachi.movieapp.data.local.MovieDao;
import com.hitachi.movieapp.data.model.response.Movie;
import com.hitachi.movieapp.databinding.ActivityMainBinding;
import com.hitachi.movieapp.presentation.adapter.MovieAdapter;
import com.hitachi.movieapp.presentation.favorite_movies.FavoriteMoviesActivity;
import com.hitachi.movieapp.presentation.movie_details.MovieDetailsActivity;
import com.hitachi.movieapp.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements
        OnMovieClickListener, OnFavoriteClickListener {
    private MovieViewModel movieViewModel;
    private MovieAdapter adapter;
    private boolean isLoading = false;
    private int currentPage = 1;
    private Integer totalPages = 0;
    private boolean isMoviesExist = false;
    private String movieName = "batman";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.hitachi.movieapp.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        adapter = new MovieAdapter(new ArrayList<>(), this, this);
        binding.recyclerViewMovies.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewMovies.setAdapter(adapter);

        // Setup SwipeRefreshLayout
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            // Reload the data here
            binding.spinnerSort.setSelection(0);
            if (movieName.isEmpty())
                movieName = "batman";
            movieViewModel.removeMovies();
            adapter.removeMovies();
            currentPage = 1;
            loadNextPage();
        });

        binding.searchCardView.setOnClickListener(view1 -> {
            binding.searchView.setIconified(false); // Expand the SearchView
            binding.searchView.requestFocus(); // Request focus
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(binding.searchView, InputMethodManager.SHOW_IMPLICIT); // Show the keyboard
        });


        binding.spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSortOption = parent.getItemAtPosition(position).toString();
                if (isMoviesExist && !selectedSortOption.equals("Select sort"))
                    movieViewModel.sortMovies(selectedSortOption);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        binding.fav.setOnClickListener(view1 -> {
            Intent intent = new Intent(MainActivity.this, FavoriteMoviesActivity.class);
            startActivity(intent);
        });

        // Set up the search query listener
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform the search when the user submits the query
                loadMovies(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                movieName = newText;
                return false;
            }
        });

        movieViewModel.getSortedMovies().observe(this, movies -> {
            adapter.removeMovies();
            adapter.updateMovies(movies);
        });

        movieViewModel.getMoviesLiveData().observe(this, movieResponse -> {
            List<Movie> movies = movieResponse.getMovies();
            totalPages = Integer.parseInt(movieResponse.getTotalResults()) / 10;

            if (movies != null) {
                isMoviesExist = true;
                adapter.removeLoadingFooter();
                isLoading = false;

                movieViewModel.getFavoriteMovies().observe(this, favMovies -> {
                    for (int i = 0; i < movies.size(); i++)
                        for (Movie favMovie : favMovies) {
                            if (favMovie.getImdbID().equals(movies.get(i).getImdbID())) {
                                movies.get(i).setFavorite(true);
                                adapter.editMovie(i);
                            }
                        }

                });
                if (currentPage == 1) {
                    adapter.updateMovies(movies);
                } else {
                    adapter.addMovies(movies);
                }
                // Stop the refreshing animation
                binding.swipeRefreshLayout.setRefreshing(false);
            } else {
                binding.swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(MainActivity.this, "Failed to load movies", Toast.LENGTH_SHORT).show();
            }
        });

        movieViewModel.getError().observe(this, errorMsg -> {
            adapter.removeLoadingFooter();
            binding.swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        });

        // Set the scroll listener for pagination
        binding.recyclerViewMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading && linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == adapter.getItemCount() - 1) {
                    // Load more data when reaching the last item
                    if (currentPage < totalPages) {
                        currentPage++;
                        loadNextPage();
                    }
                }
            }
        });

        loadNextPage();

    }

    private void loadMovies(String searchText) {
        if (NetworkUtils.isInternetAvailable(this)) {
            movieName = searchText;
            isLoading = true;
            currentPage = 1;
            adapter.removeMovies();
            adapter.addLoadingFooter();
            movieViewModel.loadNextPage(searchText, currentPage);
        } else {
            adapter.removeLoadingFooter();
            Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadNextPage() {
        isLoading = true;
        adapter.addLoadingFooter();
        if (NetworkUtils.isInternetAvailable(this))
            movieViewModel.loadNextPage(movieName, currentPage);
        else {
            adapter.removeLoadingFooter();
            Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onMovieClick(Movie movie) {
        Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
        intent.putExtra("imdbID", movie.getImdbID());
        startActivity(intent);
    }

    @Override
    public void onFavoriteClick(Movie movie) {
        if (movie.isFavorite())
            Toast.makeText(this, "movie removed from favorites", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "movie added to favorites", Toast.LENGTH_SHORT).show();

        movieViewModel.updateFavoriteStatus(movie);
    }
}