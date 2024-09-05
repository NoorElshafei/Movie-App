package com.hitachi.movieapp.di;

import android.content.Context;

import androidx.room.Room;

import com.hitachi.movieapp.data.local.AppDatabase;
import com.hitachi.movieapp.data.local.MovieDao;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

/**
 * Dagger Hilt module for providing database and DAO instances.
 */
@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    /**
     * Provides an instance of AppDatabase using Room.
     *
     * @param context The application context.
     * @return The AppDatabase instance.
     */
    @Provides
    @Singleton
    public AppDatabase provideDatabase(
            @ApplicationContext Context context
    ) {
        return Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "movie_database")
                .fallbackToDestructiveMigration()
                .build();
    }

    /**
     * Provides an instance of MovieDao from the AppDatabase.
     *
     * @param database The AppDatabase instance.
     * @return The MovieDao instance.
     */
    @Provides
    @Singleton
    public MovieDao provideMovieDao(AppDatabase database) {
        return database.movieDao();
    }

    @Provides
    @Singleton
    public Executor provideExecutor() {
        // Use a single-threaded executor for background operations
        return Executors.newSingleThreadExecutor();
    }

}
