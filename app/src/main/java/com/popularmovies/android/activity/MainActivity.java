package com.popularmovies.android.activity;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.popularmovies.android.adapter.MoviesFavoriteAdapter;
import com.popularmovies.android.data.MovieListViewModel;
import com.popularmovies.android.data.MovieModel;
import com.popularmovies.android.model.GetMoviesCallback;
import com.popularmovies.android.model.Movie;
import com.popularmovies.android.repository.MoviesRepository;
import com.popularmovies.android.utils.MovieUtile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.popularmovies.android.utils.Constant.CURRENT_PAGE;
import static com.popularmovies.android.utils.Constant.FAVORITE_MOVIES;
import static com.popularmovies.android.utils.Constant.MOVIE_LIST;
import static com.popularmovies.android.utils.Constant.POPULARE_MOVIES;
import static com.popularmovies.android.utils.Constant.TOP_RATE_MOVIES;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler, MoviesFavoriteAdapter.MoviesAdapterOnClickHandler {
    MoviesFavoriteAdapter adapterFavorite;
    private ProgressBar progressBar;
    private CustomRecyclerView moviesList;
    private MoviesAdapter adapter;
    private TextView emptyView;
    private boolean isLoadingMovies;
    private int currentPage = 1;
    private boolean popularMovies = true;
    private boolean topRatedMovies = false;
    private boolean favoriteMovies = false;
    private MoviesRepository moviesRepository;
    private MovieListViewModel viewModel;
    private Snackbar snackbar;
    private GridLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);

        moviesRepository = MoviesRepository.getInstance();

        moviesList = findViewById(R.id.movies_list_rustomrecyclerriew);
        moviesList.setHasFixedSize(true);
        manager = new GridLayoutManager(this, MovieUtile.calculateNoOfColumns(getApplicationContext()));
        moviesList.setLayoutManager(manager);

        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        if (savedInstanceState != null) {
            currentPage = savedInstanceState.getInt(CURRENT_PAGE);
            popularMovies = savedInstanceState.getBoolean(POPULARE_MOVIES);
            favoriteMovies = savedInstanceState.getBoolean(FAVORITE_MOVIES);
            topRatedMovies = savedInstanceState.getBoolean(TOP_RATE_MOVIES);
            moviesList.setVisibility(View.VISIBLE);
            isLoadingMovies = false;
            progressBar.setVisibility(View.GONE);
            if (favoriteMovies) {
                adapterFavorite = new MoviesFavoriteAdapter(this, (ArrayList<MovieModel>) savedInstanceState.getSerializable(MOVIE_LIST), this);
                moviesList.setAdapter(adapterFavorite);
            } else {
                adapter = new MoviesAdapter(this, (ArrayList<Movie>) savedInstanceState.getSerializable(MOVIE_LIST), this);
                moviesList.setAdapter(adapter);
            }

        } else {
            getPopularMovies(currentPage);

        }
        onScrollListenerPage();

        snackbar = Snackbar
                .make(findViewById(R.id.swipe_layout), R.string.no_internet, Snackbar.LENGTH_LONG)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //RETRY
                    }
                });


        snackbar.setActionTextColor(Color.RED);


        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(CURRENT_PAGE, currentPage);
        outState.putBoolean(POPULARE_MOVIES, popularMovies);
        outState.putBoolean(FAVORITE_MOVIES, favoriteMovies);
        outState.putBoolean(TOP_RATE_MOVIES, topRatedMovies);
        if (favoriteMovies) {
            List<MovieModel> movieModels = adapterFavorite.getData();
            outState.putSerializable(MOVIE_LIST, (Serializable) movieModels);
        } else {
            List<Movie> movies = adapter.getData();
            outState.putSerializable(MOVIE_LIST, (Serializable) movies);
        }
    }


    public void getFavoriteMovies() {
        isLoadingMovies = true;
        progressBar.setVisibility(View.VISIBLE);
        if (adapter == null) {
            viewModel.getItemModel().observe(this, new Observer<List<MovieModel>>() {
                @Override
                public void onChanged(@Nullable final List<MovieModel> movieModels) {
                    adapterFavorite = new MoviesFavoriteAdapter(MainActivity.this, null, MainActivity.this);
                    adapterFavorite.setData(movieModels);
                    if (favoriteMovies) {
                        moviesList.setAdapter(adapterFavorite);
                        progressBar.setVisibility(View.GONE);
                    }

                }
            });


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

                snackbar.show();

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
                snackbar.show();

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
        adapterFavorite = null;
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
    public void onClick(MovieModel movieModel) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        Movie movie = new Movie();
        movie.setId(movieModel.getMovieId());
        movie.setOverview(movieModel.getOverview());
        movie.setPosterPath(movieModel.getPosterPath());
        movie.setRating(movieModel.getRating());
        movie.setReleaseDate(movieModel.getReleaseDate());
        movie.setRuntime(movieModel.getRuntime());
        movie.setTitle(movieModel.getTitle());
        intentToStartDetailActivity.putExtra("Movie", movie);

        startActivity(intentToStartDetailActivity);
    }


}