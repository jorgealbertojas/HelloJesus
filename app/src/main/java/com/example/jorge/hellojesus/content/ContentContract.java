package com.example.jorge.hellojesus.content;

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

/**
 * Created by jorge on 27/02/2018.
 * Contract for support View amd Listener User of the Content
 */

public interface ContentContract {

        interface View {


            void showContent(List<Content> contents);

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

            void playAudio(SimpleExoPlayer ExoPlayerAudio);

            void pauseAudio(SimpleExoPlayer ExoPlayerAudio);

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