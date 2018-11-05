package com.example.jorge.hellojesus.data.onLine.main;

import com.example.jorge.hellojesus.data.onLine.main.model.ListMain;
import com.example.jorge.hellojesus.data.onLine.main.model.Main;
import com.example.jorge.hellojesus.data.onLine.topic.TopicClient;
import com.example.jorge.hellojesus.data.onLine.topic.TopicEndPoint;
import com.example.jorge.hellojesus.data.onLine.topic.TopicServiceApi;
import com.example.jorge.hellojesus.data.onLine.topic.model.ListTopic;
import com.example.jorge.hellojesus.data.onLine.topic.model.Topic;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jorge on 22/02/2018.
 */

public class MainServiceImpl implements MainServiceApi {
    MainEndPoint mRetrofit;

    public MainServiceImpl(){
        mRetrofit = MainClient.getClient().create(MainEndPoint.class);
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

