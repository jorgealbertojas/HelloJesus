package com.example.jorge.hellojesus.topic;

import android.support.annotation.NonNull;

import com.example.jorge.hellojesus.data.onLine.topic.TopicServiceApi;
import com.example.jorge.hellojesus.data.onLine.topic.model.ListTopic;
import com.example.jorge.hellojesus.data.onLine.topic.model.Topic;

/**
 * Created by jorge on 21/02/2018.
 * Presenter
 */

public class TopicPresenter  implements TopicContract.UserActionsListener {

    private final TopicServiceApi mTopicServiceApi;
    private final TopicContract.View mTopicContractView;

    public TopicPresenter(TopicServiceApi topicServiceApi, TopicContract.View topicContract_View) {
        this.mTopicContractView = topicContract_View;
        this.mTopicServiceApi = topicServiceApi;
    }


    @Override
    public void loadingTopic() {

    }

    @Override
    public void openDetail() {
        mTopicContractView.setLoading(true);
        mTopicServiceApi.getTopics(new TopicServiceApi.TopicServiceCallback<ListTopic<Topic>>(){
            @Override
            public void onLoaded(ListTopic listTopic) {
                mTopicContractView.setLoading(false);
              //  mTopicContractView.showTopic(listTopic.items);
            }
        });
    }


}
