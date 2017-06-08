package com.example.zrs.visigoth;

/**
 * Created by nanditakannapadi on 3/31/17.
 */

import android.content.SharedPreferences;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface APIInterface {


//    @GET("{user}")
//    Call<List<Alcohol>> alcoholList(@Path("user") String user);

    //Add user parameter to this
//    @FormUrlEncoded
//    @POST("adddrinks/")
//    Call<Alcohol> adddrink(@Field String id, @Field String day, @Field Double beer, @Field Double wine, @Field Double shots, @Field Double liquor, @Field Double money);

    @FormUrlEncoded
    @POST("transfers?key=67d9a238a69baa7daee2a3a22bd1ee75")
    Call<Example> transfer(@Field("medium") String medium, @Field("payee_id") String payee_id, @Field("amount") String amount, @Field("transaction_date") String transaction_date, @Field("description") String description);

    /*@FormUrlEncoded
    @GET("5938c8f1ceb8abe2425178e1/transfers?key=67d9a238a69baa7daee2a3a22bd1ee75")
    Call<>*/


}