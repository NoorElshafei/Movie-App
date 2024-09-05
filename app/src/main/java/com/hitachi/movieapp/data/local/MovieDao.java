package com.hitachi.movieapp.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.hitachi.movieapp.data.model.response.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movie movie);

    @Query("UPDATE movies SET isFavorite = :isFavorite WHERE imdbID = :imdbID")
    void updateFavoriteStatus(String imdbID, boolean isFavorite);

    @Query("DELETE FROM movies WHERE imdbID = :imdbID")
    void deleteById(String imdbID);

    @Query("SELECT * FROM movies WHERE imdbID = :imdbID LIMIT 1")
    Movie getMovieById(String imdbID);

    @Query("SELECT EXISTS(SELECT 1 FROM movies WHERE imdbID = :imdbID LIMIT 1)")
    boolean isMovieExists(String imdbID);
    @Query("SELECT * FROM movies WHERE isFavorite = 1")
    LiveData<List<Movie>> getFavoriteMovies();
}
