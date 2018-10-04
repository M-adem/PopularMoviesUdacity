package com.popularmovies.android.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;


@Dao
public interface MovieModelDao {
    @Query("select * from MovieModel")
    LiveData<List<MovieModel>> getAllMovieItems();

    @Query("select * from MovieModel where movie_id = :id")
    MovieModel getItembyIdMovie(int id);

    @Query("select * from MovieModel where id = :id")
    MovieModel getItembyId(String id);


    @Insert(onConflict = REPLACE)
    void addMovie(MovieModel movieModel);

    @Delete
    void deleteMovie(MovieModel movieModel);

}