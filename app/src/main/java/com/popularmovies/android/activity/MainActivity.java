package com.popularmovies.android.activity;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.popularmovies.android.R;
import com.popularmovies.android.adapter.MoviesAdapter;
import com.popularmovies.android.model.GetMoviesCallback;
import com.popularmovies.android.model.Movie;
import com.popularmovies.android.repository.MoviesRepository;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {
    private RecyclerView moviesList;
    private MoviesAdapter adapter;

    private boolean isLoadingMovies;
    private int currentPage = 1;


    private boolean popularMovies=true;
    private boolean topRatedMovies=false;

    private MoviesRepository moviesRepository;
    ProgressBar progressBar;
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

    public void getPopularMovies(int page) {
        progressBar.setVisibility(View.VISIBLE);
        isLoadingMovies = true;
        moviesRepository.getPopularMovies(page, new GetMoviesCallback() {

            @Override
            public void onSuccess(Object... params  ) {
                progressBar.setVisibility(View.GONE);
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
                Toast.makeText(MainActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getTopRatedMovies(int page) {
        progressBar.setVisibility(View.VISIBLE);
        isLoadingMovies = true;
        moviesRepository.getTopRatedMovies(page, new GetMoviesCallback() {
            @Override
            public void onSuccess(Object... params  ) {
                progressBar.setVisibility(View.GONE);
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
                Toast.makeText(MainActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        currentPage=1;
        adapter=null;
        switch (item.getItemId()) {
            case R.id.sort_by_popularity:
                popularMovies=true;
                topRatedMovies=false;
                getPopularMovies(currentPage);
                return true;
            case R.id.sort_by_rating:
                popularMovies=false;
                topRatedMovies=true;
                getTopRatedMovies(currentPage);
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
        intentToStartDetailActivity.putExtra("id", movie.getId());
        intentToStartDetailActivity.putExtra("movie_detail_image", movie.getPosterPath());
        intentToStartDetailActivity.putExtra("movie_detail_title", movie.getTitle());
        intentToStartDetailActivity.putExtra("movie_detail_date", movie.getReleaseDate());
        intentToStartDetailActivity.putExtra("movie_detail_rating", movie.getRating());
        intentToStartDetailActivity.putExtra("movie_detail_overview", movie.getOverview());
        startActivity(intentToStartDetailActivity);

    }


    private void onScrollListenerPage() {
        moviesList.setHasFixedSize(true);
        final GridLayoutManager manager = new GridLayoutManager(this,2);
        moviesList.setLayoutManager(manager);
        moviesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = manager.getItemCount();
                int visibleItemCount = manager.getChildCount();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isLoadingMovies) {
                        if(popularMovies){
                            getPopularMovies(currentPage + 1);
                        }
                        if(topRatedMovies){
                            getTopRatedMovies(currentPage + 1);
                        }
                    }
                }
            }
        });
    }


}