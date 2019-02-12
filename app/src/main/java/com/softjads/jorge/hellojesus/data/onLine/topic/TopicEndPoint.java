package com.softjads.jorge.hellojesus.data.onLine.topic;

import com.softjads.jorge.hellojesus.data.onLine.topic.model.ListTopic;
import com.softjads.jorge.hellojesus.data.onLine.topic.model.Topic;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by jorge on 21/02/2018.
 */

public interface TopicEndPoint{
        @GET("/questions")
        Call<ListTopic<Topic>> getTopic() ;

}
