package com.example.jorge.hellojesus.data.onLine.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jorge on 22/02/2018.
 */

public class MainClient {

    public static final String BASE_URL = "https://private-8ea63-topicscourseenglih.apiary-mock.com/";

    private static Retrofit retrofit = null;

    private static Gson gson = new GsonBuilder()
            .create();


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

}
