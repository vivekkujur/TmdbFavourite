package com.tmdbfav.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tmdbfav.R;
import com.tmdbfav.Setting.Config;
import com.tmdbfav.model.Movie;
import com.tmdbfav.ui.FavoriteActivity;
import com.tmdbfav.ui.MovieActivity;
import com.tmdbfav.utils.SharedStorage;

import java.util.ArrayList;
import java.util.List;

public class FavRecyclerAdapter  extends RecyclerView.Adapter<FavRecyclerAdapter.ViewHolder> {

    FavoriteActivity favAvtivity;
    final String[] Selected = new String[]{"Jan", "Feb", "Mar", "Apr",
            "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    List<Movie> movies ;


    public FavRecyclerAdapter(FavoriteActivity mainActivity, List<Movie> movieList) {
        this.favAvtivity= mainActivity;
        this.movies = movieList;
    }

    public FavRecyclerAdapter() {

    }
    @NonNull
    @Override
    public FavRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new FavRecyclerAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favmovie, parent, false));
    }

    @SuppressLint({"CheckResult", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final FavRecyclerAdapter.ViewHolder viewHolder, final int i) {


        Picasso.get().load(Config.ImageBaseUrl+movies.get(i).getPoster_path()).placeholder(R.drawable.ic_image_black_24dp)
                .into(viewHolder.poster);

        viewHolder.language.setText(movies.get(i).getOriginal_language());
        viewHolder.desc.setText(movies.get(i).getOverview());
        viewHolder.title.setText(movies.get(i).getTitle());
        viewHolder.rating.setText(movies.get(i).getVote_average());

        try{
            String datestr = movies.get(i).getRelease_date();

            String[] arrOfStr = datestr.split("-");
            if(Integer.parseInt(arrOfStr[2]) > 10 && Integer.parseInt(arrOfStr[2])<14)
                arrOfStr[2]   = arrOfStr[2] + "th ";
            else{
                if(arrOfStr[2].endsWith("1")) arrOfStr[2] = arrOfStr[2] + "st";
                else if(arrOfStr[2].endsWith("2")) arrOfStr[2] = arrOfStr[2] + "nd";
                else if(arrOfStr[2].endsWith("3")) arrOfStr[2] = arrOfStr[2] + "rd";
                else arrOfStr[2] = arrOfStr[2] + "th";
            }

            viewHolder.date.setText(arrOfStr[2]+" "+Selected[Integer.parseInt(arrOfStr[1])-1]+arrOfStr[0]);

        }catch(Exception e){
            e.printStackTrace();
        }

        viewHolder.mainlv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(favAvtivity, MovieActivity.class);
                intent.putExtra("id",movies.get(i).getId());

                favAvtivity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return movies.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc, date, rating , language;
        ImageView poster,favImage;
        ConstraintLayout mainlv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title= itemView.findViewById(R.id.titleMovie);
            desc= itemView.findViewById(R.id.descMovie);
            date= itemView.findViewById(R.id.date);
            rating = itemView.findViewById(R.id.rating);
            language= itemView.findViewById(R.id.language);
            poster= itemView.findViewById(R.id.imageMovie);
            mainlv= itemView.findViewById(R.id.mainlv);

        }
    }


}
