package com.popularmovies.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.popularmovies.android.R;
import com.squareup.picasso.Picasso;


public class DetailActivity extends AppCompatActivity {
    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w342";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
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
