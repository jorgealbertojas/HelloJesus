package com.example.jorge.hellojesus.topic;

import android.support.annotation.NonNull;

import com.example.jorge.hellojesus.data.onLine.topic.model.Topic;

import java.util.List;

/**
 * Created by jorge on 21/02/2018.
 * Contract View and Present of the Viewr
 */

public interface TopicContract {

    interface View {

        void setLoading(boolean isActive);

        void showTopic(List<Topic> topics);

        void showAllTopics();
    }

    interface UserActionsListener {

        void loadingTopic();

        void openDetail();
    }
}
