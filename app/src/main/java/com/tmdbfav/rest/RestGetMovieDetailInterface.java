package com.tmdbfav.rest;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface RestGetMovieDetailInterface {
    void onMoviedetailSuccess(JSONObject response);
    void onMoviedetailError(VolleyError error);
}
