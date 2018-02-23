package com.example.jorge.hellojesus.data.onLine.main;

import com.example.jorge.hellojesus.data.onLine.main.model.ListMain;
import com.example.jorge.hellojesus.data.onLine.main.model.Main;
import com.example.jorge.hellojesus.data.onLine.topic.model.ListTopic;
import com.example.jorge.hellojesus.data.onLine.topic.model.Topic;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by jorge on 22/02/2018.
 */

public interface MainEndPoint {
    @GET("/questions")
    Call<ListMain<Main>> getMain() ;

}
