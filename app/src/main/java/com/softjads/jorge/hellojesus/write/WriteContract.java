package com.softjads.jorge.hellojesus.write;

import android.net.Uri;

import com.softjads.jorge.hellojesus.data.onLine.topic.model.Content;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.List;

/**
 * Created by jorge on 06/04/2018.
 * Contract for support Fragment and Presenter Write
 */

public interface WriteContract {
    interface View {


        void showContent(List<Content> contents);


        void initializeMediaSession();

        void initializePlayer(Uri mediaUriAudio);

        void setListTime(long[] listTime);
    }

    interface UserActionsListener {


        void loadingContent(List<Content> contents, int mTimeLast);

        void onStart();

        void onStop();

        void playAudio(SimpleExoPlayer ExoPlayerAudio);

        void pauseAudio(SimpleExoPlayer ExoPlayerAudio);

    }
}
