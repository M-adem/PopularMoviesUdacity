package com.popularmovies.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.popularmovies.android.R;
import com.popularmovies.android.model.Movie;
import com.popularmovies.android.utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private Context context;
    private boolean isLoading = false;



    private static final int ITEM = 0;
    private static final int LOADING = 1;



    private final MoviesAdapterOnClickHandler mClickHandler ;


    public interface MoviesAdapterOnClickHandler {
        void onClick(Movie movie);
    }


    public MoviesAdapter(MoviesAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public MoviesAdapter(Context context, List<Movie> movies, MoviesAdapterOnClickHandler mClickHandler) {
        this.movies = movies;
        this.context = context;
        this.mClickHandler = mClickHandler;
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(movies.get(position));
    }


    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageUrl;

        public MovieViewHolder(View itemView) {
            super(itemView);
            imageUrl = itemView.findViewById(R.id.image_url);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = movies.get(adapterPosition);
            mClickHandler.onClick(movie );
        }
        public void bind(Movie movie) {

            Picasso.with(context).load(Constant.BASE_URL_IMG + movie.getPosterPath()).placeholder(R.drawable.movie_placeholder).error(R.drawable.erreur_images).into(imageUrl);
        }
    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }
    @Override
    public int getItemViewType(int position) {
        return (position == movies.size() - 1 && isLoading) ? LOADING : ITEM;
    }

    public void appendMovies(List<Movie> moviesToAppend) {
        movies.addAll(moviesToAppend);
        notifyDataSetChanged();
    }

}