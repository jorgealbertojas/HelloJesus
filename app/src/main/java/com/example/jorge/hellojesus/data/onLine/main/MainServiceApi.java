package com.example.jorge.hellojesus.data.onLine.main;

import com.example.jorge.hellojesus.data.onLine.main.model.ListMain;
import com.example.jorge.hellojesus.data.onLine.main.model.Main;
import com.example.jorge.hellojesus.data.onLine.topic.TopicServiceApi;
import com.example.jorge.hellojesus.data.onLine.topic.model.ListTopic;
import com.example.jorge.hellojesus.data.onLine.topic.model.Topic;

/**
 * Created by jorge on 22/02/2018.
 */

public interface MainServiceApi {

    interface MainServiceCallback<T> {

    void onLoaded(ListMain<Main> mainListMain);
}

    void getMain(MainServiceApi.MainServiceCallback<ListMain<Main>> callback);

}