package com.hitachi.movieapp.presentation.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hitachi.movieapp.R;
import com.hitachi.movieapp.data.model.response.Movie;
import com.hitachi.movieapp.presentation.movies_list.OnFavoriteClickListener;
import com.hitachi.movieapp.presentation.movies_list.OnMovieClickListener;

import java.util.ArrayList;
import java.util.List;
/**
 * Adapter for displaying a list of movies in a RecyclerView.
 * Handles view binding, click events, and updates to the movie list.
 */
public class MovieAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    // List of movies to display
    private final List<Movie> movieList;
    // Callback for when a movie item is clicked
    private final OnMovieClickListener listener;
    // Callback for when the favorite button is clicked
    private final OnFavoriteClickListener onFavoriteClickListener;
    // View types for loading indicator and movie items
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_MOVIE = 1;
    // Flag to indicate if a loading footer is added
    private boolean isLoadingAdded = false;


    /**
     * Constructor for the MovieAdapter.
     *
     * @param movieList The list of movies to display.
     * @param listener The callback for movie item clicks.
     * @param onFavoriteClickListener The callback for favorite button clicks.
     */
    public MovieAdapter(List<Movie> movieList,
                        OnMovieClickListener listener,
                        OnFavoriteClickListener onFavoriteClickListener
    ) {
        this.onFavoriteClickListener = onFavoriteClickListener;
        this.movieList = movieList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_MOVIE) {
            return new MovieViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_movie, parent, false));
        } else {
            return new ProgressHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.onBind(position);
    }

    /**
     * Adds a loading footer to the adapter for indicating data is being fetched.
     */
    public void addLoadingFooter() {
        isLoadingAdded = true;
        movieList.add(new Movie()); // Adding an empty item as the loading placeholder
        notifyItemInserted(movieList.size() - 1);
    }

    /**
     * Removes the loading footer from the adapter.
     */
    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = movieList.size() - 1;
        Movie item = getItem(position);
        if (item != null) {
            movieList.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * Gets the movie item at the specified position.
     *
     * @param position The position of the item.
     * @return The movie item at the given position, or null if it doesn't exist.
     */
    private Movie getItem(int position) {
        return movieList.get(position);
    }

    @Override
    public int getItemCount() {
        return movieList != null ? movieList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == movieList.size() - 1 && isLoadingAdded) ? VIEW_TYPE_LOADING : VIEW_TYPE_MOVIE;
    }

    /**
     * Updates the movie list with the provided list of movies.
     *
     * @param movies The new list of movies.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void updateMovies(List<Movie> movies) {
        movieList.addAll(movies);
        notifyDataSetChanged();
    }

    /**
     * Adds a list of new movies to the end of the existing movie list.
     *
     * @param newMovies The list of new movies to add.
     */
    public void addMovies(List<Movie> newMovies) {
        int startPosition = movieList.size();
        this.movieList.addAll(newMovies);
        notifyItemRangeInserted(startPosition, newMovies.size());
    }

    /**
     * Clears the movie list.
     */
    public void removeMovies() {
        movieList.clear();
        notifyDataSetChanged();
    }

    /**
     * Edits the movie at the specified position to toggle its favorite status.
     *
     * @param position The position of the movie to edit.
     */
    public void editMovie(int position) {
        if (!movieList.isEmpty()) {
            movieList.get(position).setFavorite(true);
            notifyItemChanged(position);
        }
    }

    /**
     * View holder for movie items.
     */
    public class MovieViewHolder extends BaseViewHolder {
        TextView title, year;
        ImageView poster, fav;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewTitle);
            year = itemView.findViewById(R.id.textViewYear);
            poster = itemView.findViewById(R.id.imageViewPoster);
            fav = itemView.findViewById(R.id.buttonFavorite);

        }

        @Override
        public void onBind(int position) {
            if (getItemViewType() == VIEW_TYPE_MOVIE) {
                Movie movie = movieList.get(position);
                title.setText(movie.getTitle());
                year.setText(movie.getYear());
                Glide.with(poster.getContext()).load(movie.getPoster()).placeholder(R.drawable.place_holder).into(poster);

                // Check if the movie is a favorite and update the favorite button accordingly
                if (movie.isFavorite())
                    Glide.with(fav.getContext()).load(R.drawable.baseline_favorite_24).into(fav);
                else
                    Glide.with(fav.getContext()).load(R.drawable.baseline_favorite_border_24).into(fav);

                itemView.setOnClickListener(v -> listener.onMovieClick(movie));
                fav.setOnClickListener(v -> {
                    onFavoriteClickListener.onFavoriteClick(movie);
                    // Toggle favorite status
                    movie.setFavorite(!movie.isFavorite());
                    notifyItemChanged(position);
                });

            }
        }
    }

    /**
     * View holder for the loading indicator.
     */
    public static class ProgressHolder extends BaseViewHolder {
        public ProgressHolder(View itemView) {
            super(itemView);
        }
    }
}
