package com.example.inggoint.flicksmovieapp;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.models.Config;
import com.models.Movie;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Ing. Goint on 14-Oct-17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
    // list of movies
    ArrayList<Movie> movies;
    //Config needed for image urls
    Config config;
    //Context for rendering
    Context context;

    // initialize with list

    public MovieAdapter(ArrayList<Movie> movies) {

        this.movies = movies;
    }

    public Config getConfig() {

        return config;
    }

    public void setConfig(Config config) {

        this.config = config;
    }

    //Create and inflates a new view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Get the context and create the inflater
        context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        //Create the view using the item_movie layout
        View movieView =inflater.inflate(R.layout.item_movie, parent, false);
        //Returne a new viewHolder
        return new ViewHolder(movieView);

    }
    //Binds an inflated view to a new item
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Get the movie data at specified position
        Movie movie=movies.get(position);
        //Populste the view with the movie data
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());

        //Determine the current orientation
        Boolean isPortrait=context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        //Build url for poster image
        String imageUrl=null;

        //If in poster mode, load the poster image
        if (isPortrait){
            imageUrl=config.getImageUrl(config.getPosterSize(), movie.getPosterPath());

        }
        else {
            // Load the backdrop image
            imageUrl=config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
        }
        //Get the correct placeholderview and imageview for the current orientation
        int placeholderId=isPortrait ? R.drawable.flicks_movie_placeholder: R.drawable.flicks_backdrop_placeholder;
        ImageView imageView= isPortrait ? holder.ivPosterImage:holder.ivBackdropImage;
        //Load image using glide
        Glide.with(context)
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 25, 0))
                .placeholder(placeholderId)
                .error(placeholderId)
                .into(imageView);
    }
    //return to the total number items of the list
    @Override
    public int getItemCount() {

        return movies.size();
    }

    //Create the viewholder as a static inner class
    public static class ViewHolder extends RecyclerView.ViewHolder{

        //Track view object
        ImageView ivPosterImage;
        ImageView ivBackdropImage;
        TextView tvTitle;
        TextView tvOverview;

        public ViewHolder(View itemView) {
            super(itemView);
            //lookup viewobjects by id
            ivPosterImage=(ImageView) itemView.findViewById(R.id.ivPosterImage);
            ivBackdropImage=(ImageView) itemView.findViewById(R.id.ivbackdropImage);
            tvOverview=(TextView) itemView.findViewById(R.id.tvOverview);
            tvTitle=(TextView) itemView.findViewById(R.id.tvTitle);


        }
    }
}
