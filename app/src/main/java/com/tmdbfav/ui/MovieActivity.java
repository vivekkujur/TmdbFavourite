package com.tmdbfav.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;
import com.tmdbfav.MainActivity;
import com.tmdbfav.R;
import com.tmdbfav.Setting.Config;
import com.tmdbfav.model.MovieDetail;
import com.tmdbfav.rest.RestGetMovieDetailApi;
import com.tmdbfav.rest.RestGetMovieDetailInterface;
import com.tmdbfav.utils.SharedStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
public class MovieActivity extends AppCompatActivity implements RestGetMovieDetailInterface {


    TextView desc, date, rating, budget, revenue, language, runningtime,genresList;
    ImageView poster,favimagemovie;
    int id;
    final String[] Selected = new String[]{"Jan", "Feb", "Mar", "Apr",
            "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    Toolbar toolbar;
    RestGetMovieDetailApi restGetMovieDetailApi;
    ArrayList<String> movieid = new ArrayList<>();
    SharedStorage sharedStorage;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        MainActivity.movieRecyclerAdapter.notifyDataSetChanged();

        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        init();
        restGetMovieDetailApi= new RestGetMovieDetailApi(this);

        Bundle bundle= getIntent().getExtras();
        if (bundle != null) {
             id= bundle.getInt("id");
             restGetMovieDetailApi.getMOvieDetail(String.valueOf(id));
        }

        sharedStorage = new SharedStorage(this);
        movieid = sharedStorage.getListString(SharedStorage.movieIdList);


        if(movieid.contains(String.valueOf(id))){
            favimagemovie.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_favorite_black_24dp));
        }else{
            favimagemovie.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_favorite_border_black_24dp));
        }

        favimagemovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(movieid.contains(String.valueOf(id))){
                    favimagemovie.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_favorite_border_black_24dp));
                    movieid.remove(String.valueOf(id));
                    sharedStorage.putListString(SharedStorage.movieIdList,movieid);

                }else {
                    favimagemovie.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_black_24dp));
                    movieid.add(String.valueOf(id));
                    sharedStorage.putListString(SharedStorage.movieIdList, movieid);
                }
            }
        });

    }

    private void init() {
        poster = findViewById(R.id.poster);
        desc= findViewById(R.id.movieoverview);
        date= findViewById(R.id.daterelease);
        rating= findViewById(R.id.ratingmovie);
        budget= findViewById(R.id.budget);
        revenue= findViewById(R.id.revenue);
        language= findViewById(R.id.languagemovie);
        runningtime= findViewById(R.id.runninnTzime);
        genresList= findViewById(R.id.genre);
        favimagemovie = findViewById(R.id.favimagemovie);
    }

    private  void SetViewDetails(MovieDetail movieDetail){

        try {
            runningtime.setText(movieDetail.getRuntime() + " minutes");

            try {
                String datestr = movieDetail.getRelease_date();

                String[] arrOfStr = datestr.split("-");
                if (Integer.parseInt(arrOfStr[2]) > 10 && Integer.parseInt(arrOfStr[2]) < 14)
                    arrOfStr[2] = arrOfStr[2] + "th ";
                else {
                    if (arrOfStr[2].endsWith("1")) arrOfStr[2] = arrOfStr[2] + "st";
                    else if (arrOfStr[2].endsWith("2")) arrOfStr[2] = arrOfStr[2] + "nd";
                    else if (arrOfStr[2].endsWith("3")) arrOfStr[2] = arrOfStr[2] + "rd";
                    else arrOfStr[2] = arrOfStr[2] + "th";
                }
                date.setText(arrOfStr[2] + " " + Selected[Integer.parseInt(arrOfStr[1]) - 1] + arrOfStr[0]);

            } catch (Exception e) {
                e.printStackTrace();
            }
            language.setText(movieDetail.getOriginal_language());
            rating.setText(movieDetail.getVote_average());
            desc.setText(movieDetail.getOverview());

            Picasso.get()
                    .load(Config.ImageBaseUrl + movieDetail.getBackdrop_path())
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .into(poster);

            genresList.setText(movieDetail.getGenres());

            int budgeta = Integer.parseInt(movieDetail.getBudget());
            int budgetd = budgeta / 1000000;
            budget.setText("$" + budgetd + "Million");

            int revenueA = Integer.parseInt(movieDetail.getRevenue());
            int revenueD = revenueA / 1000000;
            revenue.setText("$" + revenueD + "Million");

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private String formatGenre(JSONObject jsonObject){
        List<String> gener1= new ArrayList<>();
        String genres="";

        try{
            for(int i=0;i<jsonObject.getJSONArray("genres").length();i++){
                gener1.add(jsonObject.getJSONArray("genres").getJSONObject(i).getString("name"));
            }
            int sizeGenres=jsonObject.getJSONArray("genres").length();
            for(int i= 0;i<sizeGenres;i++){
                if(i!=0){
                    genres=genres+", ";
                }
                genres = genres + jsonObject.getJSONArray("genres").getJSONObject(i).getString("name");

            }
        }catch (Exception e){
            e.printStackTrace();
        }
       return  genres;
    }

    @Override
    public void onMoviedetailSuccess(JSONObject response) {

        MovieDetail movieDetail1 = new MovieDetail();

        try {
            movieDetail1.setId(id);
            movieDetail1.setGenres(formatGenre(response));
            movieDetail1.setBackdrop_path(response.getString("backdrop_path"));
            movieDetail1.setBudget(response.getString("budget"));
            movieDetail1.setOriginal_language(response.getString("original_language"));
            movieDetail1.setOriginal_title(response.getString("original_title"));
            movieDetail1.setOverview(response.getString("overview"));
            movieDetail1.setRelease_date(response.getString("release_date"));
            movieDetail1.setRevenue(response.getString("revenue"));
            movieDetail1.setRuntime(response.getString("runtime"));
            movieDetail1.setVote_average(response.getString("vote_average"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SetViewDetails(movieDetail1);

    }

    @Override
    public void onMoviedetailError(VolleyError error) {

    }
}
