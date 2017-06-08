package com.example.zrs.visigoth;

/**
 * Created by nanditakannapadi on 3/31/17.
 */

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by sherriff on 10/25/16.
 */

public class APIClient {

    public static final String BASE_URL = "http://api.reimaginebanking.com/accounts/5938c8f1ceb8abe2425178e1";
    private static Retrofit retrofit = null;



    public static Retrofit getClient(String s) {
        final String BASE_URL_FINAL = BASE_URL + "5938c8f1ceb8abe2425178e1";

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}