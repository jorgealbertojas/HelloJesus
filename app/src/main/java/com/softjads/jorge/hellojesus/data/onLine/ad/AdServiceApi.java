package com.softjads.jorge.hellojesus.data.onLine.ad;

import com.softjads.jorge.hellojesus.data.onLine.ad.model.Ad;
import com.softjads.jorge.hellojesus.data.onLine.ad.model.ListAd;

public interface AdServiceApi {
    interface AdServiceCallback<T> {

        void onLoaded(ListAd<Ad> adListAd);
    }

    void getAd(AdServiceApi.AdServiceCallback<ListAd<Ad>> callback);

}
