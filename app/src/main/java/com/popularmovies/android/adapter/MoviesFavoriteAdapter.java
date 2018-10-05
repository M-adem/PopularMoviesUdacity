package com.popularmovies.android.adapter;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.popularmovies.android.R;
import com.popularmovies.android.data.MovieModel;
import com.popularmovies.android.utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesFavoriteAdapter extends RecyclerView.Adapter<MoviesFavoriteAdapter.MovieViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private final MoviesAdapterOnClickHandler mClickHandler;
    private List<MovieModel> movieModels;
    private Context context;
    private boolean isLoading = false;

    public MoviesFavoriteAdapter(MoviesAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }


    public MoviesFavoriteAdapter(Context context, List<MovieModel> movieModels, MoviesAdapterOnClickHandler mClickHandler) {
        this.movieModels = movieModels;
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
        holder.bind(movieModels.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return movieModels == null ? 0 : movieModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == movieModels.size() - 1 && isLoading) ? LOADING : ITEM;
    }

    public void appendMovies(List<MovieModel> moviesToAppend) {
        movieModels.addAll(moviesToAppend);
        notifyDataSetChanged();
    }

    public List<MovieModel> getData() {
        return this.movieModels;
    }

    public void setData(List<MovieModel> newData) {
        if (movieModels != null) {
            PostDiffCallback postDiffCallback = new PostDiffCallback(movieModels, newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(postDiffCallback);

            movieModels.clear();
            movieModels.addAll(newData);
            diffResult.dispatchUpdatesTo(this);
        } else {
            // first initialization
            movieModels = newData;
        }
    }

    public interface MoviesAdapterOnClickHandler {
        void onClick(MovieModel movieModels);
    }

    class PostDiffCallback extends DiffUtil.Callback {

        private final List<MovieModel> oldPosts, newPosts;

        public PostDiffCallback(List<MovieModel> oldPosts, List<MovieModel> newPosts) {
            this.oldPosts = oldPosts;
            this.newPosts = newPosts;
        }

        @Override
        public int getOldListSize() {
            return oldPosts.size();
        }

        @Override
        public int getNewListSize() {
            return newPosts.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldPosts.get(oldItemPosition).getId() == newPosts.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldPosts.get(oldItemPosition).equals(newPosts.get(newItemPosition));
        }
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageUrl;

        public MovieViewHolder(View itemView) {
            super(itemView);
            imageUrl = itemView.findViewById(R.id.image_url);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            MovieModel movieModel = movieModels.get(adapterPosition);
            mClickHandler.onClick(movieModel);
        }

        public void bind(MovieModel movieModels) {

            Picasso.with(context).load(Constant.BASE_URL_IMG + movieModels.getPosterPath()).placeholder(R.drawable.movie_placeholder).error(R.drawable.erreur_images).into(imageUrl);
        }
    }
}