package com.softjads.jorge.hellojesus.data.onLine.topic;

import com.softjads.jorge.hellojesus.data.onLine.topic.model.ListTopic;
import com.softjads.jorge.hellojesus.data.onLine.topic.model.Topic;

/**
 * Created by jorge on 21/02/2018.
 */

public interface TopicServiceApi {

    interface TopicServiceCallback<T> {

        void onLoaded(ListTopic<Topic> topic);
    }

    void getTopics(TopicServiceApi.TopicServiceCallback<ListTopic<Topic>> callback);

}