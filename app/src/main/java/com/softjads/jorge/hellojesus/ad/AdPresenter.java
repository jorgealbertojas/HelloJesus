package com.softjads.jorge.hellojesus.ad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.softjads.jorge.hellojesus.data.local.Word;
import com.softjads.jorge.hellojesus.data.local.WordsDataSource;
import com.softjads.jorge.hellojesus.data.local.WordsRepository;
import com.softjads.jorge.hellojesus.data.local.help.Help;
import com.softjads.jorge.hellojesus.data.onLine.ad.AdServiceApi;
import com.softjads.jorge.hellojesus.data.onLine.ad.model.Ad;
import com.softjads.jorge.hellojesus.data.onLine.ad.model.ListAd;
import com.softjads.jorge.hellojesus.helpApp.HelpAppActivity;
import com.softjads.jorge.hellojesus.util.Common;
import com.softjads.jorge.hellojesus.util.EspressoIdlingResource;
import com.softjads.jorge.hellojesus.word.WordActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.supercharge.shimmerlayout.ShimmerLayout;

import static com.softjads.jorge.hellojesus.word.WordFragment.EXTRA_BUNDLE_WORD;
import static com.softjads.jorge.hellojesus.word.WordFragment.EXTRA_WORD;
import static com.softjads.jorge.hellojesus.word.WordFragment.EXTRA_WORD_CHECK;
import static com.google.common.base.Preconditions.checkNotNull;

public class AdPresenter implements AdContract.UserActionsListener {

    private final AdServiceApi mAdServiceApi;
    private final AdContract.View mAdContractView;
    private final WordsRepository mWordsRepository;

    public static int[] locationInScreen = new int[]{0,0};

    public AdPresenter(WordsRepository wordsRepository, AdServiceApi mAdServiceApi, AdContract.View adContract_View) {
        this.mAdServiceApi = mAdServiceApi;
        this.mAdContractView = adContract_View;
        mWordsRepository = checkNotNull(wordsRepository, "tasksRepository cannot be null");

    }

    @Override
    public void loadingAd(final ShimmerLayout shimmerLayout) {
        shimmerLayout.setVisibility(View.VISIBLE);
        shimmerLayout.startShimmerAnimation();
        mAdServiceApi.getAd(new AdServiceApi.AdServiceCallback<ListAd<Ad>>() {
            @Override
            public void onLoaded(ListAd<Ad> adListAd) {
                shimmerLayout.stopShimmerAnimation();
                shimmerLayout.setVisibility(View.GONE);
                mAdContractView.showAd(adListAd.items);
            }


        });
    }

    @Override
    public void loadWordWrong(final Context context) {

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        EspressoIdlingResource.increment();

        mWordsRepository.getWordsWrong(new WordsDataSource.LoadWordCallback() {

            @Override
            public void onWordLoaded(List<Word> wordList) {
                List<String> arrayList = new ArrayList<>();

                // This callback may be called twice, once for the cache and once for loading
                // the data from the server API, so we check before decrementing, otherwise
                // it throws "Counter has been corrupted!" exception.
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement(); // Set app as idle.
                }

                for (Word word : wordList) {
                    arrayList.add(word.getWord());
                }

                Intent intent = new Intent(context, WordActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable(EXTRA_WORD, (Serializable) arrayList);
                bundle.putString(EXTRA_WORD_CHECK, "0");
                intent.putExtra(EXTRA_BUNDLE_WORD, bundle);

                context.startActivity(intent);

            }


            @Override
            public void onDataNotAvailable() {

            }

        });
    }

    @Override
    public void loadWordCorrect(final Context context) {
        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        EspressoIdlingResource.increment();

        mWordsRepository.getWordsCorrect(new WordsDataSource.LoadWordCallback() {

            @Override
            public void onWordLoaded(List<Word> wordList) {
                List<String> arrayList = new ArrayList<>();

                // This callback may be called twice, once for the cache and once for loading
                // the data from the server API, so we check before decrementing, otherwise
                // it throws "Counter has been corrupted!" exception.
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement(); // Set app as idle.
                }
                for (Word word : wordList) {
                    arrayList.add(word.getWord());
                }

                Intent intent = new Intent(context, WordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(EXTRA_WORD, (Serializable) arrayList);
                bundle.putString(EXTRA_WORD_CHECK, "1");
                intent.putExtra(EXTRA_BUNDLE_WORD, bundle);
                context.startActivity(intent);
            }


            @Override
            public void onDataNotAvailable() {

            }

        });
    }

    @Override
    public void saveHelp(Help help) {
        mWordsRepository.saveHelp(help);
    }

    @Override
    public void loadHelp(View root, Context context) {
        EspressoIdlingResource.increment();

        mWordsRepository.getHelp(new WordsDataSource.LoadHelpCallback() {

            @Override
            public void onHelpLoaded(List<Help> helpList, View root, Context context) {
                List<String> stringList = new ArrayList<String>();
                List<String> stringListTemp = new ArrayList<String>();
                List<Integer> stringListX = new ArrayList<Integer>();
                List<Integer> stringListY = new ArrayList<Integer>();
                List<String> stringListTOP= new ArrayList<String>();

                // This callback may be called twice, once for the cache and once for loading
                // the data from the server API, so we check before decrementing, otherwise
                // it throws "Counter has been corrupted!" exception.
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement(); // Set app as idle.
                }

                // We filter the tasks based on the requestType
                int i = 0;
                for (Help help : helpList) {
                    int valueID = 0;
                    valueID = Common.getResourceString(help.getMkey());
                    if (valueID != 0) {
                        View vievHelp2 = (View) root.findViewById(valueID);

                        if (vievHelp2  == null){
                            vievHelp2 = (View) root.getRootView().findViewById(valueID);
                        }




                        if ((vievHelp2  != null) && (!stringList.contains(help.getMkey()))) {
                            vievHelp2.getLocationOnScreen(locationInScreen);
                            stringList.add(help.getMkey());
                            stringListTemp.add(help.getMvalue());
                            stringListTOP.add(help.getMlast());
                            stringListX.add(locationInScreen[0]);
                            int temp = locationInScreen[1] - (vievHelp2.getHeight() / 2);
                            if (temp < 0){
                                temp = temp * (-1);
                            }
                            stringListY.add(temp);
                        }
                    }
                    i++;
                }

                Intent intent = new Intent(context, HelpAppActivity.class);
                intent.putExtra("HELP_ID", (Serializable) stringListTemp);
                intent.putExtra("HELP_X", (Serializable) stringListX);
                intent.putExtra("HELP_Y", (Serializable) stringListY);
                intent.putExtra("HELP_TOP", (Serializable) stringListTOP);

                context.startActivity(intent);

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


    @Override
    public void start() {
        // loadTasks(false);
    }




}
