package com.example.jorge.hellojesus.topic;

import com.example.jorge.hellojesus.BasePresenter;
import com.example.jorge.hellojesus.BaseView;
import com.example.jorge.hellojesus.data.onLine.topic.model.Content;
import com.example.jorge.hellojesus.data.onLine.topic.model.Topic;

import java.util.List;

/**
 * Created by jorge on 21/02/2018.
 * Contract View and Present of the Viewr
 */

public interface TopicContract {

    interface View extends BaseView<UserActionsListener> {


        void showTopicBible(List<Topic> topics);

        void showTopicMusic(List<Topic> topics);

        void showTopicExercise(List<Topic> topics);

        void showTopicQuestion(List<Topic> topics);

        void showWords(List<Content> contentList);

        void showLoadingShoppingError();

        boolean isActive();

        void setLoadingIndicator(boolean active);

        void showAllTopics();
    }

    interface UserActionsListener extends BasePresenter {

        void loadingWords(String type, String sourceName, String write);

        void loadingWords(String type);

        void loadingTopic();

        void openDetail();

    }
}
