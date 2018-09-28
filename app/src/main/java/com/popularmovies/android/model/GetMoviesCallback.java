package com.popularmovies.android.model;

import java.util.List;

public  interface GetMoviesCallback {

    void onSuccess(Object ... params);
    void onError();

}
