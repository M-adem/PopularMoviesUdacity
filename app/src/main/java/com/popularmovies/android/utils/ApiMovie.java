package com.popularmovies.android.utils;

import com.popularmovies.android.model.DetailMovie;
import com.popularmovies.android.model.Movie;
import com.popularmovies.android.model.MoviesResponse;
import com.popularmovies.android.model.ReviewsResponse;
import com.popularmovies.android.model.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiMovie {
    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );
    @GET("movie/{movie_id}")
    Call<DetailMovie> getMovieById(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language

    );

    @GET("movie/{movie_id}/videos")
    Call<TrailerResponse> getTrailer(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/{movie_id}/reviews")
    Call<ReviewsResponse> getReviews(
            @Path("movie_id") int movieId,
            @Query("page") int page,
            @Query("api_key") String apiKey,
            @Query("language") String language

    );
}
