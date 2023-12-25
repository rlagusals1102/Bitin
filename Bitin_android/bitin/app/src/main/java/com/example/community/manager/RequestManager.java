package com.example.community.manager;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class RequestManager {
    public static RequestQueue requestQueue = null;

    public static void sendStringRequest(Context context, StringRequest stringRequest) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        requestQueue.add(stringRequest);
    }
}
