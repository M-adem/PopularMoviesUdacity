package com.popularmovies.android.model;

public interface GetMoviesCallback {

    void onSuccess(Object... params);

    void onError();

}
