package com.example.jorge.hellojesus.data.onLine.ad;

import com.example.jorge.hellojesus.data.onLine.ad.model.Ad;
import com.example.jorge.hellojesus.data.onLine.ad.model.ListAd;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AdEndPoint {
    @GET("/questions")
    Call<ListAd<Ad>> getAd() ;

}
