package com.popularmovies.android.repository;

import android.support.annotation.NonNull;

import com.popularmovies.android.model.DetailMovie;
import com.popularmovies.android.model.GetMoviesCallback;
import com.popularmovies.android.model.Movie;
import com.popularmovies.android.model.MoviesResponse;
import com.popularmovies.android.model.ReviewsResponse;
import com.popularmovies.android.model.TrailerResponse;
import com.popularmovies.android.utils.ApiMovie;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.popularmovies.android.utils.Constant;
public class MoviesRepository {


    private static MoviesRepository repository;

    private ApiMovie api;

    private MoviesRepository(ApiMovie api) {
        this.api = api;
    }

    public static MoviesRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new MoviesRepository(retrofit.create(ApiMovie.class));
        }

        return repository;
    }

    public void getPopularMovies(int page, final GetMoviesCallback callback) {
        api.getPopularMovies(Constant.APIKEY, Constant.LANGUAGE, page)
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                        if (response.isSuccessful()) {
                            MoviesResponse moviesResponse = response.body();
                            if (moviesResponse != null && moviesResponse.getMovies() != null) {
                                callback.onSuccess(moviesResponse.getPage(), moviesResponse.getMovies());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getTopRatedMovies(int page, final GetMoviesCallback callback) {
        api.getTopRatedMovies(Constant.APIKEY, Constant.LANGUAGE, page)
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                        if (response.isSuccessful()) {
                            MoviesResponse moviesResponse = response.body();
                            if (moviesResponse != null && moviesResponse.getMovies() != null) {
                                callback.onSuccess(moviesResponse.getPage(), moviesResponse.getMovies());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getDetailMovie(int movieId, final GetMoviesCallback callback) {
        api.getMovieById(movieId,Constant.APIKEY, Constant.LANGUAGE)
                .enqueue(new Callback<DetailMovie>() {
                    @Override
                    public void onResponse(Call<DetailMovie> call, Response<DetailMovie> response) {

                        if (response.isSuccessful()) {
                            DetailMovie movieDetail = response.body();
                            if (movieDetail != null  ) {
                                callback.onSuccess(movieDetail.getId(), movieDetail);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<DetailMovie> call, Throwable t) {
                        callback.onError();
                    }

                });
    }


    public void getTrailerMovie(final int movieId, final GetMoviesCallback callback) {
        api.getTrailer(movieId,Constant.APIKEY, Constant.LANGUAGE)
                .enqueue(new Callback<TrailerResponse>() {
                    @Override
                    public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {

                        if (response.isSuccessful()) {
                            TrailerResponse trailerResponse = response.body();
                            if (trailerResponse != null  ) {
                                callback.onSuccess(movieId,trailerResponse.getTrailers());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<TrailerResponse> call, Throwable t) {
                        callback.onError();
                    }

                });
    }

    public void getReviewsMovie(final int movieId,final int page, final GetMoviesCallback callback) {
        api.getReviews(movieId,page,Constant.APIKEY, Constant.LANGUAGE)
                .enqueue(new Callback<ReviewsResponse>() {
                    @Override
                    public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {

                        if (response.isSuccessful()) {
                            ReviewsResponse reviewsResponse = response.body();
                            if (reviewsResponse != null  ) {
                                callback.onSuccess(movieId,page,reviewsResponse.getReviews());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                        callback.onError();
                    }

                });
    }
}
