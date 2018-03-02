package com.example.jorge.hellojesus.content;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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


            void showAllContent();
        }

        interface UserActionsListener {

            void loadingContent(List<Content> contents);

            void ShowFabButton(FloatingActionButton floatingActionButton, Animation animation);

            void HideFabButton(FloatingActionButton floatingActionButton, Animation animation);

            Boolean isActiveFabButton(FloatingActionButton floatingActionButton);

            void openBrowserImage(Context context, TextView word);

            void openBrowserExplanation(Context context, TextView word);

            void openBrowserTranslate(Context context, TextView word);



            void openDetail();

        }
    }