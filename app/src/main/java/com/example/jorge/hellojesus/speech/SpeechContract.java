package com.example.jorge.hellojesus.speech;

import com.example.jorge.hellojesus.data.onLine.topic.model.Content;
import java.util.List;

/**
 * Created by jorge on 16/03/2018.
 * Contract for support Fragment and Presenter Speech
 */

public interface SpeechContract {
    interface View {


        void showContent(List<Content> contents);

        void showProgress(android.view.View root);

        void setListTime(long[] listTime);
    }

    interface UserActionsListener {


        void loadingContent(List<Content> contents, int mTimeLast);

        void onStart();

        void onStop();

        void playAudio();

        void pauseAudio();

    }
}