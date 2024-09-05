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

/**
 * Dagger Hilt module for providing network-related dependencies.
 */
@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    /**
     * Provides an OkHttpClient instance with a logging interceptor.
     *
     * @return The OkHttpClient instance.
     */
    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }
    /**
     * Provides  
     a Gson instance for JSON parsing.
     *
     * @return The Gson instance.
     */
    @Provides
    @Singleton
    public Gson provideGson() {
        return new GsonBuilder().create();
    }

    /**
     * Provides a Retrofit instance configured with the OkHttpClient and Gson.
     *
     * @param okHttpClient The OkHttpClient instance.
     * @param gson The Gson instance.
     * @return The Retrofit instance.
     */
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
    /**
     * Provides an OmdbApi interface for making API calls.
     *
     * @param retrofit The Retrofit instance.
     * @return The OmdbApi interface.
     */
    @Provides
    @Singleton
    public OmdbApi provideApiService(
            Retrofit retrofit
    ) {
        return retrofit.create(OmdbApi.class);
    }
}
