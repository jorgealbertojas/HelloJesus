package com.softjads.jorge.hellojesus.data.onLine.main;

import com.softjads.jorge.hellojesus.data.onLine.main.model.ListMain;
import com.softjads.jorge.hellojesus.data.onLine.main.model.Main;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by jorge on 22/02/2018.
 */

public interface MainEndPoint {
    @GET("/questions")
    Call<ListMain<Main>> getMain() ;

}
