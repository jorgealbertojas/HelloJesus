package com.example.jorge.hellojesus.data.onLine.topic;

import com.example.jorge.hellojesus.data.onLine.topic.model.ListTopic;
import com.example.jorge.hellojesus.data.onLine.topic.model.Topic;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jorge on 21/02/2018.
 */

public class TopicServiceImpl implements TopicServiceApi {
    TopicEndPoint mRetrofit;

    public TopicServiceImpl(){
        mRetrofit = TopicClient.getClient().create(TopicEndPoint.class);
    }

    @Override
    public void getTopics(final TopicServiceCallback<ListTopic<Topic>> callback) {
        Call<ListTopic<Topic>> callTopic = mRetrofit.getTopic();
        callTopic.enqueue(new Callback<ListTopic<Topic>>() {
            @Override
            public void onResponse(Call<ListTopic<Topic>> call, Response<ListTopic<Topic>> response) {
                if(response.code()==200){
                    ListTopic<Topic> resultSearch = response.body();
                    callback.onLoaded(resultSearch);
                }
            }

            @Override
            public void onFailure(Call<ListTopic<Topic>> call, Throwable t) {

            }
        });
    }
}

