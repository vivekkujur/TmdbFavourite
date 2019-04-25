package com.tmdbfav;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.VolleyError;
import com.tmdbfav.Adapter.MovieRecyclerAdapter;
import com.tmdbfav.model.Movie;
import com.tmdbfav.rest.RestGetMovieListApi;
import com.tmdbfav.rest.RestGetMovieListInterface;
import com.tmdbfav.ui.FavoriteActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , RestGetMovieListInterface {
    public static  MovieRecyclerAdapter movieRecyclerAdapter;
    RecyclerView movieRecycler;
    RestGetMovieListApi restGetMovieListApi;
    List<Movie> movieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        restGetMovieListApi= new RestGetMovieListApi(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        restGetMovieListApi.getMOvielist();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_playing) {
            // Handle the camera action
        } else if (id == R.id.nav_fav) {
            Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void init() {

        movieRecycler = findViewById(R.id.movieRecycler);
    }

    private void initrecycler(List<Movie> movieList){
        movieRecyclerAdapter= new MovieRecyclerAdapter(this,movieList);
        movieRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        movieRecycler.setAdapter(movieRecyclerAdapter);
    }

    @Override
    public void onMovieListSuccess(JSONObject response) {
        try{
            for(int i= 0 ;i< response.getJSONArray("results").length();i++){

                Movie movie= new Movie();
                movie.setId(Integer.parseInt(response.getJSONArray("results").getJSONObject(i).getString("id")));
                movie.setOriginal_language(response.getJSONArray("results").getJSONObject(i).getString("original_language"));
                movie.setOverview(response.getJSONArray("results").getJSONObject(i).getString("overview"));
                movie.setPoster_path(response.getJSONArray("results").getJSONObject(i).getString("poster_path"));
                movie.setRelease_date(response.getJSONArray("results").getJSONObject(i).getString("release_date"));
                movie.setTitle(response.getJSONArray("results").getJSONObject(i).getString("title"));
                movie.setVote_average(response.getJSONArray("results").getJSONObject(i).getString("vote_average"));

                movieList.add(movie);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        initrecycler(movieList);

    }

    @Override
    public void onMovieListError(VolleyError error) {

    }
}
