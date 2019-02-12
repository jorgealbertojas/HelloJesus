package com.softjads.jorge.hellojesus.helloWord;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.softjads.jorge.hellojesus.R;
import com.softjads.jorge.hellojesus.data.local.WordsRepository;
import com.softjads.jorge.hellojesus.data.onLine.topic.model.Content;

import java.util.List;

public class HelloWordPresenter implements HelloWordContract.UserActionsListener {


    private final HelloWordContract.View mHelloWordContractView;
    private final WordsRepository mWordsRepository;

    public HelloWordPresenter(HelloWordContract.View contentContract_View, WordsRepository wordsRepository) {

        this.mWordsRepository = wordsRepository;
        this.mHelloWordContractView = contentContract_View;
    }


    @Override
    public void ShowFabButton(FloatingActionButton floatingActionButton, Animation animation, Button button) {
        if (!isActiveFabButton(floatingActionButton)) {
            floatingActionButton.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) floatingActionButton.getLayoutParams();
            layoutParams.rightMargin += (int) (floatingActionButton.getWidth() * 1.7);
            layoutParams.bottomMargin += (int) (floatingActionButton.getHeight() * 0.25);
            floatingActionButton.setLayoutParams(layoutParams);
            floatingActionButton.startAnimation(animation);
            floatingActionButton.setClickable(true);
            button.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void HideFabButton(FloatingActionButton floatingActionButton, Animation animation, Button button) {
        if (isActiveFabButton(floatingActionButton)) {
            floatingActionButton.setVisibility(View.INVISIBLE);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) floatingActionButton.getLayoutParams();
            layoutParams.rightMargin -= (int) (floatingActionButton.getWidth() * 1.7);
            layoutParams.bottomMargin -= (int) (floatingActionButton.getHeight() * 0.25);
            floatingActionButton.setLayoutParams(layoutParams);
            floatingActionButton.startAnimation(animation);
            floatingActionButton.setClickable(false);
            button.setVisibility(View.GONE);
        }


    }

    @Override
    public Boolean isActiveFabButton(FloatingActionButton floatingActionButton) {
        return floatingActionButton.getVisibility() == View.VISIBLE;
    }

    @Override
    public void openBrowserImage(Context context, TextView word) {
        String url = "https://www.google.com.br/search?hl=pt-BR&tbm=isch&source=hp&biw=1080&bih=1765&ei=X_Z8Wu-8K4iiwgTsvqKADw&q=" + word.getText().toString() + "&oq=" + word.getText().toString() + "&gs_l=img.3...2937.4111.0.4927.0.0.0.0.0.0.0.0..0.0....0...1ac.1.64.img..0.0.0....0.dYQYv-zXKss";

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(context.getResources().getColor(R.color.colorPrimary));
        builder.setCloseButtonIcon(BitmapFactory.decodeResource(
                context.getResources(), R.drawable.ic_arrow_back_white_24dp));

        builder.setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        builder.setShowTitle(true);

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl((Activity) context, Uri.parse(url));
    }

    @Override
    public void openBrowserExplanation(Context context, TextView word) {
        String url = "https://en.oxforddictionaries.com/definition/" + word.getText().toString();

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(context.getResources().getColor(R.color.colorPrimary));
        builder.setCloseButtonIcon(BitmapFactory.decodeResource(
                context.getResources(), R.drawable.ic_arrow_back_white_24dp));

        builder.setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        builder.setShowTitle(true);

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl((Activity) context, Uri.parse(url));
    }

    @Override
    public void openBrowserTranslate(Context context, TextView word) {
        String url = "https://translate.google.com.br/?hl=pt-BR&tab=wT#en/pt/" + word.getText().toString();

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(context.getResources().getColor(R.color.colorPrimary));
        builder.setCloseButtonIcon(BitmapFactory.decodeResource(
                context.getResources(), R.drawable.ic_arrow_back_white_24dp));

        builder.setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        builder.setShowTitle(true);

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl((Activity) context, Uri.parse(url));
    }





    private void processWords(List<Content> wordList) {

        if (wordList.isEmpty()) {

        } else {

            mHelloWordContractView.showHelloWords(wordList);

        }
    }





}

