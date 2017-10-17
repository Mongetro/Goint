package com.example.inggoint.flicksmovieapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.models.Config;
import com.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieListActivity extends AppCompatActivity {

    // Constants
    //The base URL for the API
    public final static String API_BASE_URL= "https://api.themoviedb.org/3";
    //The parameter name for the API key
    public final static String API_KEY_PARAM = "api_key";
    // Tag for logging from the activity
    public final static String TAG ="MovieListActivity";

    //Instance fields
    AsyncHttpClient client;

    //The list of currntly playing movie
    ArrayList<Movie> movies;
    //The recycler view
    RecyclerView rvMovies;
    //The adapter wired to the recycler view
    MovieAdapter adapter;
    //Image config
    Config config;


   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        // Initialize the client
        client = new AsyncHttpClient();
       //initialize the list of movie
       movies=new ArrayList<>();
       //Initialize the adapter --movie array can not be reinitilized after this point
       adapter=new MovieAdapter(movies);
       //Resolve the recycler view and connect a layout manager and adapter
       rvMovies=(RecyclerView) findViewById(R.id.rvMovies);
       rvMovies.setLayoutManager(new LinearLayoutManager(this));
       rvMovies.setAdapter(adapter);
        // Get the configuration on the app creation
        getConfiguration();
    }
    // get the list of currently playing movie from the API
    private void getNowPlaying(){
        // create the url
        String url= API_BASE_URL + "/movie/now_playing";

        // set the request param
        RequestParams params=new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //API key always requiered
        //Execute a guest request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                //load the result into movies list
                try {
                    JSONArray result=response.getJSONArray("results");
                    //iterate through set and crete Movie objects
                    for (int i=0; i<result.length(); i++){
                        Movie movie=new Movie(result.getJSONObject(i));
                        movies.add(movie);
                        //Notify adapter that a row was added
                        adapter.notifyItemInserted(movies.size() -1);
                    }
                    Log.i(TAG, String.format("Loaded %s movies", result.length()));
                } catch (JSONException e) {
                    logError("Faild to parse the now playing movies", e, true);
                }

            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                logError("Failled to get data from now_playing endpoint", throwable, true);
            }
        });
    }
    //Get the configuration from the API
    private void getConfiguration() {
        // create the url
        String url= API_BASE_URL + "/configuration";
        // set the request parameters
        RequestParams params=new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //API key always requiered
        //Execute a guest request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                // Get the image base url
                try {
                    config = new Config(response);
                    Log.i(TAG,
                            String.format("Loaded configuration with imageBaseUrl %s and posterSize %s",
                                    config.getImageBaseUrl(),
                                    config.getPosterSize()));
                    // Pass config to adapter
                    adapter.setConfig(config);
                //get the now playing movie list
                    getNowPlaying();
                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                logError("Failed getting configuration", throwable, true);
            }
        });

    }
    //Handle errors, log and alert user
    private void logError(String message, Throwable error, boolean alertUser){
        //always log the error
        Log.e(TAG, message, error);
        //alert the user to avoid silent error
        if (alertUser){
            // Show a long toast withe the error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }

    }
}