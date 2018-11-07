package com.sanitcode.favoritemovie.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanitcode.favoritemovie.BuildConfig;
import com.sanitcode.favoritemovie.DetailActivity;
import com.sanitcode.favoritemovie.R;
import com.sanitcode.favoritemovie.data.model.ResultItems;
import com.sanitcode.favoritemovie.util.DateTime;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    public final static String MOVIE_DETAIL = "movie_detail";

    private List<ResultItems> movieResultList = new ArrayList<>();
    private Context context;

    public MovieAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,
                        parent, false)
        );
    }

    public void setMovieResult(List<ResultItems> movieResult) {
        this.movieResultList = movieResult;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bindView(movieResultList.get(position));
    }

    @Override
    public int getItemCount() {
        return movieResultList.size();
    }

    //View Holder
    class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.poster)
        ImageView image_poster;
        @BindView(R.id.btn_share)
        Button buttonShare;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.btn_detail)
        Button buttonDetail;
        @BindView(R.id.releasedate)
        TextView releaseDate;
        @BindView(R.id.overview)
        TextView overview;

        MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindView(final ResultItems movieResult) {
            title.setText(movieResult.getTitle());
            overview.setText(movieResult.getOverview());
            releaseDate.setText(DateTime.getDateDay(movieResult.getReleaseDate()));
            Picasso.get()
                    .load(BuildConfig.BASE_POSTER_URL + movieResult.getPosterPath())
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_image)
                    .into(image_poster);

            buttonDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(buttonDetail.getContext(), DetailActivity.class);
                    intent.putExtra(MOVIE_DETAIL, movieResult);
                    buttonDetail.getContext().startActivity(intent);
                }
            });
            buttonShare.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("StringFormatInvalid")
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                            context.getString(R.string.share, movieResult.getTitle()));
                    sendIntent.setType("text/plain");
                    itemView.getContext().startActivity(sendIntent);
                }
            });

        }
    }
}
