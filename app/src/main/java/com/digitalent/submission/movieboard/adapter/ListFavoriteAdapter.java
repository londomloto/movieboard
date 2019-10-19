package com.digitalent.submission.movieboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.digitalent.submission.movieboard.R;
import com.digitalent.submission.movieboard.model.Favorite;

import java.util.ArrayList;

public class ListFavoriteAdapter extends RecyclerView.Adapter<ListFavoriteAdapter.ListViewHolder> {

    private final Context context;
    private final ArrayList<Favorite> favorites = new ArrayList<>();

    public ListFavoriteAdapter(Context context) {
        this.context = context;
    }

    public void setFavorites(ArrayList<Favorite> favorite) {
        this.favorites.clear();
        this.favorites.addAll(favorite);
        notifyDataSetChanged();
    }

    public ArrayList<Favorite> getFavorites() {
        return favorites;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_list, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.bind(favorites.get(position));
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        final TextView txtTitle;
        final TextView txtRelease;
        final TextView txtOverview;
        final ImageView imgPoster;
        final CircularProgressDrawable imgLoading;

        ListViewHolder(@NonNull View view) {
            super(view);

            txtTitle = view.findViewById(R.id.list_txt_title);
            txtRelease = view.findViewById(R.id.list_txt_release);
            txtOverview = view.findViewById(R.id.list_txt_overview);
            imgPoster = view.findViewById(R.id.list_img_poster);

            imgLoading = new CircularProgressDrawable(view.getContext());
            imgLoading.setStrokeWidth(5f);
            imgLoading.setCenterRadius(20f);
            imgLoading.setColorSchemeColors(
                    context.getResources().getColor(R.color.colorAccent)
            );
            imgLoading.start();
        }

        void bind(final Favorite favorite) {
            txtTitle.setText(favorite.getTitle());
            txtRelease.setText(favorite.getRelease_date());
            txtOverview.setText(favorite.getOverview());

            Glide.with(context)
                    .load(favorite.getPoster())
                    .apply(new RequestOptions()
                            .override(100, 150)
                            .placeholder(imgLoading))
                    .into(imgPoster);
        }
    }
}

