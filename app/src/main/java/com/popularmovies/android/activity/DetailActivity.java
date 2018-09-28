package com.popularmovies.android.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.popularmovies.android.R;
import com.popularmovies.android.adapter.MoviesAdapter;
import com.popularmovies.android.adapter.ReviewsListAdapter;
import com.popularmovies.android.adapter.TrailerListAdapter;
import com.popularmovies.android.data.MovieContract;
import com.popularmovies.android.model.DetailMovie;
import com.popularmovies.android.model.GetMoviesCallback;
import com.popularmovies.android.model.Movie;
import com.popularmovies.android.model.Review;
import com.popularmovies.android.model.ReviewsResponse;
import com.popularmovies.android.model.Trailer;
import com.popularmovies.android.model.TrailerResponse;
import com.popularmovies.android.repository.MoviesRepository;
import com.popularmovies.android.utils.MovieUtile;
import com.squareup.picasso.Picasso;
import com.popularmovies.android.utils.Constant;

import java.util.List;

import static com.popularmovies.android.R.layout.listview_trailer_item;

public class DetailActivity extends AppCompatActivity {

    private MoviesRepository moviesRepository;
    private TextView moviDetailTitle;
    private ImageView movieDetailImage;
    private TextView movieDetailDate;
    private TextView movieDetailOverview;
    private TextView movieDetailRating;
    private TextView movieDetailRuntime;
    private TextView movieMakeAsFavorite;
    private Movie movie;
    private List<Trailer> trailers;
    private TrailerListAdapter trailerListAdapter;
    private List<Review> reviews;
    private ReviewsListAdapter reviewsListAdapter;
    private ListView listViewTrailer;
    private RecyclerView listViewReview;
    private boolean isLoadingReview;
    private int currentPage = 1;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        moviesRepository = MoviesRepository.getInstance();
        setUpUI(this);


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

                        listViewReview = (RecyclerView) findViewById(R.id.review_list);
                        listViewReview.setAdapter(reviewsListAdapter);
                        //MovieUtile.setListViewHeightBasedOnItems(listViewReview);
                        onScrollListenerPage(id, context);
                    } else {

                        reviewsListAdapter.appendReview((List<Review>) params[2]);
                    }

                } else {
                    TextView reviewText = (TextView) findViewById(R.id.review_header);
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

                    listViewTrailer = (ListView) findViewById(R.id.trailer_list);
                    listViewTrailer.setAdapter(trailerListAdapter);
                    MovieUtile.setListViewHeightBasedOnItems(listViewTrailer);

                    listViewTrailer.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            MovieUtile.playTrailer(context, trailers.get(position).getKey(), trailers.get(position).getSite());
                        }
                    });

                } else {
                    TextView trailerText = (TextView) findViewById(R.id.trailers_header);
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
        moviDetailTitle = (TextView) findViewById(R.id.movie_detail_title);
        movieDetailImage = (ImageView) findViewById(R.id.movie_detail_image);
        movieDetailDate = (TextView) findViewById(R.id.movie_detail_date);
        movieDetailOverview = (TextView) findViewById(R.id.movie_detail_overview);
        movieDetailRating = (TextView) findViewById(R.id.movie_detail_rating);
        movieDetailRuntime = (TextView) findViewById(R.id.movie_detail_runtime);
        movieMakeAsFavorite = (TextView) findViewById(R.id.movie_make_as_favorite);
        movieMakeAsFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isFavorite) {

                    movieMakeAsFavorite.setText(R.string.unfavorite);
                    // insert
                    ContentValues movieValues = new ContentValues();
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
                    movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
                    movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
                    movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
                    movieValues.put(MovieContract.MovieEntry.COLUMN_RATING, movie.getRating());
                    movieValues.put(MovieContract.MovieEntry.COLUMN_RUNTIME, movie.getRuntime());
                    movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());

                    context.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,movieValues);

                    isFavorite = true;
                } else {
                    movieMakeAsFavorite.setText(R.string.action_favorite);
                    isFavorite = false;

                    String[] projection = new String[]{
                            String.valueOf(movie.getId())
                    };
                    context.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,MovieContract.MovieEntry.COLUMN_MOVIE_ID +"=?", projection);

                }

            }
        });
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
                // test if favorite
                String[] projection = new String[]{
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID
                };
                String selection = MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = " + movie.getId() ;

                Cursor cursor = context.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,projection, selection, null, null);

                if ((cursor != null) && (cursor.moveToFirst())) {
                    isFavorite=true;
                    movieMakeAsFavorite.setText(R.string.unfavorite);
                    cursor.close();
                }else{
                    movieMakeAsFavorite.setText(R.string.action_favorite);
                    isFavorite=false;
                }
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
