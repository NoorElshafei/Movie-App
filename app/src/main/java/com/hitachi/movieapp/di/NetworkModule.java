package com.hitachi.movieapp.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hitachi.movieapp.data.network.OmdbApi;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(
            OkHttpClient okHttpClient,
            Gson gson
    ) {
        return new Retrofit.Builder()
                .baseUrl("https://www.omdbapi.com/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    @Singleton
    public OmdbApi provideApiService(
            Retrofit retrofit
    ) {
        return retrofit.create(OmdbApi.class);
    }
}
