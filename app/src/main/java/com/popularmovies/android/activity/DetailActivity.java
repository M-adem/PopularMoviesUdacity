package com.popularmovies.android.activity;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.popularmovies.android.R;
import com.popularmovies.android.adapter.ReviewsListAdapter;
import com.popularmovies.android.adapter.TrailerListAdapter;
import com.popularmovies.android.data.MovieListViewModel;
import com.popularmovies.android.data.MovieModel;
import com.popularmovies.android.model.DetailMovie;
import com.popularmovies.android.model.GetMoviesCallback;
import com.popularmovies.android.model.Movie;
import com.popularmovies.android.model.Review;
import com.popularmovies.android.model.Trailer;
import com.popularmovies.android.repository.MoviesRepository;
import com.popularmovies.android.utils.Constant;
import com.popularmovies.android.utils.MovieUtile;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.popularmovies.android.utils.Constant.CURRENT_PAGE;

public class DetailActivity extends AppCompatActivity {

    private MoviesRepository moviesRepository;
    @BindView(R.id.movie_detail_title)
    public TextView moviDetailTitle;
    @BindView(R.id.movie_detail_image)
    public ImageView movieDetailImage;
    @BindView(R.id.movie_detail_date)
    public TextView movieDetailDate;
    @BindView(R.id.movie_detail_overview)
    public TextView movieDetailOverview;
    @BindView(R.id.movie_detail_rating)
    public TextView movieDetailRating;
    @BindView(R.id.movie_detail_runtime)
    public TextView movieDetailRuntime;
    private Movie movie;
    private List<Trailer> trailers;
    private TrailerListAdapter trailerListAdapter;
    private List<Review> reviews;
    private ReviewsListAdapter reviewsListAdapter;
    @BindView(R.id.trailer_list)
    public ListView listViewTrailer;
    @BindView(R.id.review_list)
    public RecyclerView listViewReview;
    private boolean isLoadingReview;
    private int currentPage = 1;
    private boolean isFavorite = false;
    private MovieListViewModel viewModel;
    @BindView(R.id.movie_make_as_favorite)
    public ToggleButton toggleButton;
    @BindView(R.id.review_header)
    public TextView reviewText;
    @BindView(R.id.trailers_header)
    public TextView trailerText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        moviesRepository = MoviesRepository.getInstance();
        viewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);
        setUpUI(this);


        toggleButton.setChecked(false);
        toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.unfavorite));
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.unfavorite));
                else
                    toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.make_favorite));
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(CURRENT_PAGE, currentPage);
    }

    public void getReviewsMovies(final int id, final int page, final Activity context) {
        isLoadingReview = true;
        moviesRepository.getReviewsMovie(id, page, new GetMoviesCallback() {
            @Override
            public void onSuccess(final Object... params) {
                isLoadingReview = false;
                reviews = (List<Review>) params[2];
                if (reviews != null && reviews.size() != 0) {
                    if (reviewsListAdapter == null) {
                        reviewsListAdapter = new ReviewsListAdapter(context, reviews);

                        listViewReview.setAdapter(reviewsListAdapter);
                        //MovieUtile.setListViewHeightBasedOnItems(listViewReview);
                        onScrollListenerPage(id, context);
                    } else {

                        reviewsListAdapter.appendReview((List<Review>) params[2]);
                    }

                } else {

                    reviewText.setVisibility(View.GONE);
                    reviewText.setVisibility(View.INVISIBLE);
                    if (listViewReview != null) {
                        // listViewReview.setVisibility(View.GONE);
                        // listViewReview.setVisibility(View.INVISIBLE);
                    }


                }


            }

            @Override
            public void onError() {

                Toast.makeText(DetailActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void getDetailMovies(int id) {

        moviesRepository.getDetailMovie(id, new GetMoviesCallback() {

            @Override
            public void onSuccess(Object... params) {
                DetailMovie detailMovie = (DetailMovie) params[1];

                movie.setRuntime(detailMovie.getRuntime());
                movieDetailRuntime.setText("" + movie.getRuntime() + "min");
            }


            @Override
            public void onError() {

                Toast.makeText(DetailActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getTrailerMovies(int id, final Activity context) {

        moviesRepository.getTrailerMovie(id, new GetMoviesCallback() {
            @Override
            public void onSuccess(Object... params) {
                trailers = (List<Trailer>) params[1];
                if (trailers != null && trailers.size() != 0) {

                    trailerListAdapter = new TrailerListAdapter(context, trailers);

                    listViewTrailer.setAdapter(trailerListAdapter);
                    MovieUtile.setListViewHeightBasedOnItems(listViewTrailer);

                    listViewTrailer.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            MovieUtile.playTrailer(context, trailers.get(position).getKey(), trailers.get(position).getSite());
                        }
                    });

                } else {
                    trailerText.setVisibility(View.GONE);
                    trailerText.setVisibility(View.INVISIBLE);
                    if (listViewTrailer != null) {
                        listViewTrailer.setVisibility(View.GONE);
                        listViewTrailer.setVisibility(View.INVISIBLE);
                    }


                }

            }

            @Override
            public void onError() {

                Toast.makeText(DetailActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpUI(final Context context) {

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.getParcelableExtra("Movie") != null) {
                movie = intentThatStartedThisActivity.getParcelableExtra("Movie");
                getDetailMovies(movie.getId());
                moviDetailTitle.setText(movie.getTitle());
                Picasso.with(this).load(Constant.BASE_URL_IMG + movie.getPosterPath()).placeholder(R.drawable.movie_placeholder).error(R.drawable.erreur_images).into(movieDetailImage);
                movieDetailDate.setText(movie.getReleaseDate());
                movieDetailOverview.setText(movie.getOverview());
                movieDetailRating.setText("" + movie.getRating() + "/10");

                // trailer
                getTrailerMovies(movie.getId(), this);
                //reviews
                getReviewsMovies(movie.getId(), 1, this);
                // test if make_favorite

                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... voids) {
                        isFavorite = viewModel.isFavorite(movie.getId());

                        return null;
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        if (isFavorite) {
                            //movieMakeAsFavorite.setText(R.string.unfavorite);
                            toggleButton.setChecked(true);
                            toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.unfavorite));

                        } else {
                            //movieMakeAsFavorite.setText(R.string.action_favorite);
                            toggleButton.setChecked(false);

                            toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.make_favorite));


                        }
                        toggleButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (!isFavorite) {
                                    final MovieModel movieModel = new MovieModel(movie.getId(), movie.getTitle(), movie.getPosterPath(), movie.getReleaseDate(), movie.getRating(), movie.getOverview(), movie.getRuntime());

                                    new AsyncTask<String, String, String>() {
                                        @Override
                                        protected String doInBackground(String... voids) {
                                            viewModel.insertMovieFavorite(movieModel);
                                            return null;
                                        }

                                        @Override
                                        protected void onPostExecute(String result) {
                                            isFavorite = true;
                                            //movieMakeAsFavorite.setText(R.string.unfavorite);
                                            toggleButton.setChecked(true);

                                            toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.unfavorite));

                                        }
                                    }.execute();

                                } else {
                                    // delete from make_favorite
                                    new AsyncTask<String, String, String>() {
                                        @Override
                                        protected String doInBackground(String... voids) {
                                            viewModel.deleteMovieByID(movie.getId());

                                            return null;
                                        }

                                        @Override
                                        protected void onPostExecute(String result) {
                                            // movieMakeAsFavorite.setText(R.string.action_favorite);
                                            toggleButton.setChecked(false);
                                            isFavorite = false;

                                            toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.make_favorite));

                                        }
                                    }.execute();

                                }

                            }
                        });
                    }
                }.execute();


            }
        }
    }

    private void onScrollListenerPage(final int id, final Activity activity) {
        listViewReview.setHasFixedSize(true);
        final GridLayoutManager manager = new GridLayoutManager(this, 1);
        listViewReview.setLayoutManager(manager);
        listViewReview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = manager.getItemCount();
                int visibleItemCount = manager.getChildCount();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isLoadingReview) {
                        currentPage++;
                        getReviewsMovies(id, currentPage, activity);

                    }
                }
            }
        });
    }

}
