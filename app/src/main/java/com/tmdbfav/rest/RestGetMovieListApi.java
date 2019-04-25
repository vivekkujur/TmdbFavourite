package com.tmdbfav.rest;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tmdbfav.Setting.Config;
import com.tmdbfav.utils.App;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RestGetMovieListApi implements Response.Listener,Response.ErrorListener  {

    public static final String GET_CATEGORY = "GET_CATEGORY";
    RestGetMovieListInterface listener;

    public RestGetMovieListApi(RestGetMovieListInterface l) {
        listener = l;
    }

    public void getMOvielist() {

        String url = Config.BaseUrl +"movie/now_playing?api_key="+Config.api_key;

        HashMap<String, String> mRequestParams = new HashMap<>();

        final JSONObject jsonObject = new JSONObject(mRequestParams);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, jsonObject, this, this);

        App.cancelPendingRequest(GET_CATEGORY);
        App.addToRequestQueue(jsonObjectRequest, GET_CATEGORY);

    }
    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("response", "err: "+ error.toString());
        listener.onMovieListError(error);
    }

    @Override
    public void onResponse(Object response) {
        JSONObject jsonObject = (JSONObject) response;
        listener.onMovieListSuccess(jsonObject);
    }
}
