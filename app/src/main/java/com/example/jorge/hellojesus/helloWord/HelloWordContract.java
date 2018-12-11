package com.example.jorge.hellojesus.helloWord;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.example.jorge.hellojesus.data.onLine.topic.model.Content;

import java.util.List;

public class HelloWordContract {

    interface View {

        void showHelloWords(List<Content> contentList);

        void ShowInformationHelloWord();

        void showLoadingHelloWordError();

        boolean isActive();

        void setLoadingIndicator(boolean active);
    }

    interface UserActionsListener {

        void ShowFabButton(FloatingActionButton floatingActionButton, Animation animation, Button button);

        void HideFabButton(FloatingActionButton floatingActionButton, Animation animation, Button button);

        Boolean isActiveFabButton(FloatingActionButton floatingActionButton);

        void openBrowserImage(Context context, TextView word);

        void openBrowserExplanation(Context context, TextView word);

        void openBrowserTranslate(Context context, TextView word);

    }
}