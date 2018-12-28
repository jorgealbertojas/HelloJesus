package com.example.jorge.hellojesus.data.onLine.ad;

import com.example.jorge.hellojesus.data.onLine.ad.model.Ad;
import com.example.jorge.hellojesus.data.onLine.ad.model.ListAd;
import com.example.jorge.hellojesus.data.onLine.main.MainServiceApi;
import com.example.jorge.hellojesus.data.onLine.main.model.ListMain;
import com.example.jorge.hellojesus.data.onLine.main.model.Main;

public interface AdServiceApi {
    interface AdServiceCallback<T> {

        void onLoaded(ListAd<Ad> adListAd);
    }

    void getAd(AdServiceApi.AdServiceCallback<ListAd<Ad>> callback);

}
