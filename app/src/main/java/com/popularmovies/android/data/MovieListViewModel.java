package com.popularmovies.android.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class MovieListViewModel extends AndroidViewModel {

    private final LiveData<List<MovieModel>> itemModelList;

    private AppDatabase appDatabase;

    public MovieListViewModel(Application application) {
        super(application);

        appDatabase = AppDatabase.getDatabase(this.getApplication());

        itemModelList = appDatabase.itemModelModel().getAllMovieItems();
    }


    public LiveData<List<MovieModel>> getItemModel() {
        return itemModelList;
    }

    public void deleteItem(MovieModel MovieModel) {
        new deleteAsyncTask(appDatabase).execute(MovieModel);
    }

    public void deleteMovieByID(int idMovie) {
        MovieModel movieModel = appDatabase.itemModelModel().getItembyIdMovie(idMovie);
        this.deleteItem(movieModel);
    }

    public void insertMovieFavorite(MovieModel movieModel) {
        appDatabase.itemModelModel().addMovie(movieModel);
    }

    public boolean isFavorite(int idMovie) {
        MovieModel movieModel = appDatabase.itemModelModel().getItembyIdMovie(idMovie);
        if (movieModel == null) {
            return false;
        } else {
            return true;
        }
    }


    private static class deleteAsyncTask extends AsyncTask<MovieModel, Void, Void> {

        private AppDatabase db;

        deleteAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final MovieModel... params) {
            db.itemModelModel().deleteMovie(params[0]);
            return null;
        }

    }

}