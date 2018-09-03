package com.popularmovies.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.popularmovies.android.R;
import com.popularmovies.android.adapter.MoviesAdapter;
import com.popularmovies.android.model.Movie;
import com.popularmovies.android.model.OnGetMoviesCallback;
import com.popularmovies.android.repository.MoviesRepository;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w342";
    private int movieID;
    private MoviesRepository moviesRepository;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            //movieID = Integer.parseInt(intentThatStartedThisActivity.getExtras().get("selectedItem").toString());
            TextView moviDetailTitle = (TextView) findViewById(R.id.movie_detail_title);
            ImageView movieDetailImage = (ImageView) findViewById(R.id.movie_detail_image);
            TextView movieDetailDate = (TextView) findViewById(R.id.movie_detail_date);
            TextView movieDetailOverview = (TextView) findViewById(R.id.movie_detail_overview);
            TextView movieDetailRating = (TextView) findViewById(R.id.movie_detail_rating);



            moviDetailTitle.setText(intentThatStartedThisActivity.getExtras().get("movie_detail_title").toString());
            movieDetailDate.setText(intentThatStartedThisActivity.getExtras().get("movie_detail_date").toString());
            movieDetailOverview.setText(intentThatStartedThisActivity.getExtras().get("movie_detail_overview").toString());
            movieDetailRating.setText(intentThatStartedThisActivity.getExtras().get("movie_detail_rating").toString());

            Picasso.with(this).load(BASE_URL_IMG + intentThatStartedThisActivity.getExtras().get("movie_detail_image").toString()).placeholder(R.drawable.movie_placeholder).error(R.drawable.erreur_images).into(movieDetailImage);




        }
    }

}
