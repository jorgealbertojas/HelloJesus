package com.softjads.jorge.hellojesus.helpApp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.softjads.jorge.hellojesus.data.local.WordsDataSource;
import com.softjads.jorge.hellojesus.data.local.WordsRepository;
import com.softjads.jorge.hellojesus.data.local.help.Help;
import com.softjads.jorge.hellojesus.data.onLine.topic.model.Content;
import com.softjads.jorge.hellojesus.util.EspressoIdlingResource;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class HelpPresenter implements HelpContract.UserActionsListener {

    private final WordsRepository mWordsRepository;

    private final HelpContract.View mHelpView;

    public HelpPresenter(@NonNull WordsRepository wordsRepository, @NonNull HelpContract.View ProgressView) {
        mWordsRepository = checkNotNull(wordsRepository, "tasksRepository cannot be null");
        mHelpView = checkNotNull(ProgressView, "tasksView cannot be null!");
        mHelpView.setPresenter(this);

    }

    @Override
    public void start() {

    }

    @Override
    public void loadHelp(View root, Context context) {
        EspressoIdlingResource.increment();

        mWordsRepository.getHelp(new WordsDataSource.LoadHelpCallback() {



            @Override
            public void onHelpLoaded(List<Help> contentList, View root, Context context) {
                List<Content> arrayList = new ArrayList<Content>();

                // This callback may be called twice, once for the cache and once for loading
                // the data from the server API, so we check before decrementing, otherwise
                // it throws "Counter has been corrupted!" exception.
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement(); // Set app as idle.
                }

                // We filter the tasks based on the requestType
                int i = 0;
                for (Help help : contentList) {
/*                    Content content = new Content();
                    content.setContent_english(word.getWord());
                    content.setContent_portuguese(word.getWord());
                    content.setCorret_option(word.getType());
                    content.setId_content(1);
                    content.setTime(1);
                    arrayList.add(i,content);*/
                    i++;
                }

/*                // The view may not be able to handle UI updates anymore
                if (!mTopicContractView.isActive()) {
                    return;
                }
                //if (showLoadingUI) {
                if (true) {
                    mTopicContractView.setLoadingIndicator(false);
                }

                processWords(arrayList);*/

            }

            @Override
            public void onDataNotAvailable() {
/*                // The view may not be able to handle UI updates anymore
                if (!mTopicContractView.isActive()) {
                    return;
                }
                mTopicContractView.showLoadingShoppingError();*/
            }

        },root,context);
    }
}
