package com.popularmovies.android.data;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;


@Entity
public class MovieModel implements Parcelable {


    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "movie_id")
    private int movieId;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "poster_path")
    private String posterPath;
    @ColumnInfo(name = "release_date")
    private String releaseDate;
    @ColumnInfo(name = "rating")
    private float rating;
    @ColumnInfo(name = "overview")
    private String overview;
    @ColumnInfo(name = "runtime")
    private int runtime;


    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };


    public MovieModel(int movieId, String title, String posterPath, String releaseDate, float rating, String overview, int runtime) {

        this.movieId = movieId;
        this.title = title;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.overview = overview;
        this.runtime = runtime;
    }

    protected MovieModel(Parcel in) {
        id = in.readInt();
        movieId = in.readInt();
        title = in.readString();
        posterPath = in.readString();
        releaseDate = in.readString();
        rating = in.readFloat();
        overview = in.readString();
        runtime = in.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(movieId);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
        dest.writeFloat(rating);
        dest.writeString(overview);
        dest.writeInt(runtime);
    }
}
