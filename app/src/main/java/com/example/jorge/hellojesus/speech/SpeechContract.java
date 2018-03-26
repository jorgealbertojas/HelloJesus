package com.example.jorge.hellojesus.speech;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.example.jorge.hellojesus.data.onLine.topic.model.Content;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.util.List;

import jp.shts.android.storiesprogressview.StoriesProgressView;

/**
 * Created by jorge on 16/03/2018.
 */

public interface SpeechContract {
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

        void ShowFabButton(FloatingActionButton floatingActionButton, Animation animation, Button button);

        void HideFabButton(FloatingActionButton floatingActionButton, Animation animation, Button button);

        Boolean isActiveFabButton(FloatingActionButton floatingActionButton);

        void openBrowserImage(Context context, TextView word);

        void openBrowserExplanation(Context context, TextView word);

        void openBrowserTranslate(Context context, TextView word);




        // Play audio
        void initAudio();

        void playAudio(SimpleExoPlayer ExoPlayerAudio, ObjectAnimator objectAnimator, StoriesProgressView storiesProgressView);

        void pauseAudio(SimpleExoPlayer ExoPlayerAudio, ObjectAnimator objectAnimator, StoriesProgressView storiesProgressView);

        void ShowControllerAudio(SimpleExoPlayerView simpleExoPlayerView);

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