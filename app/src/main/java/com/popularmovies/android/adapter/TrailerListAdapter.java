package com.popularmovies.android.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.popularmovies.android.R;
import com.popularmovies.android.model.Trailer;

import java.util.List;

public class TrailerListAdapter extends ArrayAdapter<Trailer> {

    private Activity context;
    private List<Trailer> trailers;

    public TrailerListAdapter(Activity context, List<Trailer> trailers) {
        super(context, R.layout.listview_trailer_item, trailers);
        this.context = context;

        this.trailers = trailers;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_trailer_item, null, true);

        TextView trailerName = (TextView) rowView.findViewById(R.id.trailer_name);

        Trailer trailer = trailers.get(position);
        trailerName.setText(trailer.getName());

        return rowView;

    }


}
