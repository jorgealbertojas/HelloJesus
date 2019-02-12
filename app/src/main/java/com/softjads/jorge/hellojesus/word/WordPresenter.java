package com.softjads.jorge.hellojesus.word;

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
import com.softjads.jorge.hellojesus.data.local.Word;
import com.softjads.jorge.hellojesus.data.local.WordsDataSource;
import com.softjads.jorge.hellojesus.data.local.WordsRepository;
import com.softjads.jorge.hellojesus.data.onLine.topic.model.Content;
import com.softjads.jorge.hellojesus.util.EspressoIdlingResource;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by jorge on 21/04/2018.
 */

public class WordPresenter implements WordContract.UserActionsListener {


    private final WordContract.View mWordContractView;
    private final WordsRepository mWordsRepository;

    public WordPresenter(WordContract.View contentContract_View, WordsRepository wordsRepository) {

        this.mWordsRepository = wordsRepository;
        this.mWordContractView = contentContract_View;
    }


    @Override
    public void saveWordQuantity(String wordName, String type, String sourceName, String countTime, String statusSaid, String statusWrite) {
        Word word = new Word(wordName, type,  countTime, sourceName,  statusSaid, statusWrite);

        mWordsRepository.saveWordQuantity(word);
    }

    @Override
    public void saveWordWrite(String wordName, String type, String sourceName, String countTime, String statusSaid, String statusWrite) {
        Word word = new Word(wordName, type,  countTime, sourceName,  statusSaid, statusWrite);
        mWordsRepository.saveWordWrite(word, statusWrite);
    }

    @Override
    public void saveWordSaid(String wordName, String type, String sourceName, String countTime, String statusSaid, String statusWrite) {
        Word word = new Word(wordName, type,  countTime, sourceName,  statusSaid, statusWrite);
        mWordsRepository.saveWordSaid(word, statusSaid);
    }

    @Override
    public void loadingWords(List<String> stringList) {

    }

    @Override
    public void loadingWord(String stringList) {
        loadWord();
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



    @Override
    public void openDetail () {

    }

    private void loadWord() {

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        EspressoIdlingResource.increment();

        mWordsRepository.getWords(new WordsDataSource.LoadWordCallback() {

            @Override
            public void onWordLoaded(List<Word> wordList) {
                List<Content> arrayList = new ArrayList<Content>();

                // This callback may be called twice, once for the cache and once for loading
                // the data from the server API, so we check before decrementing, otherwise
                // it throws "Counter has been corrupted!" exception.
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement(); // Set app as idle.
                }

                // We filter the tasks based on the requestType
                int i = 0;
                for (Word word : wordList) {
                    Content content = new Content();
                    content.setContent_english(word.getWord());
                    content.setContent_portuguese(word.getWord());
                    content.setCorret_option(word.getType());
                    content.setId_content(1);
                    content.setTime(1);
                    arrayList.add(i,content);
                    i++;
                }

                // The view may not be able to handle UI updates anymore
                if (!mWordContractView.isActive()) {
                    return;
                }
                //if (showLoadingUI) {
                if (true) {
                    mWordContractView.setLoadingIndicator(false);
                }

                processWords(arrayList);
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mWordContractView.isActive()) {
                    return;
                }
                mWordContractView.showLoadingWordError();
            }

        });
    }


    private void processWords(List<Content> wordList) {

        if (wordList.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type.
            //processEmptyTasks();
        } else {
            // Show the list of tasks
            mWordContractView.showWords(wordList);
            // Set the filter label's text.
            // showFilterLabel();
        }
    }


    @Override
    public void start() {

    }

    @Override
    public void loadHelp(View root, Context context) {

    }
}


