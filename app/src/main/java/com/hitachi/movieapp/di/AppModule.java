package com.hitachi.movieapp.di;

import android.content.Context;

import androidx.room.Room;

import com.hitachi.movieapp.data.local.AppDatabase;
import com.hitachi.movieapp.data.local.MovieDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public AppDatabase provideDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "movie_database")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    public MovieDao provideMovieDao(AppDatabase database) {
        return database.movieDao();
    }



}
