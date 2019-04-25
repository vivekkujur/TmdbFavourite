package com.tmdbfav.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.VolleyError;
import com.tmdbfav.Adapter.FavRecyclerAdapter;
import com.tmdbfav.Adapter.MovieRecyclerAdapter;
import com.tmdbfav.R;
import com.tmdbfav.model.Movie;
import com.tmdbfav.model.MovieDetail;
import com.tmdbfav.rest.RestGetMovieDetailApi;
import com.tmdbfav.rest.RestGetMovieDetailInterface;
import com.tmdbfav.utils.SharedStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity implements RestGetMovieDetailInterface {

  public static   FavRecyclerAdapter favRecyclerAdapter;
    RecyclerView favRecycler;
    List<Movie> movieList = new ArrayList<>();
    RestGetMovieDetailApi restGetMovieDetailApi;
    SharedStorage sharedStorage;
    int i;
    ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        init();

        restGetMovieDetailApi = new RestGetMovieDetailApi(this);
        sharedStorage = new SharedStorage(this);

        list = sharedStorage.getListString(SharedStorage.movieIdList);

        for(i=0; i<list.size();i++){
            Log.e( "onCreate: ", list.get(i)+"sdfd" );
            restGetMovieDetailApi.getMOvieDetail(list.get(i));

        }


    }

    @Override
    public void onMoviedetailSuccess(JSONObject response) {
        Movie movie= new Movie();

        try {
            movie.setId(Integer.parseInt(response.getString("id")));
            movie.setOriginal_language(response.getString("original_language"));
            movie.setOverview(response.getString("overview"));
            movie.setPoster_path(response.getString("poster_path"));
            movie.setRelease_date(response.getString("release_date"));
            movie.setTitle(response.getString("title"));
            movie.setVote_average(response.getString("vote_average"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        movieList.add(movie);
        Log.e( "onMoviedetailSuccess: ", i  +" size ovie "  );

        if(i == list.size() ){
            initrecycler(movieList);
        }

    }

    @Override
    public void onMoviedetailError(VolleyError error) {

    }

    private void init() {

        favRecycler = findViewById(R.id.favRecycler);
    }
    private void initrecycler(List<Movie> movieList){

        favRecyclerAdapter= new FavRecyclerAdapter(this,movieList);
        favRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        favRecycler.setAdapter(favRecyclerAdapter);
    }
}
