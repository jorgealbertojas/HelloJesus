package com.softjads.jorge.hellojesus.data.onLine.ad;

import com.softjads.jorge.hellojesus.data.onLine.ad.model.Ad;
import com.softjads.jorge.hellojesus.data.onLine.ad.model.ListAd;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdServiceImpl implements AdServiceApi {
    AdEndPoint mRetrofit;

    public AdServiceImpl(){
        mRetrofit = AdClient.getClient().create(AdEndPoint.class);
    }

    @Override
    public void getAd(final AdServiceCallback<ListAd<Ad>> callback) {
        Call<ListAd<Ad>> callAd = mRetrofit.getAd();
        callAd.enqueue(new Callback<ListAd<Ad>>() {
            @Override
            public void onResponse(Call<ListAd<Ad>> call, Response<ListAd<Ad>> response) {
                if(response.code()==200){
                    ListAd<Ad> resultSearch = response.body();
                    callback.onLoaded(resultSearch);
                }
            }

            @Override
            public void onFailure(Call<ListAd<Ad>> call, Throwable t) {
                ListAd<Ad> resultSearch = null;

            }
        });
    }


}
