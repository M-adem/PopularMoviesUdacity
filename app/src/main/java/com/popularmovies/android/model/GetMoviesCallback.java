package com.popularmovies.android.model;

import java.util.List;

public interface GetMoviesCallback {

    void onSuccess(int page,List<Movie> movies);

    void onError();
}
