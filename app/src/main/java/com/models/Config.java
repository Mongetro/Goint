package com.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ing. Goint on 14-Oct-17.
 */

public class Config {
    // The base url for loading images
    String imageBaseUrl;
    // The poster size to use when fetching images, part of the url
    String posterSize;
    // The backdrop size to use when fetching images
    String backdropSize;

    public Config(JSONObject object)throws JSONException{
        JSONObject images=object.getJSONObject("images");
        //Get the image base url
        imageBaseUrl=images.getString("secure_base_url");
        // Get the poster size
        JSONArray posterSizeOptions=images.getJSONArray("poster_sizes");
        // Use the option at index 3 or w342 as a fallback
        posterSize=posterSizeOptions.optString(3, "w342");
        //Parse the backdrop sizes and use the option at index 1 or w780 as a fallback
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        backdropSize = backdropSizeOptions.optString(1, "w780");

    }
    //Helper methode for creating urls
    public String getImageUrl(String size, String path){
        return String.format("%s%s%s", imageBaseUrl, size, path); //Concatenate all three
    }

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }

    public String getBackdropSize() {
        return backdropSize;
    }
}
