package com.example.jorge.hellojesus.content;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.example.jorge.hellojesus.data.onLine.topic.model.Content;
import com.example.jorge.hellojesus.data.onLine.topic.model.Topic;

import java.util.List;

/**
 * Created by jorge on 27/02/2018.
 */

public interface ContentContract {

        interface View {


            void showContent(List<Content> contents);

            void showProgress(android.view.View root);

            void showAnimation();

            void initializeMediaSession();

            void initializePlayer(Uri mediaUriAudio);

            void setListTime(long[] listTime);



            void showAllContent();
        }

        interface UserActionsListener {


            void loadingContent(List<Content> contents, int mTimeLast);

            void ShowFabButton(FloatingActionButton floatingActionButton, Animation animation);

            void HideFabButton(FloatingActionButton floatingActionButton, Animation animation);

            Boolean isActiveFabButton(FloatingActionButton floatingActionButton);

            void openBrowserImage(Context context, TextView word);

            void openBrowserExplanation(Context context, TextView word);

            void openBrowserTranslate(Context context, TextView word);


            // Play audio
            void initAudio();

            void playAudio();

            void pauseAudio();

            void stopAudio();

            void back5Audio();

            void back30Audio();

            void backAudio();

            void next30Audio();

            void showNotification();


            void releasePlayer();








            void openDetail();

        }
    }