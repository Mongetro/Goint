package com.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ing. Goint on 10-Oct-17.
 */

public class Movie {

    //value from API
    private String title;
    private String overview;
    private String posterPath; //only the path
    private String backdropPath;
    //initialize from JSON data
    public Movie(JSONObject objet) throws JSONException{
        title=objet.getString("title");
        overview=objet.getString("overview");
        posterPath=objet.getString("poster_path");
        backdropPath=objet.getString("backdrop_path");

    }

    public String getTitle() {

        return title;
    }

    public String getOverview() {

        return overview;
    }

    public String getPosterPath() {

        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }
}
