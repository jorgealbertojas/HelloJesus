package com.softjads.jorge.hellojesus.data.onLine.main;

import android.app.Activity;
import android.content.Context;

import com.softjads.jorge.hellojesus.data.onLine.main.model.ListMain;
import com.softjads.jorge.hellojesus.data.onLine.main.model.Main;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jorge on 22/02/2018.
 */

public class MainServiceImpl implements MainServiceApi {
    MainEndPoint mRetrofit;

    public MainServiceImpl(Activity activity){
        mRetrofit = MainClient.getClient(activity).create(MainEndPoint.class);
    }

    @Override
    public void getMain(final MainServiceCallback<ListMain<Main>> callback) {
        Call<ListMain<Main>> callMain = mRetrofit.getMain();
        callMain.enqueue(new Callback<ListMain<Main>>() {
            @Override
            public void onResponse(Call<ListMain<Main>> call, Response<ListMain<Main>> response) {
                if(response.code()==200){
                    ListMain<Main> resultSearch = response.body();
                    callback.onLoaded(resultSearch);
                }
            }

            @Override
            public void onFailure(Call<ListMain<Main>> call, Throwable t) {
                ListMain<Main> resultSearch = null;

            }
        });
    }


}

