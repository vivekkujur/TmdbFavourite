package com.tmdbfav.rest;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface RestGetMovieListInterface {
    void onMovieListSuccess(JSONObject response);
    void onMovieListError(VolleyError error);
}
