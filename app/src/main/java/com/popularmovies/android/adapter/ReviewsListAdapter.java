package com.popularmovies.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.popularmovies.android.R;
import com.popularmovies.android.model.Review;

import java.util.List;

public class ReviewsListAdapter extends RecyclerView.Adapter<ReviewsListAdapter.ReviewViewHolder> {
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    TextView reviewAuthor;
    TextView reviewContent;
    private List<Review> reviews;
    private Context context;
    private boolean isLoading = false;


    public ReviewsListAdapter(Context context, List<Review> reviews) {
        this.reviews = reviews;
        this.context = context;

    }


    @Override
    public ReviewsListAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_reviews_item, parent, false);

        return new ReviewsListAdapter.ReviewViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ReviewsListAdapter.ReviewViewHolder holder, int position) {
        holder.bind(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        return reviews == null ? 0 : reviews.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == reviews.size() - 1 && isLoading) ? LOADING : ITEM;
    }

    public void appendReview(List<Review> reviewToAppend) {
        reviews.addAll(reviewToAppend);
        notifyDataSetChanged();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ReviewViewHolder(View itemView) {
            super(itemView);

            reviewAuthor = (TextView) itemView.findViewById(R.id.reviewers);
            reviewContent = (TextView) itemView.findViewById(R.id.content);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Review review = reviews.get(adapterPosition);
        }

        public void bind(Review review) {
            reviewAuthor.setText(review.getAuthor());
            reviewContent.setText(review.getContent());

        }
    }

}
