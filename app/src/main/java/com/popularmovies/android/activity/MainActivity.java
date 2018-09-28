package com.popularmovies.android.activity;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.popularmovies.android.R;
import com.popularmovies.android.adapter.MoviesAdapter;
import com.popularmovies.android.data.MovieContract;
import com.popularmovies.android.model.GetMoviesCallback;
import com.popularmovies.android.model.Movie;
import com.popularmovies.android.repository.MoviesRepository;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {
    ProgressBar progressBar;
    private RecyclerView moviesList;
    private MoviesAdapter adapter;
    private TextView emptyView;
    private boolean isLoadingMovies;
    private int currentPage = 1;
    private boolean popularMovies = true;
    private boolean topRatedMovies = false;
    private boolean favoriteMovies = false;
    private MoviesRepository moviesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesRepository = MoviesRepository.getInstance();

        moviesList = findViewById(R.id.movies_list);


        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        onScrollListenerPage();
        getPopularMovies(currentPage);


    }

    public List<Movie> getAllFavorite() {
        List<Movie> movieList = new ArrayList<>();

        Cursor cursor = this.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, MovieContract.MovieEntry.COLUMN_MOVIE_ID);

        if ((cursor != null) && (cursor.moveToFirst())) {
            emptyView = (TextView) findViewById(R.id.empty_view);
            emptyView.setText("");
            moviesList.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            do {
                Movie movie = new Movie();
                movie.setId(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH)));
                movie.setRating(cursor.getFloat(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)));
                movie.setRuntime(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RUNTIME)));

                movieList.add(movie);
            } while (cursor.moveToNext());
        } else {
            emptyView = (TextView) findViewById(R.id.empty_view);
            emptyView.setText(R.string.no_favorite_available);
            moviesList.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);

        }

        return movieList;
    }

    public void getFavoriteMovies() {
        //progressBar.setVisibility(View.VISIBLE);
        isLoadingMovies = true;

        //progressBar.setVisibility(View.GONE);
        if (adapter == null) {
            adapter = new MoviesAdapter(MainActivity.this, getAllFavorite(), MainActivity.this);
            moviesList.setAdapter(adapter);
        }

        isLoadingMovies = false;
    }

    public void getPopularMovies(int page) {
        progressBar.setVisibility(View.VISIBLE);
        isLoadingMovies = true;
        moviesRepository.getPopularMovies(page, new GetMoviesCallback() {

            @Override
            public void onSuccess(Object... params) {
                progressBar.setVisibility(View.GONE);
                emptyView = (TextView) findViewById(R.id.empty_view);
                emptyView.setText("");
                moviesList.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);

                if (adapter == null) {
                    adapter = new MoviesAdapter(MainActivity.this, (List<Movie>) params[1], MainActivity.this);
                    moviesList.setAdapter(adapter);
                } else {
                    adapter.appendMovies((List<Movie>) params[1]);
                }
                currentPage = (int) params[0];
                isLoadingMovies = false;
            }


            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);

                emptyView = (TextView) findViewById(R.id.empty_view);
                emptyView.setText(R.string.no_internet);
                moviesList.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);

            }
        });
    }

    public void getTopRatedMovies(int page) {
        progressBar.setVisibility(View.VISIBLE);
        isLoadingMovies = true;

        moviesRepository.getTopRatedMovies(page, new GetMoviesCallback() {
            @Override
            public void onSuccess(Object... params) {
                progressBar.setVisibility(View.GONE);
                emptyView = (TextView) findViewById(R.id.empty_view);
                emptyView.setText("");
                moviesList.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);

                if (adapter == null) {
                    adapter = new MoviesAdapter(MainActivity.this, (List<Movie>) params[1], MainActivity.this);
                    moviesList.setAdapter(adapter);
                } else {
                    adapter.appendMovies((List<Movie>) params[1]);
                }
                currentPage = (int) params[0];
                isLoadingMovies = false;
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                emptyView = (TextView) findViewById(R.id.empty_view);
                emptyView.setText(R.string.no_internet);
                moviesList.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        currentPage = 1;
        adapter = null;
        switch (item.getItemId()) {
            case R.id.sort_by_popularity:
                popularMovies = true;
                topRatedMovies = false;
                favoriteMovies = false;
                getPopularMovies(currentPage);
                return true;
            case R.id.sort_by_rating:
                popularMovies = false;
                topRatedMovies = true;
                favoriteMovies = false;
                getTopRatedMovies(currentPage);
                return true;
            case R.id.sort_by_favorite:
                popularMovies = false;
                topRatedMovies = false;
                favoriteMovies = true;
                getFavoriteMovies();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("Movie", movie);

        startActivity(intentToStartDetailActivity);

    }


    private void onScrollListenerPage() {
        moviesList.setHasFixedSize(true);
        final GridLayoutManager manager = new GridLayoutManager(this, 2);
        moviesList.setLayoutManager(manager);
        moviesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = manager.getItemCount();
                int visibleItemCount = manager.getChildCount();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isLoadingMovies) {
                        if (popularMovies) {
                            getPopularMovies(currentPage + 1);
                        }
                        if (topRatedMovies) {
                            getTopRatedMovies(currentPage + 1);
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = null;
        currentPage = 1;
        if (favoriteMovies) {
            getFavoriteMovies();
        }
        if (topRatedMovies) {
            getTopRatedMovies(currentPage);
        }
        if (popularMovies) {
            getPopularMovies(currentPage);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        /*
        if ( getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(MainActivity.this, "landscape", Toast.LENGTH_SHORT).show();
        } else if ( getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(MainActivity.this, "portrait", Toast.LENGTH_SHORT).show();
        }*/
    }
}