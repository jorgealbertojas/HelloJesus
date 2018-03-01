package com.example.jorge.hellojesus.content;

import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.example.jorge.hellojesus.R;
import com.example.jorge.hellojesus.data.onLine.main.MainServiceApi;
import com.example.jorge.hellojesus.data.onLine.main.model.ListMain;
import com.example.jorge.hellojesus.data.onLine.main.model.Main;
import com.example.jorge.hellojesus.data.onLine.topic.model.Content;
import com.example.jorge.hellojesus.main.MainContract;

import java.util.List;

/**
 * Created by jorge on 27/02/2018.
 */

public class ContentPresenter implements ContentContract.UserActionsListener {


    private final ContentContract.View mContentContractView;

    public ContentPresenter(ContentContract.View contentContract_View) {

        this.mContentContractView = contentContract_View;
    }

    @Override
    public void loadingContent(List<Content> contents) {
        mContentContractView.showContent(contents);


    }

    @Override
    public void ShowFabButton(FloatingActionButton floatingActionButton, Animation animation) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) floatingActionButton.getLayoutParams();
        layoutParams.rightMargin += (int) (floatingActionButton.getWidth() * 1.7);
        layoutParams.bottomMargin += (int) (floatingActionButton.getHeight() * 0.25);
        floatingActionButton.setLayoutParams(layoutParams);
        floatingActionButton.startAnimation(animation);
        floatingActionButton.setClickable(true);
    }

    @Override
    public void HideFabButton(FloatingActionButton floatingActionButton, Animation animation) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) floatingActionButton.getLayoutParams();
        layoutParams.rightMargin -= (int) (floatingActionButton.getWidth() * 1.7);
        layoutParams.bottomMargin -= (int) (floatingActionButton.getHeight() * 0.25);
        floatingActionButton.setLayoutParams(layoutParams);
        floatingActionButton.startAnimation(animation);
        floatingActionButton.setClickable(false);
    }




        @Override
        public void openDetail () {

        }


}

