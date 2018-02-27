package com.example.jorge.hellojesus.topic;

import android.support.annotation.NonNull;

import com.example.jorge.hellojesus.data.onLine.topic.TopicServiceApi;
import com.example.jorge.hellojesus.data.onLine.topic.model.ListTopic;
import com.example.jorge.hellojesus.data.onLine.topic.model.Topic;

import java.util.ArrayList;
import java.util.List;

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
        mTopicServiceApi.getTopics(new TopicServiceApi.TopicServiceCallback<ListTopic<Topic>>(){
            @Override
            public void onLoaded(ListTopic listTopic) {

                List<Topic>  topicListBible = new ArrayList<>();
                List<Topic>  topicListMusic = new ArrayList<>();
                List<Topic>  topicListQuestion = new ArrayList<>();
                List<Topic>  topicListExercise = new ArrayList<>();

                if (listTopic != null){
                    List<Topic>  topicList = (List<Topic>) listTopic.items;
                    for (int i = 0 ; i < topicList.size(); i++){
                        if (topicList.get(i).getType().equals("bible")){
                            topicListBible.add(topicList.get(i));

                        }else if (topicList.get(i).getType().equals("music")){
                            topicListMusic.add(topicList.get(i));

                        }else if (topicList.get(i).getType().equals("question")){
                            topicListQuestion.add(topicList.get(i));

                        }else if (topicList.get(i).getType().equals("exercise")){
                            topicListExercise.add(topicList.get(i));

                        }
                    }
                }

                mTopicContractView.showTopicBible(topicListBible);
                mTopicContractView.showTopicMusic(topicListMusic);
                mTopicContractView.showTopicQuestion(topicListQuestion);
                mTopicContractView.showTopicExercise(topicListExercise);
            }
        });
    }

    @Override
    public void openDetail() {

    }




}
