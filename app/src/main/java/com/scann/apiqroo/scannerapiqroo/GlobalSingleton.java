package com.scann.apiqroo.scannerapiqroo;

/**
 * Created by Uriel Velasquez on 17/05/2017.
 */

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class GlobalSingleton {

    // Atributos
    private static GlobalSingleton singleton;
    private RequestQueue requestQueue;
    private static Context context;

    //metodo constructor
    private GlobalSingleton(Context context) {
        GlobalSingleton.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized GlobalSingleton getInstance(Context context) {
        if (singleton == null) {
            singleton = new GlobalSingleton(context);
        }
        return singleton;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    //metodo de asignación de petición a la cola de peticiónes
    public  void addToRequestQueue(Request req) {
        getRequestQueue().add(req);
    }




}
