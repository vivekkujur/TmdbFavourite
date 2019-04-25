package com.tmdbfav.utils;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class App extends Application {
   public static Context context;
    public void onCreate() {
        super.onCreate();

             context = getApplicationContext();
    }

  public static void  addToRequestQueue(Request req, String tag) {
        req.setTag(tag);
        Volley.newRequestQueue(context).add(req);
    }
    public static void cancelPendingRequest( String req) {
        RequestQueue mRequestQueue=Volley.newRequestQueue(context) ;
        mRequestQueue.cancelAll(req);
    }
}